package com.jinny.plancast.presentation.transfer


import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.jinny.plancast.databinding.ActivityWebviewBinding


class TransferActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebviewBinding
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ✨ 3. 바인딩 객체를 초기화하고 화면을 설정합니다.
        binding = ActivityWebviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ✨ 4. 'binding.webView'를 통해 뷰에 직접 접근합니다.
        binding.webView.apply {
            // 웹뷰 클라이언트 설정
            webViewClient = WebViewClient()

            // 자바스크립트 실행 허용
            settings.javaScriptEnabled = true

            // 웹과 통신할 인터페이스 추가
            addJavascriptInterface(WebAppInterface(), "Android")

            // 디버깅 활성화
            if (applicationInfo.flags and android.content.pm.ApplicationInfo.FLAG_DEBUGGABLE != 0) {
                WebView.setWebContentsDebuggingEnabled(true)
            }
        }

        // 웹페이지 로드
//        binding.webView.loadUrl("file:///android_asset/index.html")
        binding.webView.loadUrl("https://www.kma.go.kr/m/index.jsp")

        // 뒤로 가기 콜백 설정
        setupOnBackPressed()
    }

    /**
     * 웹에서 호출할 함수들을 정의하는 클래스
     */
    inner class WebAppInterface {
        @JavascriptInterface
        fun showToast(message: String) {
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 뒤로 가기 버튼 로직을 처리하는 함수
     */
    private fun setupOnBackPressed() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // ✨ 'binding.webView'를 사용하여 뒤로 갈 수 있는지 확인합니다.
                if (binding.webView.canGoBack()) {
                    binding.webView.goBack()
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
    }
}

