package com.smogcheck.app.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.exifinterface.media.ExifInterface

object ImageLoader {
    /**
     * Decode [uri] to a bitmap capped at [maxDim] on its longest side,
     * applying EXIF rotation so camera photos display upright.
     */
    fun loadBitmap(context: Context, uri: Uri, maxDim: Int = 1600): Bitmap? {
        return try {
            val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            context.contentResolver.openInputStream(uri)?.use {
                BitmapFactory.decodeStream(it, null, bounds)
            }
            var sample = 1
            val biggest = maxOf(bounds.outWidth, bounds.outHeight)
            while (biggest / sample > maxDim) sample *= 2
            val opts = BitmapFactory.Options().apply { inSampleSize = sample }
            val bmp = context.contentResolver.openInputStream(uri)?.use {
                BitmapFactory.decodeStream(it, null, opts)
            } ?: return null
            applyExifRotation(context, uri, bmp)
        } catch (e: Exception) {
            null
        }
    }

    private fun applyExifRotation(context: Context, uri: Uri, bmp: Bitmap): Bitmap {
        return try {
            val exif = context.contentResolver.openInputStream(uri)?.use { ExifInterface(it) }
                ?: return bmp
            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL
            )
            val matrix = Matrix()
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
                ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
                ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.postScale(-1f, 1f)
                ExifInterface.ORIENTATION_FLIP_VERTICAL -> matrix.postScale(1f, -1f)
                else -> return bmp
            }
            val rotated = Bitmap.createBitmap(bmp, 0, 0, bmp.width, bmp.height, matrix, true)
            if (rotated != bmp) bmp.recycle()
            rotated
        } catch (e: Exception) {
            bmp
        }
    }
}
