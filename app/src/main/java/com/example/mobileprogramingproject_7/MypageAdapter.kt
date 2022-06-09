package com.example.mobileprogramingproject_7

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

// 9주차 3차시 참고하여 마이페이지 내 슬라이드 탭바 구현 - 윤섭
class MypageAdapter(fragmentActivity: FragmentActivity)
    :FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> FavoriteFragment()
            1 -> ReviewFragment()
            else -> FavoriteFragment()
        }
    }
}