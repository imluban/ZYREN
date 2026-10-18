package com.music.zyren.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.lubannoor.pulse.core.repository.PlaylistRepositoryImpl
import com.lubannoor.pulse.core.repository.SongRepositoryImpl
import com.lubannoor.pulse.domain.repositories.PlaylistRepository
import com.lubannoor.pulse.domain.repositories.SongRepository
import com.lubannoor.pulse.musicflow.MusicFlowFeedSource
import com.lubannoor.pulse.musicflow.MusicFlowRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DomainBindingsModule {

  @Binds
  @Singleton
  abstract fun bindSongRepository(
    impl: SongRepositoryImpl
  ): SongRepository

  @Binds
  @Singleton
  abstract fun bindPlaylistRepository(
    impl: PlaylistRepositoryImpl
  ): PlaylistRepository

  @Binds
  @Singleton
  abstract fun bindMusicFlowFeedSource(
    impl: MusicFlowRepository
  ): MusicFlowFeedSource
}
