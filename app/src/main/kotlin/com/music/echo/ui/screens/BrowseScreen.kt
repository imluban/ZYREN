package com.lubannoor.pulse.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.music.innertube.models.AlbumItem
import com.music.innertube.models.ArtistItem
import com.music.innertube.models.PlaylistItem
import com.lubannoor.pulse.LocalPlayerAwareWindowInsets
import com.lubannoor.pulse.LocalPlayerConnection
import com.lubannoor.pulse.R
import com.lubannoor.pulse.constants.GridItemSize
import com.lubannoor.pulse.constants.GridItemsSizeKey
import com.lubannoor.pulse.constants.GridThumbnailHeight
import com.lubannoor.pulse.ui.component.IconButton
import com.lubannoor.pulse.ui.component.LocalMenuState
import com.lubannoor.pulse.ui.component.YouTubeGridItem
import com.lubannoor.pulse.ui.component.shimmer.GridItemPlaceHolder
import com.lubannoor.pulse.ui.component.shimmer.ShimmerHost
import com.lubannoor.pulse.ui.menu.YouTubeAlbumMenu
import com.lubannoor.pulse.ui.menu.YouTubeArtistMenu
import com.lubannoor.pulse.ui.menu.YouTubePlaylistMenu
import com.lubannoor.pulse.ui.utils.backToMain
import com.lubannoor.pulse.utils.rememberEnumPreference
import com.lubannoor.pulse.viewmodels.BrowseViewModel

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun BrowseScreen(
  navController: NavController,
  scrollBehavior: TopAppBarScrollBehavior,
  browseId: String?,
  viewModel: BrowseViewModel = hiltViewModel(),
) {
  val menuState = LocalMenuState.current
  val playerConnection = LocalPlayerConnection.current ?: return
  val isPlaying by playerConnection.isEffectivelyPlaying.collectAsState()
  val mediaMetadata by playerConnection.mediaMetadata.collectAsState()

  val title by viewModel.title.collectAsState()
  val items by viewModel.items.collectAsState()

  val coroutineScope = rememberCoroutineScope()
  val gridItemSize by rememberEnumPreference(GridItemsSizeKey, GridItemSize.BIG)

  LazyVerticalGrid(
    columns =
      GridCells.Adaptive(
        minSize = GridThumbnailHeight + if (gridItemSize == GridItemSize.BIG) 24.dp else (-24).dp
      ),
    contentPadding = LocalPlayerAwareWindowInsets.current.asPaddingValues()
  ) {
    items?.let { items ->
      items(items = items.distinctBy { it.id }, key = { it.id }) { item ->
        YouTubeGridItem(
          item = item,
          isPlaying = isPlaying,
          fillMaxWidth = true,
          coroutineScope = coroutineScope,
          modifier =
            Modifier.combinedClickable(
              onClick = {
                when (item) {
                  is AlbumItem -> navController.navigate("album/${item.id}")
                  is PlaylistItem -> navController.navigate("online_playlist/${item.id}")
                  is ArtistItem -> navController.navigate("artist/${item.id}")
                  else -> {}
                }
              },
              onLongClick = {
                menuState.show {
                  when (item) {
                    is AlbumItem ->
                      YouTubeAlbumMenu(
                        albumItem = item,
                        navController = navController,
                        onDismiss = menuState::dismiss
                      )
                    is PlaylistItem -> {
                      YouTubePlaylistMenu(
                        playlist = item,
                        coroutineScope = coroutineScope,
                        onDismiss = menuState::dismiss
                      )
                    }
                    is ArtistItem -> {
                      YouTubeArtistMenu(artist = item, onDismiss = menuState::dismiss)
                    }
                    else -> {}
                  }
                }
              }
            )
        )
      }

      if (items.isEmpty()) {
        items(8) { ShimmerHost { GridItemPlaceHolder(fillMaxWidth = true) } }
      }
    }
  }

  TopAppBar(
    title = { Text(title ?: "") },
    navigationIcon = {
      IconButton(onClick = navController::navigateUp, onLongClick = navController::backToMain) {
        Icon(painterResource(R.drawable.arrow_back), contentDescription = null)
      }
    }
  )
}
