package image

import entity.Coordinate
import org.bytedeco.opencv.global.opencv_core.BORDER_DEFAULT
import org.bytedeco.opencv.global.opencv_core.cvRound
import org.bytedeco.opencv.global.opencv_imgproc.*
import org.bytedeco.opencv.opencv_core.Mat
import org.bytedeco.opencv.opencv_core.Size
import org.bytedeco.opencv.opencv_imgproc.Vec3fVector

class PointDetector {
    private val THRESHOLD = 30.0

    private val MIN_RADIUS = 1

    private val MAX_RADIUS = 50

    fun detect(binaryImage: Mat): List<Coordinate> {
        return detectCircle(gaussianBlur(binaryImage), THRESHOLD, MIN_RADIUS, MAX_RADIUS)
    }

    fun gaussianBlur(input: Mat): Mat {
        val smooth = Mat()
        val kernelSize = Size(5, 5)
        val sigma = 1.5
        GaussianBlur(input, smooth, kernelSize, sigma, sigma, BORDER_DEFAULT)
        return smooth
    }

    fun detectCircle(binaryImage: Mat, threshold: Double, minRadius: Int, maxRadius: Int): List<Coordinate> {
        val circleVector = Vec3fVector()
        HoughCircles(binaryImage, circleVector, HOUGH_GRADIENT, 1.0, 1.0, 300.0, threshold, minRadius, maxRadius)
        return toCoordinates(circleVector)
    }

    fun toCoordinates(circleVector: Vec3fVector): List<Coordinate> {
        val coordinates = ArrayList<Coordinate>()
        for (i in 0 until circleVector.size()) {
            val elm = circleVector.get(i)
            coordinates.add(Coordinate(cvRound(elm.get(0)), cvRound(elm.get(1))))
        }
        return coordinates
    }
}