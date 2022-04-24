package com.nisecoder.gradle.plugin.nodejs

import com.nisecoder.gradle.plugin.nodejs.binary.NodeBinaryArchiveType.Zip
import com.nisecoder.gradle.plugin.nodejs.binary.NodeBinaryPathResolver
import com.nisecoder.gradle.plugin.nodejs.binary.NodeBinaryType
import com.nisecoder.gradle.plugin.nodejs.binary.NodeBinaryTypeSelector
import org.gradle.api.GradleException
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

    /**
     * provision node.js binary path
     *
     * now only supports installing node.js from nodejs.org
     *
     * @param nodeVersion version
     */
    fun provision(nodeVersion: NodeVersion): NodePath {
        val version = nodeVersion.fixed.get()

        val nodeCacheDir = parameters.nodeInstallationPath.get().asFile.also {
            if (!it.exists()) {
                it.mkdirs()
            }
        }
        val installationDirName = nodeBinaryType.let {
            val osName = it.osName
            val arch = it.arch
            "node-$version-$osName-$arch"
        }
        val fileName = nodeBinaryType.let {
            val osName = it.osName
            val arch = it.arch
            val ext = it.ext.value
            "node-$version-$osName-$arch.$ext"
        }

        val installationDir = nodeCacheDir.resolve(installationDirName).toPath()
        if (!installationDir.toFile().exists()) {
            val dist = nodeCacheDir.resolve(fileName)
            val uri = URI.create("https://nodejs.org/dist/$version/$fileName")
            val request = HttpRequest.newBuilder(uri).GET().build()
            val client = HttpClient.newHttpClient()
            val response = client.send(request, HttpResponse.BodyHandlers.ofFile(dist.toPath()))
            if (response.statusCode() != 200) {
                throw GradleException("node $version is not found. Please check https://nodejs.org/en/download/releases/")
            }
            // TODO verify using checksum
            unpack(dist.toPath(), nodeCacheDir.toPath())
            dist.delete()
        }

        val resolver = NodeBinaryPathResolver(installationDir, nodeBinaryType)
        return resolver.toNodePath()
    }

    private fun NodeBinaryPathResolver.toNodePath(): NodePath {
        return NodePath(
            nodeBinaryType,
            installPath,
            resolveBinPath(),
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
        return installationDir
    }

}
