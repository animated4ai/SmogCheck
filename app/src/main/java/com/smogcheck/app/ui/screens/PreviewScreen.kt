package com.smogcheck.app.ui.screens

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smogcheck.app.ui.theme.BgMint
import com.smogcheck.app.ui.theme.DeepPurple
import com.smogcheck.app.ui.theme.PurpleAnalyze
import com.smogcheck.app.ui.theme.SkyBlue
import com.smogcheck.app.ui.theme.TextDark

@Composable
fun PreviewScreen(
    bitmap: Bitmap,
    analyzing: Boolean,
    onAnalyze: () -> Unit,
    onRetake: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgMint)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "Tasveer tayyar hai ✅",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "Selected photo",
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(20.dp)),
            contentScale = ContentScale.Fit
        )
        GradientButton(
            text = if (analyzing) "⏳  Tajziya ho raha hai..." else "🔍  Smog Check Karein",
            brush = Brush.horizontalGradient(
                listOf(PurpleAnalyze, DeepPurple, SkyBlue)
            ),
            onClick = onAnalyze,
            enabled = !analyzing
        )
        if (analyzing) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
                Spacer(Modifier.height(6.dp))
                Text("Rangon ka tajziya ho raha hai...", fontSize = 14.sp)
            }
        }
        TextButton(
            onClick = onRetake,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("↩️  Doosri tasveer", fontSize = 17.sp)
        }
    }
}
