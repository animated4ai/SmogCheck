package com.smogcheck.app.data

import androidx.compose.ui.graphics.Color

data class Tip(val emoji: String, val text: String)

data class SeverityLevel(
    val max: Int,
    val en: String,
    val ur: String,
    val color: Color,
    val tips: List<Tip>
)

/** Shown when the photo is too dark for a trustworthy reading (verbatim from v3). */
val DARK_PHOTO_TIP = Tip(
    "🌙",
    "Tasveer bohat andheri hai — din ki roshni me dobara try karein."
)

/** Dedicated "Indoor Photo" state — no score shown (verbatim from v3). */
val INDOOR_COLOR = Color(0xFFA855F7)
const val INDOOR_EN = "Indoor Photo"
const val INDOOR_UR = "Kamre ke andar ki tasveer"
val INDOOR_TIPS = listOf(
    Tip(
        "🏠",
        "Ye kamre ke andar ki tasveer lag rahi hai — is me aasmaan nazar nahi aa raha, is liye smog ka andaza nahi lagaya ja sakta."
    ),
    Tip(
        "📷",
        "Bahar ja kar aasmaan ki taraf photo lein, phir Analyze dabayein."
    )
)

/** Severity bands, copied verbatim from SmogCheck-v3.html LEVELS (score < max). */
val SEVERITY_LEVELS = listOf(
    SeverityLevel(
        max = 20, en = "Clear Air", ur = "Hawa saaf hai",
        color = Color(0xFF22C55E),
        tips = listOf(
            Tip("😊", "Hawa saaf hai! Bahar sair aur exercise ke liye behtareen waqt."),
            Tip("🪟", "Khirkiyan khol kar taza hawa lein.")
        )
    ),
    SeverityLevel(
        max = 40, en = "Light Smog", ur = "Halki smog",
        color = Color(0xFFEAB308),
        tips = listOf(
            Tip("😷", "Hassas afraad (bache, buzurg) mask pehnein."),
            Tip("🏃", "Sakht exercise se parhez karein.")
        )
    ),
    SeverityLevel(
        max = 60, en = "Moderate Smog", ur = "Darmiyani smog",
        color = Color(0xFFF97316),
        tips = listOf(
            Tip("😷", "Bahar nikalte waqt mask zaroor pehnein."),
            Tip("🚗", "Gari ka sheesha band rakhein, AC recirculation par."),
            Tip("💧", "Pani zyada piyen, aankhein jalien to dho lein.")
        )
    ),
    SeverityLevel(
        max = 80, en = "Heavy Smog", ur = "Shadeed smog",
        color = Color(0xFFEF4444),
        tips = listOf(
            Tip("🏠", "Ghair zaroori bahar nikalne se parhez karein."),
            Tip("😷", "N95 mask istemal karein."),
            Tip("👶", "Bachon aur mareezon ko ghar par rakhein."),
            Tip("🪟", "Khirkiyan band rakhein.")
        )
    ),
    SeverityLevel(
        max = 101, en = "Severe Smog", ur = "Intehai khatarnaak smog",
        color = Color(0xFF991B1B),
        tips = listOf(
            Tip("🚨", "Bahar nikalna khatarnaak — ghar par rahein."),
            Tip("😷", "N95 mask ke baghair bahar na niklein."),
            Tip("🏥", "Saans me dushwari ho to foran doctor se rujoo karein."),
            Tip("📢", "AQI updates ke liye news dekhein.")
        )
    )
)

fun levelFor(score: Int): SeverityLevel = SEVERITY_LEVELS.first { score < it.max }
