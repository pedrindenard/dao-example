package com.app.musicapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.app.musicapp.adapter.MusicAdapter
import com.app.musicapp.dao.DatabaseImpl
import com.app.musicapp.databinding.ActivityHomeBinding
import kotlinx.coroutines.flow.collectLatest

class HomeActivity : AppCompatActivity() {

    private val mainAdapter by lazy { MusicAdapter() }

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launchWhenStarted {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                setAdapter()
                setListener()
                setItemTouch()
                setData()
            }
        }
    }

    private fun setAdapter() {
        binding.musicRecyclerView.adapter = mainAdapter
    }

    private fun setListener() {
        binding.musicAdd.setOnClickListener {
            val intent = Intent(this, FormActivity::class.java)
            startActivity(intent)
        }

        mainAdapter.itemClickListener = { item ->
            val intent = Intent(this, AlterActivity::class.java)
            intent.putExtra("name", item.name)
            intent.putExtra("actor", item.actor)
            intent.putExtra("image", item.image)
            intent.putExtra("runtime", item.runtime)
            intent.putExtra("id", item.id)
            startActivity(intent)
        }
    }

    private fun setItemTouch() {
        val itemTouchDirection = ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        val itemTouch = object : ItemTouchHelper.SimpleCallback(0, itemTouchDirection) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.absoluteAdapterPosition
                removeMusicFromDatabase(position)
                mainAdapter.removeItem(position)
            }

            override fun onMove(
                view: RecyclerView, holder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder
            ): Boolean = false

        }

        ItemTouchHelper(itemTouch).attachToRecyclerView(binding.musicRecyclerView)
    }

    private suspend fun setData() {
        DatabaseImpl.getInstance(context = this@HomeActivity).apply {
            musicDao().getAll().collectLatest { result ->
                mainAdapter.insertItems(newList = result?.map { it.music } ?: emptyList())
            }
        }
    }

    private fun removeMusicFromDatabase(position: Int) {
        lifecycleScope.launchWhenStarted {
            DatabaseImpl.getInstance(context = this@HomeActivity).apply {
                musicDao().delete(mainAdapter.items[position].id)
            }
        }
    }
}