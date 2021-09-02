import entity.Color
import entity.Rectangle
import image.BinarizationExecutor
import image.GunMarksDetector
import image.ImageReaderWriter
import org.bytedeco.opencv.global.opencv_imgproc.LINE_8

import org.bytedeco.opencv.global.opencv_imgproc.rectangle
import org.bytedeco.opencv.opencv_core.Point
import org.bytedeco.opencv.opencv_core.Scalar
import java.util.stream.Collectors

fun main(args: Array<String>) {
    val base = args[0]
    val name = args[1]
    val red =  args[2].toInt()
    val blue = args[3].toInt()
    val green = args[4].toInt()
    val path = base + name

    //画像の読み込み
    val imageReaderWriter = ImageReaderWriter()
    val inputImage = imageReaderWriter.read(path)

    //2値化
    val binarizationExecutor = BinarizationExecutor()
    val binaryImage = binarizationExecutor.toBinary(inputImage, Color(red, blue, green))
    imageReaderWriter.write(base + "tmp.png", binaryImage)


    //座標検出
    val gunMarksDetector = GunMarksDetector()
    val rectangles = gunMarksDetector.detect(binaryImage, 255)
    println(rectangles.size)
    println(rectangles.joinToString())

    //y軸でソート
    val sorted = rectangles.sortedWith(compareByDescending { it.bottomRight.y });
    println(sorted.joinToString())

    sorted.forEach { rect ->
        rectangle(
            inputImage,
            Point(rect.topLeft.x, rect.topLeft.y),
            Point(rect.bottomRight.x, rect.bottomRight.y),
            Scalar(255.0, 0.0, 0.0, 0.0),
            -1,
            LINE_8,
            0
        )
    }
    imageReaderWriter.write(base + "result.png", inputImage)

    if (sorted.isNotEmpty()) {
        val referenceRectangle = sorted[0]
        val referencePoint = entity.Point(
            (referenceRectangle.bottomRight.x - referenceRectangle.topLeft.x) / 2 + referenceRectangle.topLeft.x,
            (referenceRectangle.bottomRight.y - referenceRectangle.topLeft.y) / 2 + referenceRectangle.topLeft.y
        )
        val diff = sorted
            .stream()
            .map { rect -> Rectangle(
                entity.Point(rect.topLeft.x - referencePoint.x, -(rect.topLeft.y - referencePoint.y)),
                entity.Point(rect.bottomRight.x - referencePoint.x, -(rect.bottomRight.y - referencePoint.y))
            ) }
            .collect(Collectors.toList())
        println(
            diff.joinToString(
                separator = ",",
                prefix = "[",
                postfix = "]",
                transform = { it -> "(${it.topLeft.x},${it.topLeft.y},${it.bottomRight.x},${it.bottomRight.y})" })
        )
    }
}