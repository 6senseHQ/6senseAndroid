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

/**
 * `StripePaymentManager` is a class responsible for managing the payment process
 * using Stripe's Payment Sheet. It handles communication with a backend server to fetch
 * payment details, initializes the Stripe SDK, and presents the Payment Sheet UI.
 *
 * @property context The application context.
 * @property ktorClient The Ktor HTTP client used to communicate with the backend server.
 */
class StripePaymentManager(
    private val context: Context,
    private val ktorClient: HttpClient
) {

    private var paymentSheet: PaymentSheet? = null
    private var customerConfig: PaymentSheet.CustomerConfiguration? = null
    private var paymentIntentClientSecret: String = BuildConfig.STRIPE_CLIENT_SECRET

    /**
     * Creates and initializes a PaymentSheet instance using the rememberPaymentSheet composable.
     *
     * This function is responsible for setting up a [PaymentSheet] that can be used to present
     * the Stripe payment UI to the user. It utilizes the [rememberPaymentSheet] composable
     * to handle the lifecycle and state management of the PaymentSheet.
     *
     * The [onPaymentSheetResult] callback is passed to [rememberPaymentSheet], allowing you to
     * handle the result of the payment process (e.g., success, cancellation, or failure).
     *
     * **Usage:**
     *
     * 1. Call this function within a composable scope to initialize the PaymentSheet.
     * 2. Use the `paymentSheet` variable (which is assigned within this function)
     *    to present the payment sheet later in your UI (e.g., in response to a button click).
     *
     * **Example:**
     *
     * ```kotlin
     * var paymentSheet: PaymentSheet? = null
     * @Composable
     * fun MyPaymentScreen() {
     *     CreatePaymentSheet()
     *
     *     Button(onClick = {
     *         paymentSheet?.presentWithPaymentIntent(...)
     *     }) {
     *         Text("Pay Now")
     *     }
     * }
     * ```
     *
     * **Note:**
     *
     * - Ensure that you have properly configured your Stripe account and API keys before using this function.
     * - The `onPaymentSheetResult` function needs to be defined elsewhere to handle the payment sheet's result.
     * - Remember to store the PaymentSheet instance in a variable outside of the composable scope (like a class variable), otherwise it will be reset on recomposition.
     * - This function must be called within a composable scope.
     *
     * @see rememberPaymentSheet
     * @see PaymentSheet
     */
    @Composable
    fun CreatePaymentSheet() {
        paymentSheet = rememberPaymentSheet(::onPaymentSheetResult)
    }

    /**
     * Establishes a connection with the Stripe backend to retrieve essential payment information.
     *
     * This function performs the following actions:
     * 1. Sends a request to the configured Stripe backend URL (defined in `BuildConfig.STRIPE_URL`).
     * 2. Parses the response from the server, expecting a JSON map containing:
     *    - `paymentIntent`: The client secret of a PaymentIntent.
     *    - `customer`: The ID of a customer.
     *    - `ephemeralKey`: The secret of an ephemeral key associated with the customer.
     *    - `publishableKey`: The Stripe publishable key.
     * 3. If the response is successful (HTTP status code 2xx), it extracts the necessary details:
     *    - Assigns the `paymentIntent` to the `paymentIntentClientSecret` property.
     *    - Creates a `PaymentSheet.CustomerConfiguration` object using the `customer` ID and `ephemeralKey`, and assign it to `customerConfig`.
     *    - Initializes the `PaymentConfiguration` using the `publishableKey`.
     * 4. If the response is not successful or if any of the expected fields are missing from the response, it throws an exception.
     *
     * @throws Exception If the connection to the Stripe backend fails, if the response is not successful,
     *                   or if any required data (`paymentIntent`, `customer`, `ephemeralKey`, `publishableKey`)
     *                   is missing from the response.
     *
     * @param ktorClient The Ktor HTTP client used to communicate with the Stripe backend.
     * @param context The application context, needed to initialize PaymentConfiguration
     * @property paymentIntentClientSecret A property to store paymentIntent secret, which will be retrieved from the response.
     * @property customerConfig A property to store customer configuration, which will be retrieved from the response.
     */
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