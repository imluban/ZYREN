package com.lubannoor.pulse.musicflow

import androidx.datastore.preferences.core.Preferences
import com.lubannoor.pulse.constants.CanvasThumbnailAnimationKey
import com.lubannoor.pulse.constants.DataSaverEnabledKey
import com.lubannoor.pulse.constants.MusicFlowAnimatedBackgroundKey
import com.lubannoor.pulse.constants.MusicFlowAutoNextKey
import com.lubannoor.pulse.constants.MusicFlowAutoplayKey
import com.lubannoor.pulse.constants.MusicFlowCellularPrefetchKey
import com.lubannoor.pulse.constants.MusicFlowEnabledKey
import com.lubannoor.pulse.constants.MusicFlowHideRecentKey
import com.lubannoor.pulse.constants.MusicFlowLyricsKey

data class MusicFlowPreferences(
  val enabled: Boolean = true,
  val autoplay: Boolean = true,
  val autoNext: Boolean = true,
  val animatedBackground: Boolean = false,
  val lyrics: Boolean = true,
  val cellularPrefetch: Boolean = true,
  val hideRecent: Boolean = true,
  val dataSaver: Boolean = false,
) {
  companion object {
    fun from(preferences: Preferences) =
      MusicFlowPreferences(
        enabled = preferences[MusicFlowEnabledKey] ?: true,
        autoplay = preferences[MusicFlowAutoplayKey] ?: true,
        autoNext = preferences[MusicFlowAutoNextKey] ?: true,
        animatedBackground =
          preferences[MusicFlowAnimatedBackgroundKey]
            ?: preferences[CanvasThumbnailAnimationKey]
            ?: false,
        lyrics = preferences[MusicFlowLyricsKey] ?: true,
        cellularPrefetch = preferences[MusicFlowCellularPrefetchKey] ?: true,
        hideRecent = preferences[MusicFlowHideRecentKey] ?: true,
        dataSaver = preferences[DataSaverEnabledKey] ?: false,
      )
  }
}
