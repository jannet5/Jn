package com.jn.winremote.util

/**
 * Rotates a single-channel (e.g. YUV luminance) byte plane by a multiple of
 * 90 degrees clockwise. Pulled out as a pure function so the QR camera
 * analyzer's geometry math is unit-testable without CameraX/an Android
 * device: sensor output is usually landscape-native, so a portrait-held
 * phone needs this before handing the frame to a QR decoder.
 *
 * @return the rotated bytes plus the new (width, height).
 */
fun rotateLumaPlane(data: ByteArray, width: Int, height: Int, rotationDegrees: Int): Triple<ByteArray, Int, Int> {
    val normalized = ((rotationDegrees % 360) + 360) % 360
    if (normalized == 0) return Triple(data, width, height)
    require(data.size >= width * height) { "buffer smaller than width*height" }

    val out = ByteArray(width * height)
    return when (normalized) {
        90 -> {
            for (y in 0 until height) {
                for (x in 0 until width) {
                    out[x * height + (height - 1 - y)] = data[y * width + x]
                }
            }
            Triple(out, height, width)
        }
        180 -> {
            for (i in 0 until width * height) {
                out[width * height - 1 - i] = data[i]
            }
            Triple(out, width, height)
        }
        270 -> {
            for (y in 0 until height) {
                for (x in 0 until width) {
                    out[(width - 1 - x) * height + y] = data[y * width + x]
                }
            }
            Triple(out, height, width)
        }
        else -> Triple(data, width, height)
    }
}
