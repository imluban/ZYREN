package com.lubannoor.pulse.viewmodels

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import com.lubannoor.pulse.constants.ExportedSongIdsKey
import com.lubannoor.pulse.constants.HideExplicitKey
import com.lubannoor.pulse.constants.HideVideoSongsKey
import com.lubannoor.pulse.constants.SongSortDescendingKey
import com.lubannoor.pulse.constants.SongSortType
import com.lubannoor.pulse.constants.SongSortTypeKey
import com.lubannoor.pulse.db.MusicDatabase
import com.lubannoor.pulse.extensions.filterExplicit
import com.lubannoor.pulse.extensions.filterVideoSongs
import com.lubannoor.pulse.extensions.toEnum
import com.lubannoor.pulse.utils.SyncUtils
import com.lubannoor.pulse.utils.dataStore
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class AutoPlaylistViewModel
@Inject
constructor(
  @ApplicationContext context: Context,
  private val database: MusicDatabase,
  savedStateHandle: SavedStateHandle,
  private val syncUtils: SyncUtils,
) : ViewModel() {
  val playlist = savedStateHandle.get<String>("playlist")!!

  private val _isRefreshing = MutableStateFlow(false)
  val isRefreshing = _isRefreshing.asStateFlow()

  @OptIn(ExperimentalCoroutinesApi::class)
  val likedSongs =
    context.dataStore.data
      .map {
        Triple(
          Triple(
            (try {
                it[SongSortTypeKey]
              } catch (e: Exception) {
                null
              })
              .toEnum(SongSortType.CREATE_DATE) to
              ((try {
                it[SongSortDescendingKey]
              } catch (e: Exception) {
                null
              }) ?: true),
            (try {
              it[HideExplicitKey]
            } catch (e: Exception) {
              null
            }) ?: false,
            (try {
              it[HideVideoSongsKey]
            } catch (e: Exception) {
              null
            }) ?: false
          ),
          (try {
            it[ExportedSongIdsKey]
          } catch (e: Exception) {
            null
          }) ?: "",
          Unit
        )
      }
      .distinctUntilChanged()
      .flatMapLatest { (triple, exportedSongIds, _) ->
        val (sortDesc, hideExplicit, hideVideoSongs) = triple
        val (sortType, descending) = sortDesc
        when (playlist) {
          "liked" ->
            database.likedSongs(sortType, descending).map {
              it.filterExplicit(hideExplicit).filterVideoSongs(hideVideoSongs)
            }
          "downloaded" ->
            database.downloadedSongs(sortType, descending).map {
              it.filterExplicit(hideExplicit).filterVideoSongs(hideVideoSongs)
            }
          "uploaded" ->
            database.uploadedSongs(sortType, descending).map {
              it.filterExplicit(hideExplicit).filterVideoSongs(hideVideoSongs)
            }
          "exported" -> {
            val ids = exportedSongIds.split(",").filter { it.isNotBlank() }
            database.getSongsByIdsFlow(ids).map {
              it.filterExplicit(hideExplicit).filterVideoSongs(hideVideoSongs)
            }
          }
          else -> kotlinx.coroutines.flow.flowOf(emptyList())
        }
      }
      .stateIn(viewModelScope, kotlinx.coroutines.flow.SharingStarted.Lazily, emptyList())

  fun syncLikedSongs() {
    viewModelScope.launch(Dispatchers.IO) { syncUtils.syncLikedSongs() }
  }

  fun syncUploadedSongs() {
    viewModelScope.launch(Dispatchers.IO) { syncUtils.syncUploadedSongs() }
  }

  fun refresh() {
    viewModelScope.launch(Dispatchers.IO) {
      _isRefreshing.value = true
      when (playlist) {
        "liked" -> syncUtils.syncLikedSongsSuspend()
        "uploaded" -> syncUtils.syncUploadedSongsSuspend()
      }
      _isRefreshing.value = false
    }
  }
}
