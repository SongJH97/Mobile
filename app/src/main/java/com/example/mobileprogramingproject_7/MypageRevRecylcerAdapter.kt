package com.example.mobileprogramingproject_7

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileprogramingproject_7.databinding.RevRowBinding
import com.google.firebase.firestore.SetOptions

// Review Fragment 에 부착하는 Adapter 입니다 - 윤섭
// UserID(여기서는 이메일 의미) 에 해당하는 DataReview 받아서 바인딩 해주는 역할
class MypageRevRecylcerAdapter(
    private val values:ArrayList<DataReview>
)
    : RecyclerView.Adapter<MypageRevRecylcerAdapter.ViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MypageRevRecylcerAdapter.ViewHolder {
            return ViewHolder(
                RevRowBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
    }

    override fun onBindViewHolder(holder: MypageRevRecylcerAdapter.ViewHolder, position: Int) {
//        holder.id.text = values[position].userID
        holder.id.text = values[position].centerName // 기존 이름 출력부분에 센터이름 출력
        holder.review.text = values[position].review
    }

    override fun getItemCount(): Int {
        return values.size
    }

    inner class ViewHolder(binding: RevRowBinding) : RecyclerView.ViewHolder(binding.root) {
//        val image: ImageView = binding.revPrfImgView
        val id:TextView = binding.revId
        val review:TextView = binding.revStr
    }

    fun removeItem(pos:Int){

        //db에서 삭제하기

        // review 값 공백
        val rev = hashMapOf("reviewString" to "")

        // db에 merge
        UserInfo.db.collection("Centers").document(values[pos].centerName)
            .collection("userID")
            .document(UserInfo.userID)
            .set(rev, SetOptions.merge())
            .addOnSuccessListener {
                Log.d("logger", "로그) 리사이블러뷰에서 rev 삭제됨. Center rev str NULL")
            }
            .addOnFailureListener { e ->
                Log.w("logger", "로그) 리사이클러뷰에서 rev 삭제 실패. Center DB", e)
            }

        UserInfo.db.collection("Users").document(UserInfo.userID)
            .collection("review")
            .document(values[pos].centerName)
            .delete()
            .addOnSuccessListener {
                Log.d("logger", "로그) 리사이클러뷰에서 rev 삭제됨. User rev delete")
//                    Toast.makeText(applicationContext, "즐겨찾기에서 해제되었습니다",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.w("logger", "로그) 리사이클러뷰에서 rev 삭제 실패. User DB", e)
            }

        values.removeAt(pos)
        notifyItemRemoved(pos)
    }

    fun moveItem(oldpos:Int, now:Int){
        val item = values[oldpos]
        values.removeAt(oldpos)
        values.add(now, item)
        notifyItemMoved(oldpos,now)
    }


}