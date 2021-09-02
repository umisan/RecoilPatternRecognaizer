package image

import entity.Color
import entity.Rectangle
import org.bytedeco.javacpp.indexer.UByteRawIndexer
import org.bytedeco.opencv.opencv_core.Mat

class GunMarksDetector {
    fun detect(binaryInput: Mat, targetColor: Int ) : List<Rectangle> {
        val searcher = GridBreadthFirstSearcher(binaryInput.rows(), binaryInput.cols())
        val binaryInputIndexer = binaryInput.createIndexer<UByteRawIndexer>()
        //初期化
        for (x in 0 until binaryInput.cols()) {
            for (y in 0 until binaryInput.rows()) {
                val currentColor = binaryInputIndexer.get(y.toLong(), x.toLong())
                if (currentColor != targetColor) {
                    searcher.markNodeAsNotAvailable(x, y)
                }
            }
        }
        //BFS
        val result = ArrayList<Rectangle>()
        for (x in 0 until binaryInput.cols()) {
            for (y in 0 until binaryInput.rows()) {
                val tmp = searcher.startAt(x, y)
                tmp?.let { rectangle -> result.add(rectangle )}
            }
        }
        return result
    }
}