package com.lubannoor.pulse.ui.theme

import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.lubannoor.pulse.R

val bbhBartle = FontFamily(Font(R.font.bbh_bartle_regular, FontWeight.Normal))

/** Thin, widely-spaced geometric wordmark (Outfit Light) used for the "ZYREN" title on Home. */
@OptIn(ExperimentalTextApi::class)
val ZyrenWordmarkFontFamily =
  FontFamily(
    Font(
      resId = R.font.outfit,
      weight = FontWeight.Light,
      variationSettings = FontVariation.Settings(FontVariation.weight(300))
    )
  )
