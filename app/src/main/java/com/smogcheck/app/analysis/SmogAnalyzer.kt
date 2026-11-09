package com.smogcheck.app.analysis

import android.graphics.Bitmap
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

data class SmogResult(val score: Int, val reliable: Boolean, val indoor: Boolean = false)

object SmogAnalyzer {
    /**
     * Faithful port of analyze() from SmogCheck-v3.html (lines ~127-154).
     * Downscales so the max dimension is 480, then samples the upper 70%
     * of pixels (sky region) — every 4th pixel — accumulating blue
     * dominance, saturation and brightness.
     */
    fun analyze(src: Bitmap): SmogResult {
        // Downscale for speed: s = min(1, 480 / max(w,h))
        val maxDim = max(src.width, src.height)
        val s = min(1f, 480f / maxDim)
        val w = (src.width * s).roundToInt().coerceAtLeast(1)
        val h = (src.height * s).roundToInt().coerceAtLeast(1)
        val bmp = Bitmap.createScaledBitmap(src, w, h, true)
        val pixels = IntArray(w * h)
        bmp.getPixels(pixels, 0, w, 0, 0, w, h)

        var sB = 0.0
        var sS = 0.0
        var sBr = 0.0
        var sR = 0.0
        var sBc = 0.0
        var cnt = 0
        val yLimit = (h * 0.7).toInt() // JS: Math.floor(h * 0.7)
        val n = w * h

        var i = 0
        while (i < n) {
            // JS: if (Math.floor(i / w) > yLimit) continue — skip lower 30%
            if (i / w > yLimit) {
                i += 4
                continue
            }
            val p = pixels[i]
            val r = (p shr 16) and 0xff
            val g = (p shr 8) and 0xff
            val b = p and 0xff
            sR += r
            sBc += b
            sB += (b - r + b - g) / 2.0 // blue dominance (+ve = blue sky)
            val mx = max(max(r, g), b)
            val mn = min(min(r, g), b)
            sS += if (mx == 0) 0.0 else (mx - mn).toDouble() / mx // saturation
            sBr += (r + g + b) / 3.0 / 255.0 // brightness
            cnt++
            i += 4
        }
        if (cnt == 0) cnt = 1

        val avgB = sB / cnt
        val avgS = sS / cnt
        val avgBr = sBr / cnt
        val avgR = sR / cnt
        val avgBc = sBc / cnt

        // Indoor guard: warm indoor light (red >> blue) with decent brightness
        // and no blue dominance = room photo, no sky visible — score would be
        // garbage. No score is shown in this case.
        val indoor = (avgR - avgBc) > 30 && avgBr > 0.40 && avgB < 0
        if (indoor) return SmogResult(score = -1, reliable = false, indoor = true)

        var score = (55 - avgB * 0.55) * (0.55 + 0.9 * (1 - avgS))
        // Cloud guard: very bright + neutral = clouds, not smog
        if (avgBr > 0.82 && abs(avgB) < 18) score = min(score, 32.0)
        else if (avgBr > 0.78 && abs(avgB) < 12 && avgS < 0.08) score = min(score, 45.0)
        score = min(100.0, max(0.0, score))
        val reliable = avgBr > 0.18

        return SmogResult(score.roundToInt(), reliable)
    }
}
