package com.lubannoor.pulse.zyrencanvas

import com.lubannoor.pulse.canvas.CanvasArtwork

/**
 * Placeholder for a ZYREN-hosted canvas catalogue. The original project's canvas server was
 * removed, so this provider returns nothing and the other canvas providers (Apple, Tidal) are used.
 */
object echomusicCanvasProvider {
  suspend fun getBySongArtist(
    song: String,
    artist: String,
  ): CanvasArtwork? = null
}
