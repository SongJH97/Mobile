package com.example.mobileprogramingproject_7

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.widget.TextView
import com.example.mobileprogramingproject_7.databinding.RevRowBinding


class RevAdapter(val reviews : ArrayList<DataReview>) : RecyclerView.Adapter<RevAdapter.ViewHolder>() {


    inner class ViewHolder(val binding: RevRowBinding) : RecyclerView.ViewHolder(binding.root) {
        val id: TextView = binding.revId
        val review: TextView = binding.revStr
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RevAdapter.ViewHolder {
        val binding = RevRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.id.text = reviews[position].userID
        holder.review.text = reviews[position].review
        Log.d("jisu", "${holder.id}, ${holder.review}")
    }

    override fun getItemCount(): Int {
        return reviews.size
    }

}