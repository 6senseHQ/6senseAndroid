package com.six.sense.presentation.screen.profile

import android.app.Activity
import android.content.Context
import android.util.Log
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.QueryProductDetailsParams
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * [GooglePlayBillingManager] is a class that manages interactions with the Google Play Billing Library.
 *
 * It handles setting up the billing client, querying for product details, and launching the billing flow for purchases.
 *
 * @param context The application context.
 */
class GooglePlayBillingManager(
    private val context: Context,
) {
    private lateinit var billingClient: BillingClient
    private val _productDetails = MutableStateFlow<List<ProductDetails>>(emptyList())
    val productDetails: StateFlow<List<ProductDetails>> = _productDetails


    private fun setupBillingClient() {
        if (::billingClient.isInitialized) return
        billingClient = BillingClient.newBuilder(context)
            .setListener { billingResult, purchases ->
                // Handle purchase updates
            }.enablePendingPurchases(
                PendingPurchasesParams.newBuilder()
                    .enableOneTimeProducts()
                    .build()
            ).build()

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    queryProducts()
                }
                Log.d("TAG", "onBillingSetupFinished: ${billingResult.responseCode} ")
            }

            override fun onBillingServiceDisconnected() {
                Log.d("TAG", "onBillingServiceDisconnected: ")
            }
        })
    }

    private fun queryProducts() {
        val queryProductDetailsParams = QueryProductDetailsParams.newBuilder()
            .setProductList(
                listOf(
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId("test_premium")
                        .setProductType(BillingClient.ProductType.INAPP)
                        .build()
                )
            )
            .build()

        billingClient.queryProductDetailsAsync(queryProductDetailsParams) { billingResult, productDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                _productDetails.value = productDetailsList
            }
        }
    }

    fun launchPurchaseFlow(activity: Activity) {
        setupBillingClient()
        val productDetailsParamsList = listOf(
            BillingFlowParams.ProductDetailsParams.newBuilder()
                .apply { productDetails.value.firstOrNull()?.let { setProductDetails(it) } }
                .build()
        )

        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productDetailsParamsList)
            .build()

        billingClient.launchBillingFlow(activity, billingFlowParams)
    }
}
