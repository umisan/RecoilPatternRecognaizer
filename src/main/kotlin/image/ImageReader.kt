package image

import org.bytedeco.opencv.opencv_core.Mat
import org.bytedeco.opencv.global.opencv_imgcodecs.imread

class ImageReader {
    fun read(path: String): Mat {
        return imread(path)
    }
}