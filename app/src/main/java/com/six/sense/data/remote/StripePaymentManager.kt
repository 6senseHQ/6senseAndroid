package com.six.sense.data.remote

import android.content.Context
import androidx.compose.runtime.Composable
import com.six.sense.BuildConfig
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import com.stripe.android.paymentsheet.rememberPaymentSheet
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.http.isSuccess

class StripePaymentManager(
    private val context: Context,
    private val ktorClient: HttpClient
) {

    private var paymentSheet: PaymentSheet? = null
    private var customerConfig: PaymentSheet.CustomerConfiguration? = null
    private var paymentIntentClientSecret: String = BuildConfig.STRIPE_CLIENT_SECRET

    @Composable
    fun CreatePaymentSheet() {
        paymentSheet = rememberPaymentSheet(::onPaymentSheetResult)
    }

    suspend fun connectToStripeBackend() {
        val response = ktorClient.submitForm(BuildConfig.STRIPE_URL)
        if (response.status.isSuccess()) {
            val responseBody = response.body<Map<String, String>>()
            paymentIntentClientSecret =
                responseBody["paymentIntent"] ?: throw Exception("Missing paymentIntent")
            customerConfig = PaymentSheet.CustomerConfiguration(
                id = responseBody["customer"] ?: throw Exception("Missing customer"),
                ephemeralKeySecret = responseBody["ephemeralKey"]
                    ?: throw Exception("Missing ephemeralKey")
            )
            val publishableKey =
                responseBody["publishableKey"] ?: throw Exception("Missing publishableKey")

            PaymentConfiguration.init(context, publishableKey)
        } else {
            throw Exception("Failed to fetch payment details: ${response.status}")
        }
    }

    private fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
        when (paymentSheetResult) {
            is PaymentSheetResult.Canceled -> {
                print("Canceled")
            }

            is PaymentSheetResult.Failed -> {
                print("Error: ${paymentSheetResult.error}")
            }

            is PaymentSheetResult.Completed -> {
                // Display for example, an order confirmation screen
                print("Completed")
            }
        }
    }


    fun presentPaymentSheet() {
        paymentSheet?.presentWithPaymentIntent(
            paymentIntentClientSecret,
            PaymentSheet.Configuration(
                merchantDisplayName = "6sense Technology",
                customer = customerConfig
            )
        )
    }


}