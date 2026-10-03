package com.lubannoor.pulse.models

import com.music.innertube.models.YTItem
import com.lubannoor.pulse.db.entities.LocalItem

data class SimilarRecommendation(
  val title: LocalItem,
  val items: List<YTItem>,
)
