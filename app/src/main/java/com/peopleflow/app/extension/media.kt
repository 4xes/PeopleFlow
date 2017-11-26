package mmd.kit.ui.extension

import android.media.MediaMetadataRetriever

fun MediaMetadataRetriever.int(key: Int): Int {
    return Integer.parseInt(extractMetadata(key))
}

fun MediaMetadataRetriever.long(key: Int): Long {
    return Integer.parseInt(extractMetadata(key)).toLong()
}

fun MediaMetadataRetriever.int(key: Int, fail: Int): Int {
    return try {
        Integer.parseInt(extractMetadata(key))
    } catch (e: Exception) { fail }
}

object Media {

    fun duration(filePath: String): Long {
        val retriever = MediaMetadataRetriever()
        return try {
            retriever.setDataSource(filePath)
            retriever.long(MediaMetadataRetriever.METADATA_KEY_DURATION)
        } catch (e: Exception) {
            throw e
        } finally {
            retriever.release()
        }
    }

    fun duration(filePath: String, fail: Long): Long {
        val retriever = MediaMetadataRetriever()
        return try {
            retriever.setDataSource(filePath)
            retriever.long(MediaMetadataRetriever.METADATA_KEY_DURATION)
        } catch (e: Exception) {
            return fail
        } finally {
            retriever.release()
        }
    }

}