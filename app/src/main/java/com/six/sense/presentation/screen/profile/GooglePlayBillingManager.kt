package com.six.sense.presentation.screen.profile

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.*

class BillingManager(private val context: Context) {
    private val billingClient: BillingClient = BillingClient.newBuilder(context)
        .setListener { billingResult, purchases ->
            handlePurchaseUpdates(billingResult, purchases)
        }
        .enablePendingPurchases()
        .build()

    fun startConnection(onConnected: () -> Unit) {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    onConnected()
                }
            }

            override fun onBillingServiceDisconnected() {
                // Retry connection logic here
            }
        })
    }

    fun queryProducts(productIds: List<String>, onProductsFetched: (List<SkuDetails>) -> Unit) {
        val params = SkuDetailsParams.newBuilder()
            .setSkusList(productIds)
            .setType(BillingClient.SkuType.INAPP) // Use SkuType.SUBS for subscriptions
            .build()

        billingClient.querySkuDetailsAsync(params) { billingResult, skuDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && !skuDetailsList.isNullOrEmpty()) {
                onProductsFetched(skuDetailsList)
            }
        }
    }

    fun launchPurchaseFlow(activity: Activity, skuDetails: SkuDetails) {
        val billingFlowParams = BillingFlowParams.newBuilder()
            .setSkuDetails(skuDetails)
            .build()
        billingClient.launchBillingFlow(activity, billingFlowParams)
    }

    private fun handlePurchaseUpdates(billingResult: BillingResult, purchases: List<Purchase>?) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (purchase in purchases) {
                handlePurchase(purchase)
            }
        }
    }

    private fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged) {
            val acknowledgeParams = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken)
                .build()
            billingClient.acknowledgePurchase(acknowledgeParams) { billingResult ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    // Grant the entitlement
                }
            }
        }
    }
}
