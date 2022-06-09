package com.example.mobileprogramingproject_7

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.user.UserApiClient

// 마이페이지 내 리뷰 관련 데이터. 리사이클러뷰로
// onCreate 시 UserID(이메일) 카카오에서 받아서
// DB 탐색하여 해당 이메일과 같은 데이터의 리뷰 뽑아서 DataReview arraylist에 add
// 이후 리사이클러뷰 어댑터에 전달하여 부착
class FavoriteFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorite, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                val adapter = MypageFavRecyclerAdapter(UserInfo.userFavList) // arrayList 타입 생성자 생성해야함

                view.adapter = adapter

                val simpleItemTouchCallback = object: ItemTouchHelper.SimpleCallback(
                    ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT){
                    override fun onMove(
                        recyclerView: RecyclerView,
                        viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder
                    ): Boolean {
                        adapter.moveItem(viewHolder.adapterPosition, target.adapterPosition)
                        return true
                    }

                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        adapter.removeItem(viewHolder.adapterPosition)
                    }

                }

                val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
                itemTouchHelper.attachToRecyclerView(view)

            }

        }

        Log.i(ContentValues.TAG, "로그) FavoriteFragement Created with adapter")

        // Inflate the layout for this fragment
        return view
    }

}