package com.wcsm.healthyfinance.data.repository

import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class NetworkRepository(private val connectivityManager: ConnectivityManager) {

    fun isConnected(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

}