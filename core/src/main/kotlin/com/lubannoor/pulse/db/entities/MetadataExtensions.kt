package com.lubannoor.pulse.db.entities

import com.music.zyren.metadata.cleaner.MetadataCleaner
import com.music.zyren.metadata.models.CleanedMetadata

val SongEntity.cleanedMetadata: CleanedMetadata
  get() = MetadataCleaner.clean(title, "")

val Song.cleanedMetadata: CleanedMetadata
  get() = MetadataCleaner.clean(title, artists.firstOrNull()?.name.orEmpty())

val Song.displayTitle: String
  get() = cleanedMetadata.toDisplayTitle()

val Song.cleanArtistName: String
  get() = cleanedMetadata.primaryArtist
