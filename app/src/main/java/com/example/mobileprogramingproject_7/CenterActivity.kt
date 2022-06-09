package com.example.mobileprogramingproject_7

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Toast
import com.example.mobileprogramingproject_7.UserInfo.db
import com.example.mobileprogramingproject_7.databinding.ActivityCenterBinding
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CenterActivity : AppCompatActivity() {
    lateinit var binding : ActivityCenterBinding
    lateinit var centerName : String

    private var changeLatitude = 0.0
    private var changeLongitude = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = ActivityCenterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        setDataLayout()
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
        binding.leftBtn.setOnClickListener {
            super.onBackPressed()
        }


        binding.reviewBtn.setOnClickListener {
            val rvintent = Intent(this, ReviewActivity::class.java)
            rvintent.putExtra("centerName", centerName)
            rvintent.putExtra("centerType", "Center")
            startActivity(rvintent)
        }
    }


    private fun setDataLayout() {
        //인텐트로 DataWheelchair객체 넘김
        //val data = intent.getSerializableExtra("center") as DataCenter
        for (data in DataManager.center) {
            if (data.tfcwkerMvmnCnterNm == intent.getStringExtra("name")) {
                centerName = data.tfcwkerMvmnCnterNm
                changeLatitude = data.latitude.toDouble()
                changeLongitude = data.longitude.toDouble()

                binding.apply {
                    TitleView.text = data.tfcwkerMvmnCnterNm
                    AddrView.text = data.rdnmadr
                    PhoneView.text = data.phoneNumber
                    trgetView.text = data.useTrget
                    chargeView.text = data.useCharge

                    var rtime = "평   일 "+data.weekdayRceptOpenHhmm+" ~ "+data.weekdayRceptColseHhmm+"\n"
                    rtime+="주  말 "+data.wkendRceptOpenHhmm+" ~ "+data.wkendRceptCloseHhmm
                    rcepttimeView.text = rtime

                    var timestr = "평  일 "+data.weekdayOperOpenHhmm+" ~ "+data.weekdayOperColseHhmm+"\n"
                    timestr+="주  말 "+data.wkendOperOpenHhmm+" ~ "+data.wkendOperCloseHhmm
                    timeView.text = timestr

                    var areastr = "관내 " +data.outsideOpratArea+"\n관외 "+data.insideOpratArea
                    areaView.text = areastr

                    callBtn.setOnClickListener {
                        val intentCall = Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+data.phoneNumber))
                        startActivity(intentCall)
                    }

                }
                break
            }
        }
    }


    fun addFav(){
        var fav = hashMapOf(
            "favorite" to true,
            "centerType" to "Center",
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
            "centerType" to "Center",
            "centerName" to centerName
        )

        db.collection("Users").document(UserInfo.userID)
            .collection("favorite")
            .document(centerName)
            .set(fav)
            .addOnSuccessListener {
                Toast.makeText(applicationContext, "즐겨찾기에 등록되었습니다",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.w("logger", "Error adding document", e)
            }
    }

    fun delFav(){
        val fav = hashMapOf(
            "favorite" to false,
            "centerType" to "Center",
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

                Toast.makeText(applicationContext, "즐겨찾기에서 해제되었습니다",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.w("logger", "Error adding document", e)
            }
    }



}