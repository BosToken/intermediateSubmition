package com.example.intermediatesubmition.ui.main

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.intermediatesubmition.R
import com.example.intermediatesubmition.data.api.response.ListStoryItem
import com.example.intermediatesubmition.databinding.ItemStoryBinding
import com.example.intermediatesubmition.ui.detail.DetailActivity
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class StoryAdapter(private val listStory: List<ListStoryItem>) : RecyclerView.Adapter<StoryAdapter.ListViewHolder>() {

    fun String.withDateFormat(): String {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        val date = format.parse(this) as Date
        return DateFormat.getDateInstance(DateFormat.FULL).format(date)
    }

    private lateinit var binding : ItemStoryBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_story, parent, false)
        return ListViewHolder(binding)
    }

    class ListViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        val imgPhoto: ImageView = binding.imageView
        val tvName: TextView = binding.textView
        val tvCreatedAt: TextView = binding.textView2
    }

    override fun getItemCount(): Int = listStory.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (photoUrl, createdAt, name, description, lon, id) = listStory[position]
        Glide.with(holder.itemView.context).load(photoUrl).into(holder.imgPhoto)
        holder.tvName.text = name
        holder.tvCreatedAt.text = createdAt.withDateFormat()

        holder.itemView.setOnClickListener {

            toDetail(holder, id)
        }
    }

    private fun toDetail(holder: ListViewHolder, id: String){
        val intent = Intent(holder.itemView.context, DetailActivity::class.java)
        intent.putExtra("id", id)
        holder.itemView.context.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(holder.itemView.context as Activity).toBundle())
    }
}