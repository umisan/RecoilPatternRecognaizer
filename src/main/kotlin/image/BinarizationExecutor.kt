package image

import entity.Color
import org.bytedeco.javacpp.indexer.UByteRawIndexer
import org.bytedeco.opencv.global.opencv_core.CV_8UC1
import org.bytedeco.opencv.opencv_core.Mat

class BinarizationExecutor {
    fun toBinary(input: Mat, targetColor: Color) :Mat {
        val inputIndexer = input.createIndexer<UByteRawIndexer>()
        val dest = Mat(input.rows(), input.cols(), CV_8UC1)
        val destIndexer = dest.createIndexer<UByteRawIndexer>()

        for (y in 0 until dest.rows()) {
            for (x in 0 until dest.cols()) {
                val blue = inputIndexer.get(y.toLong(), x.toLong(), 0L)
                val green = inputIndexer.get(y.toLong(), x.toLong(), 1L)
                val red = inputIndexer.get(y.toLong(), x.toLong(), 2L)
                if (isTargetColor(targetColor, Color(red, green, blue))) {
                    destIndexer.put(y.toLong(), x.toLong(), 255)
                } else {
                    destIndexer.put(y.toLong(), x.toLong(), 0)
                }
            }
        }
        return dest
    }

    fun isTargetColor(color1: Color, color2: Color):Boolean {
        return color1.r == color2.r && color1.g == color2.g && color1.b == color2.b
    }
}