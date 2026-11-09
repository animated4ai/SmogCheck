package com.smogcheck.app.ui.screens

import android.graphics.Bitmap
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smogcheck.app.data.DARK_PHOTO_TIP
import com.smogcheck.app.data.INDOOR_COLOR
import com.smogcheck.app.data.INDOOR_EN
import com.smogcheck.app.data.INDOOR_TIPS
import com.smogcheck.app.data.INDOOR_UR
import com.smogcheck.app.data.Tip
import com.smogcheck.app.data.levelFor
import com.smogcheck.app.ui.theme.BgMint
import com.smogcheck.app.ui.theme.DeepPurple
import com.smogcheck.app.ui.theme.PurpleAnalyze
import com.smogcheck.app.ui.theme.SkyBlue
import com.smogcheck.app.ui.theme.TextDark
import com.smogcheck.app.ui.theme.TextMuted
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ResultScreen(
    bitmap: Bitmap,
    score: Int,
    reliable: Boolean,
    indoor: Boolean,
    onNewPhoto: () -> Unit
) {
    val level = if (indoor) null else levelFor(score)
    val badgeColor = if (indoor) INDOOR_COLOR else level!!.color
    val gaugeTarget = if (indoor) 0f else score / 100f
    val animated by animateFloatAsState(
        targetValue = gaugeTarget,
        animationSpec = tween(durationMillis = 1200),
        label = "gauge"
    )
    val tips = buildList {
        if (indoor) {
            addAll(INDOOR_TIPS)
        } else {
            if (!reliable) add(DARK_PHOTO_TIP)
            addAll(level!!.tips)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BgMint)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Photo thumbnail + timestamp
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Analyzed photo",
                    modifier = Modifier
                        .size(88.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.width(14.dp))
                Column {
                    Text(
                        "Smog Report",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                    Text(
                        "Checked: " + SimpleDateFormat(
                            "dd MMM yyyy, hh:mm a",
                            Locale.getDefault()
                        ).format(Date()),
                        fontSize = 13.sp,
                        color = TextMuted
                    )
                }
            }
        }

        item {
            // Big score card with animated gauge
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        if (indoor) "—" else "$score",
                        fontSize = 72.sp,
                        fontWeight = FontWeight.Bold,
                        color = badgeColor
                    )
                    Text("/ 100", fontSize = 18.sp, color = TextMuted)
                    Spacer(Modifier.height(12.dp))
                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(26.dp)
                    ) {
                        val barW = size.width
                        val barH = size.height
                        // Track
                        drawRoundRect(
                            color = Color(0xFFE5E7EB),
                            size = Size(barW, barH),
                            cornerRadius = CornerRadius(barH / 2, barH / 2)
                        )
                        // Animated fill
                        val fillW = (barW * animated).coerceAtLeast(barH)
                        drawRoundRect(
                            color = badgeColor,
                            size = Size(fillW, barH),
                            cornerRadius = CornerRadius(barH / 2, barH / 2)
                        )
                    }
                }
            }
        }

        item {
            // Severity badge — English + Urdu, color coded
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(badgeColor, RoundedCornerShape(18.dp))
                    .padding(vertical = 16.dp, horizontal = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        if (indoor) INDOOR_EN else level!!.en,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        if (indoor) INDOOR_UR else level!!.ur,
                        fontSize = 18.sp,
                        color = Color.White.copy(alpha = 0.95f)
                    )
                }
            }
        }

        items(tips) { tip -> TipRow(tip) }

        item {
            Spacer(Modifier.height(4.dp))
            GradientButton(
                text = "📷  Nayi Tasveer",
                brush = Brush.horizontalGradient(
                    listOf(PurpleAnalyze, DeepPurple, SkyBlue)
                ),
                onClick = onNewPhoto
            )
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun TipRow(tip: Tip) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(tip.emoji, fontSize = 26.sp)
            Spacer(Modifier.width(12.dp))
            Text(tip.text, fontSize = 15.sp, color = TextDark)
        }
    }
}
