package com.pandora.userservice.utils

import org.apache.commons.io.IOUtils
import org.springframework.core.io.ResourceLoader
import java.io.IOException

@Throws(IOException::class)
fun readResourceIntoString(resourceLoader: ResourceLoader, file: String): String {
    val resource = resourceLoader.getResource(file)
    return IOUtils.toString(resource.inputStream, "UTF-8")
}
