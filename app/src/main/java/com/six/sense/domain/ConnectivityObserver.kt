package com.six.sense.domain

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import com.six.sense.utils.log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Observes network connectivity status and provides updates via a StateFlow.
 * @param context The context to access system services.
 *
 *
 * Usage:
 * - Access the current network status using `ConnectivityObserver.networkState`.
 * - Check if a network connection is available using `ConnectivityObserver.isNetworkAvailable`.
 * - Observe network status changes by collecting from the `networkStatus` StateFlow.
 */
class ConnectivityObserver(context: Context) {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val _networkStatus = MutableStateFlow(Status.Idle)
    /**
     * A StateFlow that emits the current network status.
     */
    val networkStatus = _networkStatus.asStateFlow()

    /**
     * Indicates whether a network connection is currently available.
     */
    var isNetworkAvailable = isConnected(context)
        private set

    /**
     * Represents the different network status states.
     *
     * @property message A human-readable message describing the status.
     */
    enum class Status(val message: String) {
        /**
         * Indicates that a network connection is available.
         */
        Available("Hurray! Network is back"),
        /**
         * Indicates that no network connection is available.
         */
        Unavailable("No Network Available"),
        /**
         * Indicates that the network connection is losing signal.
         */
        Losing("Poor Network Connection"),
        /**
         * Indicates that the network connection has been lost.
         */
        Lost("Lost Network Connection"),
        /**
         * Indicates that the network status is idle.
         */
        Idle("Idle")
    }

    init {
        _networkStatus.value = if (isNetworkAvailable) Status.Available else Status.Unavailable
        logStatus()

        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                _networkStatus.value = Status.Available
                isNetworkAvailable = true
                logStatus()
            }

            override fun onLosing(network: Network, maxMsToLive: Int) {
                super.onLosing(network, maxMsToLive)
                _networkStatus.value = Status.Losing
                logStatus()
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                _networkStatus.value = Status.Lost
                isNetworkAvailable = false
                logStatus()
            }

            override fun onUnavailable() {
                super.onUnavailable()
                _networkStatus.value = Status.Unavailable
                isNetworkAvailable = false
                logStatus()
            }
        }

        connectivityManager.registerDefaultNetworkCallback(callback)
    }

    private fun logStatus() {
        networkStatus.value.message.log("ConnectivityObserver")
    }


    private fun isConnected(context: Context): Boolean {
        var status = false
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        cm.run {
            getNetworkCapabilities(activeNetwork)?.run {
                status = hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ||
                        hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) ||
                        hasTransport(NetworkCapabilities.TRANSPORT_VPN)
            }
        }
        return status
    }

    /**
     * Checks if a network connection is available and throws an error if not.
     *
     * This function checks the device's network connectivity and throws an exception
     * with the message "No Internet Connection" if no network is available.
     *
     * @throws IllegalStateException If no network connection is available.
     */
    fun checkAndThrow4NoInternet() {
        if (!isNetworkAvailable)
            error(Status.Unavailable)
    }
}