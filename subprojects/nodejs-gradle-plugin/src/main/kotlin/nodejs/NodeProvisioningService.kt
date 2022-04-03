package com.nisecoder.gradle.plugin.nodejs

import com.nisecoder.gradle.plugin.nodejs.binary.NodeBinaryArchiveType.Zip
import com.nisecoder.gradle.plugin.nodejs.binary.NodeBinaryPathResolver
import com.nisecoder.gradle.plugin.nodejs.binary.NodeBinaryType
import com.nisecoder.gradle.plugin.nodejs.binary.NodeBinaryTypeSelector
import org.gradle.api.file.ArchiveOperations
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileSystemOperations
import org.gradle.api.file.FileTree
import org.gradle.api.services.BuildService
import org.gradle.api.services.BuildServiceParameters
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.file.Files
import java.nio.file.Path
import javax.inject.Inject

abstract class NodeProvisioningService: BuildService<NodeProvisioningService.Params> {
    private val nodeBinaryType: NodeBinaryType = NodeBinaryTypeSelector.select()

    interface Params: BuildServiceParameters {
        val nodeInstallationPath: DirectoryProperty
    }

    @get:Inject
    abstract val archiveOps: ArchiveOperations
    @get:Inject
    abstract val fsOps: FileSystemOperations

    fun provision(nodeVersion: String): NodePath {
        val nodeCacheDir = parameters.nodeInstallationPath.get().asFile.also {
            if (!it.exists()) {
                it.mkdirs()
            }
        }
        val installationDirName = nodeBinaryType.let {
            val osName = it.osName
            val arch = it.arch
            "node-$nodeVersion-$osName-$arch"
        }
        val fileName = nodeBinaryType.let {
            val osName = it.osName
            val arch = it.arch
            val ext = it.ext.value
            "node-$nodeVersion-$osName-$arch.$ext"
        }

        val installationDir = nodeCacheDir.resolve(installationDirName).toPath()
        if (!installationDir.toFile().exists()) {
            val dist = nodeCacheDir.resolve(fileName)
            val uri = URI.create("https://nodejs.org/dist/$nodeVersion/$fileName")
            val request = HttpRequest.newBuilder(uri).GET().build()
            val client = HttpClient.newHttpClient()
            client.send(request, HttpResponse.BodyHandlers.ofFile(dist.toPath()))
            // TODO verify using checksum
            unpack(dist.toPath(), nodeCacheDir.toPath())
            dist.delete()
        }

        val resolver = NodeBinaryPathResolver(installationDir, nodeBinaryType)
        return resolver.toNodePath()
    }

    private fun NodeBinaryPathResolver.toNodePath(): NodePath {
        return NodePath(
            installPath,
            resolveNode(),
            resolveNpm(),
            resolveNpx(),
            resolveCorepack(),
        )
    }

    private fun unpack(archiveFile: Path, installationDir: Path): Path {
        val fileTree: FileTree = when(nodeBinaryType.ext) {
            Zip -> archiveOps.zipTree(archiveFile)
            else -> archiveOps.tarTree(archiveFile)
        }
        fsOps.copy {
            from(fileTree)
            into(installationDir)
        }
        fsOps.copy {
            from(fileTree.filter { Files.isSymbolicLink(it.toPath()) })
            eachFile {
                exclude()
                Files.copy(Files.readSymbolicLink(file.toPath()), relativePath.getFile(installationDir.toFile()).toPath())
            }
        }
        return installationDir
    }

}
