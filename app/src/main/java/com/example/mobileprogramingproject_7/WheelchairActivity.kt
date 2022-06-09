package com.example.mobileprogramingproject_7

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobileprogramingproject_7.UserInfo.db
import com.example.mobileprogramingproject_7.UserInfo.userID
import com.example.mobileprogramingproject_7.databinding.ActivityMainBinding
import com.example.mobileprogramingproject_7.databinding.ActivityWheelchairBinding
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class WheelchairActivity : AppCompatActivity() {
    lateinit var binding: ActivityWheelchairBinding
    lateinit var centerName: String

    private var changeLatitude = 0.0
    private var changeLongitude = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = ActivityWheelchairBinding.inflate(layoutInflater)
        setContentView(binding.root)
        UserInfo.getListdata()
        setDataLayout()
        supportActionBar?.hide()
//        setRecyclerView()
        setOnClick()

    }

    private fun setOnClick() {
        binding.apply {

            favBtn.setOnClickListener {

                val col = db.collection("Users")
                    .document(UserInfo.userID)
                    .collection("favorite")
                    .document(centerName)
                    .get()
                    .addOnSuccessListener {
                        if(it.get("centerType")==null){
                            addFav()
                        }else
                            delFav()
                    }

            }

            arriveBtn.setOnClickListener {
                var myLatitude = intent.getStringExtra("myLatitude")
                var myLongitude = intent.getStringExtra("myLongitude")


                val url : String ="kakaomap://route?sp=$myLatitude,$myLongitude&ep=$changeLatitude,$changeLongitude&by=FOOT"
                var intent =  Intent(Intent.ACTION_VIEW, Uri.parse(url))
                intent.addCategory(Intent.CATEGORY_BROWSABLE)
                var list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
                //startActivity(intent)
                if (list.equals("") && list.isEmpty()){
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=net.daum.android.map")))
                }else{
                    startActivity(intent)
                }

            }

        }
    }


    private fun setDataLayout() {
        //인텐트로 DataWheelchair객체 넘김
//        val data = intent.getSerializableExtra("wheelchair") as DataWheelchair


        for (data in DataManager.wheelchair) {
            if (data.fcltyNm == intent.getStringExtra("name")) {
                centerName = data.fcltyNm
                changeLatitude = data.latitude.toDouble()
                changeLongitude = data.longitude.toDouble()


                binding.apply {
                    TitleView.text = data.fcltyNm
                    AddrView.text = data.rdnmadr
                    PhoneView.text = data.institutionPhoneNumber
                    instlView.text =  data.instlLcDesc

                    var timestr = "평  일 "+data.weekdayOperOpenHhmm+" ~ "+data.weekdayOperColseHhmm+"\n"
                    timestr+="토요일 "+data.satOperOperOpenHhmm+" ~ "+data.satOperCloseHhmm+"\n"
                    timestr+="공휴일 "+data.holidayOperOpenHhmm+" ~ "+data.holidayCloseOpenHhmm

                    timeView.text = timestr

                    callBtn.setOnClickListener {
                        val intentCall = Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+data.institutionPhoneNumber))
                        startActivity(intentCall)
                    }
                }

                binding.reviewBtn.setOnClickListener {
                    val rvintent = Intent(this, ReviewActivity::class.java)
                    rvintent.putExtra("centerName", centerName)
                    rvintent.putExtra("centerType", "Wheelchair")
                    startActivity(rvintent)
                }
                binding.leftBtn.setOnClickListener {
                    super.onBackPressed()
                }

            }
        }



    }



    fun addFav(){
        var fav = hashMapOf(
            "favorite" to true,
            "centerType" to "Wheelchair",
            "centerName" to centerName
        )

        db.collection("Centers").document(centerName)
            .collection("userID")
            .document(UserInfo.userID)
            .set(fav, SetOptions.merge())
            .addOnSuccessListener {
                Log.d("logger", "review saved to center DB")
            }
            .addOnFailureListener { e ->
                Log.w("logger", "Error adding document", e)
            }

        fav = hashMapOf(
            "centerType" to "Wheelchair",
            "centerName" to centerName
        )

        db.collection("Users").document(UserInfo.userID)
            .collection("favorite")
            .document(centerName)
            .set(fav)
            .addOnSuccessListener {
                Toast.makeText(applicationContext, "즐겨찾기에 등록되었습니다", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.w("logger", "Error adding document", e)
            }
    }

    fun delFav(){
        val fav = hashMapOf(
            "favorite" to false,
            "centerType" to "Wheelchair",
            "centerName" to centerName
        )

        UserInfo.db.collection("Centers").document(centerName)
            .collection("userID")
            .document(UserInfo.userID)
            .set(fav, SetOptions.merge())
            .addOnSuccessListener {
            }
            .addOnFailureListener { e ->
                Log.w("logger", "Error adding document", e)
            }

        UserInfo.db.collection("Users").document(UserInfo.userID)
            .collection("favorite")
            .document(centerName)
            .delete()
            .addOnSuccessListener {

                Toast.makeText(applicationContext, "즐겨찾기에서 해제되었습니다", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.w("logger", "Error adding document", e)
            }
    }




}