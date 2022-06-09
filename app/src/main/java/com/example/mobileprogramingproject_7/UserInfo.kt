package com.example.mobileprogramingproject_7

import android.content.ContentValues
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object UserInfo {

//    val userID = "jisukelly9@naver.com"
    lateinit var userID:String // MypageActivity 에서 지정해줌 - 윤섭
    //나중에 카카오 API에서 가져오는걸로 바꿔야함
    lateinit var userNick:String

    val db = Firebase.firestore
    val userRVList = ArrayList<DataReview>()
    val userFavList = ArrayList<DataFav>()

    fun getListdata() {
        userRVList.clear()
        userFavList.clear()

        db.collection("Users").document(userID).collection("review")
            .get()
            .addOnSuccessListener {
                for(document in it){
                    val centerName = document.id
                    val type = document.getString("centerType")
                    val str = document.getString("reviewString")!!
//                    Log.i(ContentValues.TAG, "centerName : ${centerName}, type : ${type}, str : ${str}")
                    Log.i(ContentValues.TAG, "str : ${str}")
                    if(str!="") userRVList.add(DataReview(userID, str, centerName))
                }
                Log.i(ContentValues.TAG, "로그) ${UserInfo.userRVList.size} rev list size in UserInfo.getListData")

            }
            .addOnFailureListener {
                Log.d("logger","GET USER REVIEW DATA FAILED")
                it.printStackTrace()
            }

        db.collection("Users").document(userID).collection("favorite")
            .get()
            .addOnSuccessListener {
                for(document in it){
                    val centerName = document.id
                    val type = document.getString("centerType")!!
                    userFavList.add(DataFav(userID,centerName,type))
                }

                Log.i(ContentValues.TAG, "로그) ${UserInfo.userFavList.size} fav list size in UserInfo.getListData")

            }.addOnFailureListener {
                Log.d("logger","GET USER FAV DATA FAILED")
                it.printStackTrace()
            }
    }


    /* DB사용법 - https://firebase.google.com/docs/firestore/quickstart?hl=ko
val db = Firebase.firestore //종속돼있는 DB오픈
binding.apply {
    submitBtn.setOnClickListener {
        val user = hashMapOf( //저장할 데이터 정의
            "id" to userID.text.toString(),
            "pw" to userPW.text.toString()
        )
        db.collection("users").document(userID.text.toString()) //컬렉션에 데이터 추가
            .set(user)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "DocumentSnapshot successfully written")
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
            }
    }
}
db.collection("users") //users컬렉션 데이터 가져오기
.get()
.addOnSuccessListener { result ->
    for (document in result) {
        Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
    }
}
.addOnFailureListener { exception ->
    Log.w(ContentValues.TAG, "Error getting documents.", exception)
} */
}