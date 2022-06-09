package com.example.mobileprogramingproject_7

import android.app.Application
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.mobileprogramingproject_7.databinding.ActivityLoginBinding
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.user.UserApiClient

// 로그인 창 띄울 로그인 액티비티 입니다. - 윤섭
class LoginActivity : AppCompatActivity() {
    private lateinit var binding:ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 로그인 정보 확인
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                Toast.makeText(this, "토큰 정보 보기 실패. 로그인 기록 없음", Toast.LENGTH_SHORT).show()
            }
            else if (tokenInfo != null) {
                Toast.makeText(this, "토큰 정보 보기 성공. 로그인 기록 있음", Toast.LENGTH_SHORT).show()

                // Login 성공시 UserInfo Update
                UserApiClient.instance.me { user, error ->
                    if(error != null){
                        Toast.makeText(this, "사용자 정보 불러오기 실패", Toast.LENGTH_SHORT).show()
                    }else if(user != null){
                        // UserInfo 설정
                        UserInfo.userID = user.kakaoAccount?.email.toString()
                        UserInfo.userNick = user.kakaoAccount?.profile?.nickname.toString()
                    }
                }

//                val intent = Intent(this, TestActivity::class.java)
                val intent = Intent(this, WheelchairMapActivity::class.java)
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                finish()
            }
        }
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                when {
                    error.toString() == AuthErrorCause.AccessDenied.toString() -> {
                        Toast.makeText(this, "접근이 거부 됨(동의 취소)", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidClient.toString() -> {
                        Toast.makeText(this, "유효하지 않은 앱", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidGrant.toString() -> {
                        Toast.makeText(this, "인증 수단이 유효하지 않아 인증할 수 없는 상태", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidRequest.toString() -> {
                        Toast.makeText(this, "요청 파라미터 오류", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidScope.toString() -> {
                        Toast.makeText(this, "유효하지 않은 scope ID", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.Misconfigured.toString() -> {
                        Toast.makeText(this, "설정이 올바르지 않음(android key hash)", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.ServerError.toString() -> {
                        Toast.makeText(this, "서버 내부 에러", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.Unauthorized.toString() -> {
                        Toast.makeText(this, "앱이 요청 권한이 없음", Toast.LENGTH_SHORT).show()
                    }
                    else -> { // Unknown
                        Toast.makeText(this, "기타 에러", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else if (token != null) {
                Toast.makeText(this, "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show()

                // Login 성공시 UserInfo Update
                UserApiClient.instance.me { user, error ->
                    if(error != null){
                        Toast.makeText(this, "사용자 정보 불러오기 실패", Toast.LENGTH_SHORT).show()
                    }else if(user != null){
                        // UserInfo 설정
                        UserInfo.userID = user.kakaoAccount?.email.toString()
                        UserInfo.userNick = user.kakaoAccount?.profile?.nickname.toString()
                    }
                }

//                val intent = Intent(this, TestActivity::class.java)

                val intent = Intent(this, WheelchairMapActivity::class.java)
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                finish()
            }
        }

        // 로그인 버튼 클릭시
        binding.loginBtn.setOnClickListener {
            // 카카오톡 있으면 앱 로그인 // 없으면 웹 로그인
            if(UserApiClient.instance.isKakaoTalkLoginAvailable(this)){
                UserApiClient.instance.loginWithKakaoTalk(this, callback = callback)
            }else{
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
        }

    }
}