package com.smogcheck.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smogcheck.app.ui.theme.BgMint
import com.smogcheck.app.ui.theme.DeepOrange
import com.smogcheck.app.ui.theme.GreenButton
import com.smogcheck.app.ui.theme.OrangeButton
import com.smogcheck.app.ui.theme.SkyBlue
import com.smogcheck.app.ui.theme.TealPrimary

@Composable
fun GradientButton(
    text: String,
    brush: Brush,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        shape = RoundedCornerShape(22.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues(0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush, RoundedCornerShape(22.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
fun HomeScreen(onCamera: () -> Unit, onGallery: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgMint)
    ) {
        // Colorful gradient header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(listOf(TealPrimary, SkyBlue))
                )
                .padding(vertical = 40.dp, horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("🌫️", fontSize = 60.sp)
                Text(
                    "SmogCheck",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    "Tasveer se smog ka andaza — secondon me",
                    color = Color.White.copy(alpha = 0.92f),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(22.dp)
        ) {
            GradientButton(
                text = "📷  Camera",
                brush = Brush.horizontalGradient(listOf(OrangeButton, DeepOrange)),
                onClick = onCamera
            )
            GradientButton(
                text = "🖼️  Gallery",
                brush = Brush.horizontalGradient(listOf(GreenButton, TealPrimary)),
                onClick = onGallery
            )

            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        "Kaise kaam karta hai? 🤔",
                        fontWeight = FontWeight.Bold,
                        fontSize = 19.sp
                    )
                    Text("☁️  Aasman ki tasveer lein ya gallery se chunein", fontSize = 15.sp)
                    Text("🔍  Analyze dabayein — app rangon ka tajziya karega", fontSize = 15.sp)
                    Text("📊  Score, severity aur sehat ke mashwaray paayein", fontSize = 15.sp)
                    Text(
                        "📴  100% offline — koi internet nahi chahiye",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TealPrimary
                    )
                }
            }
        }
    }
}
