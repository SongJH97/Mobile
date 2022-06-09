package com.example.mobileprogramingproject_7

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileprogramingproject_7.databinding.FavRowBinding
import com.google.firebase.firestore.SetOptions

//  FavFragment 에 부착하는 Adapter 입니다 - 윤섭
// UserID(여기서는 이메일 의미) 에 해당하는 DataFav 받아서 바인딩 해주는 역할
class MypageFavRecyclerAdapter(
    private val values:ArrayList<DataFav>
)
    : RecyclerView.Adapter<MypageFavRecyclerAdapter.ViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MypageFavRecyclerAdapter.ViewHolder {
        return ViewHolder(
            FavRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MypageFavRecyclerAdapter.ViewHolder, position: Int) {
        holder.favName.text = values[position].favCenterName
    }

    override fun getItemCount(): Int {
        return values.size
    }

    inner class ViewHolder(binding: FavRowBinding) : RecyclerView.ViewHolder(binding.root) {
        val favName: TextView = binding.favName

//        override fun toString(): String {
//            return super.toString() + " '" + contentView.text + "'"
//        }
    }

    fun removeItem(pos:Int){

        //db에서 삭제하기

            // 바꿀 값 set
//            val fav = hashMapOf(
//                "favorite" to false,
//                "centerType" to "",
//                "centerName" to values[pos].favCenterName
//            )
//            // 값 지정안하면 안 바꾸고 기존 값 유지할 수 있나 ?
//            if(values[pos].centerType.toString() == "Wheelchair")
//                fav.set("centerType", "Wheelchair")
//            else if(values[pos].centerType.toString() == "Center")
//                fav.set("centerType", "Center")

        val fav = hashMapOf("favorite" to false)

            // UserID 로 DB 접근하여 값 교체. favorite 이 false 됨.
            UserInfo.db.collection("Centers").document(values[pos].favCenterName)
                .collection("userID")
                .document(UserInfo.userID)
                .set(fav, SetOptions.merge())
                .addOnSuccessListener {
                    Log.d("logger", "로그) 리사이블러뷰에서 fav 삭제됨. Center fav bool FALSE")
                }
                .addOnFailureListener { e ->
                    Log.w("logger", "로그) 리사이클러뷰에서 fav 삭제 실패. Center DB", e)
                }

            UserInfo.db.collection("Users").document(UserInfo.userID)
                .collection("favorite")
                .document(values[pos].favCenterName)
                .delete()
                .addOnSuccessListener {
                    Log.d("logger", "로그) 리사이클러뷰에서 fav 삭제됨. User fav delete")
//                    Toast.makeText(applicationContext, "즐겨찾기에서 해제되었습니다",Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Log.w("logger", "로그) 리사이클러뷰에서 fav 삭제 실패. User DB", e)
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