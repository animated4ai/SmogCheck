package com.smogcheck.app

import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import com.smogcheck.app.analysis.SmogAnalyzer
import com.smogcheck.app.ui.screens.HomeScreen
import com.smogcheck.app.ui.screens.PreviewScreen
import com.smogcheck.app.ui.screens.ResultScreen
import com.smogcheck.app.util.ImageLoader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

sealed interface Screen {
    data object Home : Screen
    data class Preview(val bitmap: Bitmap) : Screen
    data class Result(
        val bitmap: Bitmap,
        val score: Int,
        val reliable: Boolean,
        val indoor: Boolean
    ) : Screen
}

@Composable
fun SmogCheckApp() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var screen by remember { mutableStateOf<Screen>(Screen.Home) }
    var analyzing by remember { mutableStateOf(false) }
    var pendingCameraUri by remember { mutableStateOf<Uri?>(null) }

    fun handleBitmap(bmp: Bitmap?) {
        if (bmp == null) {
            Toast.makeText(context, "Tasveer nahi mili", Toast.LENGTH_SHORT).show()
            return
        }
        screen = Screen.Preview(bmp)
    }

    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri == null) return@rememberLauncherForActivityResult
            scope.launch(Dispatchers.IO) {
                val bmp = ImageLoader.loadBitmap(context, uri)
                withContext(Dispatchers.Main) { handleBitmap(bmp) }
            }
        }

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (!success) return@rememberLauncherForActivityResult
            val uri = pendingCameraUri ?: return@rememberLauncherForActivityResult
            scope.launch(Dispatchers.IO) {
                val bmp = ImageLoader.loadBitmap(context, uri)
                withContext(Dispatchers.Main) { handleBitmap(bmp) }
            }
        }

    fun launchCamera() {
        try {
            val file = File(context.cacheDir, "smogcheck_${System.currentTimeMillis()}.jpg")
            val uri = FileProvider.getUriForFile(
                context, "${context.packageName}.fileprovider", file
            )
            pendingCameraUri = uri
            cameraLauncher.launch(uri)
        } catch (e: Exception) {
            Toast.makeText(
                context, "Camera nahi khul saka: ${e.message}", Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun runAnalysis(bmp: Bitmap) {
        if (analyzing) return
        analyzing = true
        scope.launch(Dispatchers.Default) {
            val result = SmogAnalyzer.analyze(bmp)
            withContext(Dispatchers.Main) {
                analyzing = false
                screen = Screen.Result(bmp, result.score, result.reliable, result.indoor)
            }
        }
    }

    when (val s = screen) {
        Screen.Home -> HomeScreen(
            onCamera = ::launchCamera,
            onGallery = { galleryLauncher.launch("image/*") }
        )

        is Screen.Preview -> PreviewScreen(
            bitmap = s.bitmap,
            analyzing = analyzing,
            onAnalyze = { runAnalysis(s.bitmap) },
            onRetake = {
                s.bitmap.recycle()
                screen = Screen.Home
            }
        )

        is Screen.Result -> ResultScreen(
            bitmap = s.bitmap,
            score = s.score,
            reliable = s.reliable,
            indoor = s.indoor,
            onNewPhoto = {
                s.bitmap.recycle()
                screen = Screen.Home
            }
        )
    }
}
