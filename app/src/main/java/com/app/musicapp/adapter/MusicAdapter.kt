package com.app.musicapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.musicapp.R
import com.app.musicapp.databinding.ItemMusicBinding
import com.app.musicapp.enums.OnClickEvent
import com.app.musicapp.model.Music
import com.bumptech.glide.Glide

class MusicAdapter : RecyclerView.Adapter<MusicAdapter.ViewHolder>() {

    val items = arrayListOf<Music>()

    lateinit var itemClickListener: (position: Int, onClickEvent: OnClickEvent) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemMusicBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(private val binding: ItemMusicBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(music: Music) {
            binding.musicActor.text = music.actor
            binding.musicRuntime.text = music.runtime
            binding.musicName.text = music.name

            binding.root.setOnClickListener {
                itemClickListener.invoke(
                    absoluteAdapterPosition,
                    OnClickEvent.MUSIC_INFORMATION
                )
            }

            binding.musicRemove.setOnClickListener {
                itemClickListener.invoke(
                    absoluteAdapterPosition,
                    OnClickEvent.MUSIC_DELETE
                )
            }

            Glide.with(binding.root)
                .load(music.image)
                .error(R.drawable.ic_error)
                .placeholder(R.drawable.ic_error)
                .into(binding.musicImage)
        }
    }

    fun insertItems(newList: List<Music>) {
        val oldPositionRange = items.size
        val newPositionRange = newList.size

        items.clear()
        items.addAll(newList)

        notifyItemRangeInserted(oldPositionRange, newPositionRange)
    }

    fun cleanItems(newList: List<Music>) {
        val oldPositionRange = items.size
        val newPositionRange = newList.size

        items.clear()

        notifyItemRangeRemoved(oldPositionRange, newPositionRange)
    }

    fun removeItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }
}