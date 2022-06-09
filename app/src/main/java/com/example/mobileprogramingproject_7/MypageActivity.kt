package com.example.mobileprogramingproject_7

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.mobileprogramingproject_7.databinding.ActivityMypageBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.firestore.auth.User
import com.kakao.sdk.user.UserApiClient

// 마이페이지 액티비티 입니다. - 윤섭
class MypageActivity : AppCompatActivity() {
    lateinit var binding: ActivityMypageBinding
    val textarr = arrayListOf<String>("즐겨찾기", "나의 리뷰")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMypageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLayout()
        Log.i(ContentValues.TAG, "로그) MypageActivity Created with getListData() by userID")
    }

    private fun initLayout(){
        // 프로필 초기화
        UserApiClient.instance.me { user, error ->
            if(error != null){
                Toast.makeText(this, "사용자 정보 불러오기 실패", Toast.LENGTH_SHORT).show()
            }else if(user != null){
                val url = user.kakaoAccount?.profile?.profileImageUrl

                // Glide (이미지를 빠르고 효율적으로 불러올 수 있게 도와주는 안드로이드용 이미지 로딩 라이브러리) 사용
                Glide.with(this)
                    .load(url)
                    .into(binding.profile)

                binding.nickname.text = UserInfo.userNick
            }
        }

        // 어댑터 부착
        binding.viewpager.adapter = MypageAdapter(this)
        TabLayoutMediator(binding.tablayout, binding.viewpager){
            tab, position ->
            tab.text = textarr[position]
//            tab.setIcon(iconarr[position]) 아이콘 줄 거면 사용
        }.attach()

        // 로그아웃
        binding.logoutBtn.setOnClickListener {
            // 로그아웃 시 로그인 화면으로 이동
            UserApiClient.instance.unlink { error ->
                if (error != null) {
                    Toast.makeText(this, "Logout Error. SDK에서 토큰 삭제됨",Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }
                else {
                    Toast.makeText(this, "Logout Success. SDK에서 토큰 삭제됨",Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }
            }
        }
        Log.i(ContentValues.TAG, "로그) InitLayout Done in MypageActivity")

        // 버튼 이벤트
        binding.leftBtn.setOnClickListener {
            super.onBackPressed()
        }

    }
}