package com.app.musicapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.app.musicapp.adapter.MusicAdapter
import com.app.musicapp.dao.DatabaseImpl
import com.app.musicapp.databinding.ActivityHomeBinding
import com.app.musicapp.enums.OnClickEvent
import com.app.musicapp.model.Music
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

        mainAdapter.itemClickListener = { position, event ->
            when (event) {
                OnClickEvent.MUSIC_INFORMATION -> {
                    navigateToActivityDetails(mainAdapter.items[position])
                }
                OnClickEvent.MUSIC_DELETE -> {
                    removeItemFromAdapter(position)
                }
            }
        }
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

    private fun navigateToActivityDetails(item: Music) {
        val intent = Intent(this, AlterActivity::class.java)
        intent.putExtra("name", item.name)
        intent.putExtra("actor", item.actor)
        intent.putExtra("image", item.image)
        intent.putExtra("runtime", item.runtime)
        intent.putExtra("id", item.id)
        startActivity(intent)
    }

    private fun removeItemFromAdapter(position: Int) {
        removeMusicFromDatabase(position)
        mainAdapter.removeItem(position)
    }
}