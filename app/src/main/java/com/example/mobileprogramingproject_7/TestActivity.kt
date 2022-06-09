package com.example.mobileprogramingproject_7

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.example.mobileprogramingproject_7.databinding.ActivityTestBinding
import com.kakao.sdk.user.UserApiClient

// Login 페이지에서 넘어오는 임시 테스트 페이지 입니다. 추후 맵 표시하는 뷰로 넘어가게 변경 예정. - 윤섭
class TestActivity : AppCompatActivity() {
    lateinit var binding:ActivityTestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            // 로그아웃
            UserApiClient.instance.logout { error ->
                if (error != null) {
                    Toast.makeText(this, "로그아웃 실패. SDK에서 토큰 삭제됨",Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }
                else {
                    Toast.makeText(this, "로그아웃 성공. SDK에서 토큰 삭제됨",Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }
            }
        }

        binding.button2.setOnClickListener {
//            Handler().postDelayed({
//                UserInfo.getListdata()
//            },2000L)
            UserInfo.getListdata()
            Thread.sleep(1000L)
            val intent = Intent(this, MypageActivity::class.java)
            startActivity(intent)
        }

        binding.button3.setOnClickListener {
            val intent = Intent(this, WheelchairMapActivity::class.java)
            startActivity(intent)
        }

//        binding.button4.setOnClickListener {
//            val intent = Intent(this, CenterMapActivity::class.java)
//            startActivity(intent)
//        }

    }

}