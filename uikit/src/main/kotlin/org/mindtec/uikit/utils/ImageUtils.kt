package org.mindtec.uikit.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.core.graphics.scale
import androidx.exifinterface.media.ExifInterface
import java.io.File
import java.io.FileOutputStream

/**
 * Utilidades de imagen usadas por [org.mindtec.uikit.ui.components.ImagePickerComponent]
 * para optimizar fotos de cámara/galería antes de subirlas (resize +
 * respeto de orientación EXIF).
 */
object ImageUtils {

    fun optimizeImageForUpload(
        context: Context,
        imageUri: Uri,
        maxWidth: Int,
        maxHeight: Int,
        quality: Int
    ): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(imageUri)
            val originalBitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            val optimizedBitmap = resizeAndRotateBitmap(context, originalBitmap, imageUri, maxWidth, maxHeight)

            val tempFile = File(context.cacheDir, "uikit_optimized_image_${System.currentTimeMillis()}.jpg")
            val outputStream = FileOutputStream(tempFile)

            optimizedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            outputStream.close()
            optimizedBitmap.recycle()

            tempFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun createImageFile(context: Context, authority: String): Uri {
        return try {
            val imageFile = File(context.cacheDir, "uikit_temp_camera_image_${System.currentTimeMillis()}.jpg")
            FileProvider.getUriForFile(context, authority, imageFile)
        } catch (e: Exception) {
            val fallbackFile = File(context.getExternalFilesDir(null), "uikit_temp_camera_image_${System.currentTimeMillis()}.jpg")
            FileProvider.getUriForFile(context, authority, fallbackFile)
        }
    }

    private fun resizeAndRotateBitmap(
        context: Context,
        bitmap: Bitmap,
        imageUri: Uri,
        maxWidth: Int,
        maxHeight: Int
    ): Bitmap {
        val rotatedBitmap = applyExifRotation(context, bitmap, imageUri)
        return resizeBitmap(rotatedBitmap, maxWidth, maxHeight)
    }

    private fun resizeBitmap(bitmap: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
        val originalWidth = bitmap.width
        val originalHeight = bitmap.height

        val scale = minOf(
            maxWidth.toFloat() / originalWidth,
            maxHeight.toFloat() / originalHeight,
            1.0f
        )

        if (scale >= 1.0f) {
            return bitmap
        }

        val newWidth = (originalWidth * scale).toInt()
        val newHeight = (originalHeight * scale).toInt()

        return bitmap.scale(newWidth, newHeight)
    }

    private fun applyExifRotation(context: Context, bitmap: Bitmap, imageUri: Uri): Bitmap {
        return try {
            val inputStream = context.contentResolver.openInputStream(imageUri)
            val exif = ExifInterface(inputStream!!)
            inputStream.close()

            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )

            val rotation = when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90f
                ExifInterface.ORIENTATION_ROTATE_180 -> 180f
                ExifInterface.ORIENTATION_ROTATE_270 -> 270f
                else -> 0f
            }

            if (rotation != 0f) {
                val matrix = Matrix()
                matrix.postRotate(rotation)
                Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            } else {
                bitmap
            }
        } catch (e: Exception) {
            bitmap
        }
    }
}
