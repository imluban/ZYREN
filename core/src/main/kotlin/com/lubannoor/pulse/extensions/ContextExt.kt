package com.lubannoor.pulse.extensions

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.music.innertube.utils.parseCookieString
import com.lubannoor.pulse.constants.InnerTubeCookieKey
import com.lubannoor.pulse.constants.YtmSyncKey
import com.lubannoor.pulse.utils.dataStore
import com.lubannoor.pulse.utils.get
import kotlinx.coroutines.runBlocking

fun Context.isSyncEnabled(): Boolean {
  return runBlocking { dataStore.get(YtmSyncKey, true) && isUserLoggedIn() }
}

fun Context.isUserLoggedIn(): Boolean {
  return runBlocking {
    val cookie = dataStore[InnerTubeCookieKey] ?: ""
    "SAPISID" in parseCookieString(cookie) && isInternetConnected()
  }
}

fun Context.isInternetConnected(): Boolean {
  val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
  val networkCapabilities =
    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
  return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
}
