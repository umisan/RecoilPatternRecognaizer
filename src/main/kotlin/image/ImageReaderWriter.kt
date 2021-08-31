package image

import org.bytedeco.opencv.opencv_core.Mat
import org.bytedeco.opencv.global.opencv_imgcodecs.imread
import org.bytedeco.opencv.global.opencv_imgcodecs.imwrite

class ImageReaderWriter {
    fun read(path: String): Mat {
        return imread(path)
    }

    fun write(path: String, image: Mat) {
        imwrite(path, image)
    }
}