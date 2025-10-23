package com.jinny.plancast.presentation.financial.payment

import PaymentScreen
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast

import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.wallet.AutoResolveHelper
import com.google.android.gms.wallet.IsReadyToPayRequest
import com.google.android.gms.wallet.PaymentData
import com.google.android.gms.wallet.PaymentDataRequest
import com.google.android.gms.wallet.PaymentsClient
import com.google.android.gms.wallet.Wallet
import com.google.android.gms.wallet.WalletConstants
import com.jinny.plancast.data.model.Product
import com.jinny.plancast.presentation.BaseActivity
import com.jinny.plancast.presentation.todo.detail.DetailActivity.Companion.DETAIL_MODE_KEY
import com.jinny.plancast.presentation.todo.detail.DetailActivity.Companion.TODO_ID_KEY
import org.json.JSONArray
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class PaymentActivity : BaseActivity<PaymentViewModel>() {

    override val viewModel: PaymentViewModel by viewModel()
    private lateinit var paymentsClient: PaymentsClient

    // 결제 데이터 로딩을 위한 요청 코드
    private val LOAD_PAYMENT_DATA_REQUEST_CODE = 991


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val walletOptions = Wallet.WalletOptions.Builder()
            .setEnvironment(WalletConstants.ENVIRONMENT_TEST) // ❗ 프로덕션에서는 ENVIRONMENT_PRODUCTION 으로 변경
            .build()
        paymentsClient = Wallet.getPaymentsClient(this, walletOptions)


        setContent {
            MaterialTheme {
                val uiState by viewModel.uiState.collectAsState()
                val showDialog by viewModel.showDialog.collectAsState()

                PaymentScreen(
                    viewModel = viewModel,
                    state = uiState,
                    onPayClick = {
                        checkIsReadyToPay() },
                    onMethodChangeClick = {  },
                    onDismissRequest = { },
                    onMethodSelected = {
                    },
                    showPaymentMethodDialog = showDialog
                )

            }

        }

    }

    override fun observeData() {
        // do op
    }

    /**
     * 사용자의 기기가 Google Pay를 사용할 수 있는지 확인하고 버튼을 표시합니다.
     */
    private fun checkIsReadyToPay() {
        val isReadyToPayRequest = IsReadyToPayRequest.fromJson(googlePayBaseConfiguration.toString())

        paymentsClient.isReadyToPay(isReadyToPayRequest).addOnCompleteListener { task ->
            try {
                if (task.isSuccessful) {
                    // Google Pay 사용 가능, 버튼 표시
//                    googlePayButton.visibility = View.VISIBLE
                    Log.e("PaymentACtivity", "google pay is available")
                    requestPayment()
                } else {
                    // 사용 불가능, 사용자에게 알림
                    Toast.makeText(this, "이 기기에서는 Google Pay를 사용할 수 없습니다.", Toast.LENGTH_LONG).show()
                }
            } catch (e: ApiException) {
                Log.e("isReadyToPay failed", e.toString())
            }
        }
    }

    /**
     * 결제 요청을 시작합니다.
     */
    private fun requestPayment() {

        // 결제 금액, 가맹점 정보 등이 포함된 결제 데이터 요청 생성
        val paymentDataRequestJson = getGooglePayPaymentDataRequestJson()
        if (paymentDataRequestJson == null) {
            Log.e("RequestPayment", "Can't fetch payment data request")
            return
        }
        val request = PaymentDataRequest.fromJson(paymentDataRequestJson.toString())

        // Google Pay 결제 시트(UI)를 띄웁니다.
        AutoResolveHelper.resolveTask(
            paymentsClient.loadPaymentData(request),
            this,
            LOAD_PAYMENT_DATA_REQUEST_CODE
        )
    }


    /**
     * Google Pay 결제 시트로부터 결과를 받아 처리합니다.
     */
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LOAD_PAYMENT_DATA_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    data?.let { intent ->
                        val paymentData = PaymentData.getFromIntent(intent)
                        val paymentDataJson = paymentData?.toJson() ?: ""

                        // 5. 결제 토큰 추출 및 서버 전송
                        val paymentMethodData = JSONObject(paymentDataJson).getJSONObject("paymentMethodData")
                        val token = paymentMethodData.getJSONObject("tokenizationData").getString("token")

                        val product  = handlePaymentProduct(intent)
                        viewModel.createProduct(product)

                        // ❗❗ 중요: 이 토큰을 백엔드 서버로 보내 실제 결제를 완료해야 합니다.
                        processPaymentOnServer(token)

                        Toast.makeText(this, "결제가 성공적으로 접수되었습니다.", Toast.LENGTH_LONG).show()
                    }
                }
                Activity.RESULT_CANCELED -> {
                    // 사용자가 결제를 취소했습니다.
                }
                AutoResolveHelper.RESULT_ERROR -> {
                    val status = AutoResolveHelper.getStatusFromIntent(data)
                    Log.w("PaymentActivity", "Error code: ${status?.statusCode}")
                    Log.w("PaymentActivity", "Error code: ${status?.statusMessage}")
                }
            }
        }
    }

    /**
     * Google Pay 응답 Intent에서 Product 정보를 추출하여 세팅하는 함수
     */
    private fun handlePaymentProduct(data: Intent) : Product {
        val paymentData = PaymentData.getFromIntent(data)

        // 1. 결제 데이터가 있는지 확인
        if (paymentData != null) {

            // 2. 결제 토큰 (암호화된 데이터) 추출
            val paymentToken = paymentData.toJson()

            // 3. (가정) 결제 데이터에서 상품 ID와 같은 메타데이터를 추출
            //    실제로는 `paymentData` 내의 `paymentMethodData` JSON을 파싱하여 가져와야 합니다.
            //    여기서는 JSON 파싱이 복잡하므로, 단순히 더미 값을 설정하거나,
            //    실제 비즈니스 로직에 맞춰 백엔드 API를 호출하여 상품 정보를 가져와야 합니다.

            // ***************************************************************
            // 핵심 로직: Product 객체에 값 세팅
            // ***************************************************************

            // (A) - 서버 통신을 통해 상품 정보를 조회하는 것이 일반적
            // val orderId = extractOrderIdFromPaymentToken(paymentToken)
            // val productInfo = getProductInfoFromServer(orderId) // 이 함수는 백엔드 통신을 담당

            // (B) - 단순 예시를 위해 더미 값 또는 정적으로 저장된 값 사용
            val productId = null // 결제 요청 전에 미리 알고 있던 상품 ID
            val productName = "프리미엄 구독권"
            val productPrice = 50000 // 결제 금액과 일치해야 함

            val product = Product(
                id = productId,
                name = productName,
                price = productPrice
            )

            Log.i("GooglePay", "결제 완료된 상품: $product")

            return product;


        } else {
            Log.e("GooglePay", "PaymentData가 응답 데이터에서 null입니다.")
            return Product(name="", price=0)
        }
    }

    private fun processPaymentOnServer(paymentToken: String) {
        // 이 함수에서 백엔드 API를 호출하여 결제를 최종 완료합니다.
        // 예: Retrofit, Volley 등을 사용하여 서버에 paymentToken 전송
        viewModel.loadHelloMessage()
        Log.d("PaymentToken", "서버로 전송할 토큰: $paymentToken")

    }

    // --- 아래는 Google Pay 요청에 필요한 JSON 데이터를 생성하는 헬퍼 함수들입니다. ---

    // API 버전, 허용 결제 수단 등 기본 설정
    private val googlePayBaseConfiguration: JSONObject
        get() = JSONObject()
            .put("apiVersion", 2)
            .put("apiVersionMinor", 0)
            .put("allowedPaymentMethods", JSONArray().put(baseCardPaymentMethod))

    // 허용할 카드 종류, 인증 방식 등 카드 결제 설정
    private val baseCardPaymentMethod: JSONObject
        get() = JSONObject()
            .put("type", "CARD")
            .put("parameters", JSONObject()
                .put("allowedAuthMethods", JSONArray(listOf("PAN_ONLY", "CRYPTOGRAM_3DS")))
                .put("allowedCardNetworks", JSONArray(listOf("AMEX", "DISCOVER", "JCB", "MASTERCARD", "VISA"))))

    /**
     * 실제 결제 요청에 필요한 전체 JSON 객체를 생성합니다.
     * 거래 정보, 가맹점 정보, PG사 정보 등이 포함됩니다.
     */
    private fun getGooglePayPaymentDataRequestJson(): JSONObject? {
        val paymentDataRequest = googlePayBaseConfiguration
        paymentDataRequest.put("transactionInfo", JSONObject()
            .put("totalPriceStatus", "FINAL")
            .put("totalPrice", "1000.00") // ❗ 실제 결제할 금액
            .put("currencyCode", "KRW")) // ❗ 통화 코드

        paymentDataRequest.put("merchantInfo", JSONObject()
            .put("merchantId", "BCR2DN7T3HC2JKAU")
            .put("merchantName", "weatherapp")) // ❗ 가맹점 이름

        // ❗ PG사(Payment Gateway) 정보 설정 (매우 중요)
        // 사용하는 PG사(예: I'mport, Toss Payments, KG이니시스 등)의 가이드를 따라야 합니다.
        val tokenizationSpecification = JSONObject()
            .put("type", "PAYMENT_GATEWAY")
            .put("parameters", JSONObject()
                .put("gateway", "example") // PG사 이름 (예: 'iamport')
                .put("gatewayMerchantId", "exampleGatewayMerchantId")) // PG사에서 발급받은 ID

        val cardPaymentMethod = baseCardPaymentMethod
        cardPaymentMethod.put("tokenizationSpecification", tokenizationSpecification)

        paymentDataRequest.put("allowedPaymentMethods", JSONArray()
            .put(cardPaymentMethod))

        return paymentDataRequest
    }

}
