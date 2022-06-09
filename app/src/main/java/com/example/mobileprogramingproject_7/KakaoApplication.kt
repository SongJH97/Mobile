package com.example.mobileprogramingproject_7

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

// SDK 초기화 위한 클래스 입니다. - 윤섭
class KakaoApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        // Kakao SDK 초기화
        KakaoSdk.init(this, "916252b1d687bf94d0ebf57bf3f59fcb") // 네이비트 앱 키
    }
}