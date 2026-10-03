package com.lubannoor.pulse.playback

import com.lubannoor.pulse.db.entities.SongEntity

interface ISyncUtils {
  fun likeSong(song: SongEntity)
}
