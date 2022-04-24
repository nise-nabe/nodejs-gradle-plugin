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
import org.gradle.api.provider.Property
import org.gradle.api.services.BuildService
import org.gradle.api.services.BuildServiceParameters
import java.io.File
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.file.Path
import java.security.MessageDigest
import javax.inject.Inject

abstract class NodeProvisioningService: BuildService<NodeProvisioningService.Params> {
    private val nodeBinaryType: NodeBinaryType = NodeBinaryTypeSelector.select()

    interface Params: BuildServiceParameters {
        val nodeInstallationPath: DirectoryProperty
        /** if true, node.js binary file check the checksum */
        val verifyChecksum: Property<Boolean>
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
            val client = HttpClient.newHttpClient()
            val dist = client.fetchNodeBinary(version, fileName, nodeCacheDir.resolve(fileName))

            if (parameters.verifyChecksum.get()) {
                val expected = client.fetchNodeBinaryChecksum(version, fileName)
                if (expected != dist.digest()) {
                    throw GradleException("node.js binary checksum mismatch")
                }
            }

            unpack(dist, nodeCacheDir.toPath())
            dist.toFile().delete()
        }

        val resolver = NodeBinaryPathResolver(installationDir, nodeBinaryType)
        return resolver.toNodePath()
    }

    private fun HttpClient.fetchNodeBinary(version: String, fileName: String, dist: File): Path {
        val uri = URI.create("https://nodejs.org/dist/$version/$fileName")
        val request = HttpRequest.newBuilder(uri).GET().build()
        val response = send(request, HttpResponse.BodyHandlers.ofFile(dist.toPath()))
        if (response.statusCode() != 200) {
            throw GradleException("node $version is not found. Please check https://nodejs.org/en/download/releases/")
        }
        return response.body()
    }

    private fun HttpClient.fetchNodeBinaryChecksum(version: String, fileName: String): String {
        val uri = URI.create("https://nodejs.org/dist/$version/SHASUMS256.txt")
        val checksums = send(
            HttpRequest.newBuilder(uri).GET().build(),
            HttpResponse.BodyHandlers.ofString()
        ).let {
            if (it.statusCode() != 200) {
                throw GradleException("fail to get checksum file: uri=$uri")
            }
            it.body()
        }

        return checksums.lines()
            .map { it.split(" +".toRegex()) }
            .filter { it.size == 2 }
            .map { it[0] to it[1] }
            .first { it.second == fileName }
            .first
    }

    private fun Path.digest(): String {
        val digest = MessageDigest.getInstance("SHA-256")

        toFile().inputStream().use {
            val buffer = ByteArray(1024)
            var read = it.read(buffer)
            while (read > -1) {
                digest.update(buffer, 0, read)
                read = it.read(buffer)
            }
        }

        fun ByteArray.convertToHex() = joinToString("") { "%02x".format(it) }

        return digest.digest().convertToHex()
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
