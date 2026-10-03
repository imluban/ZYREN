package com.lubannoor.pulse.ui.screens.equalizer

import com.lubannoor.pulse.eq.data.SavedEQProfile

data class EQState(
  val profiles: List<SavedEQProfile> = emptyList(),
  val activeProfileId: String? = null,
  val importStatus: String? = null,
  val error: String? = null
)
