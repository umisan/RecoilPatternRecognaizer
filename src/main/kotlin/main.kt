import entity.Color
import entity.Coordinate
import image.BinarizationExecutor
import image.ImageReaderWriter
import image.PointDetector
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
    val pointDetector = PointDetector()
    val coordinates = pointDetector.detect(binaryImage)
    println(coordinates.size)

    //y軸でソート
    val sorted = coordinates.sortedWith(compareByDescending { it.y });
    println(sorted.joinToString())

    if (sorted.isNotEmpty()) {
        val referenceCoordinate = sorted[0]
        val diff = sorted
            .stream()
            .map { coordinate -> Coordinate(coordinate.x - referenceCoordinate.x, referenceCoordinate.y - coordinate.y) }
            .collect(Collectors.toList())
        println(diff.joinToString())
    }
}