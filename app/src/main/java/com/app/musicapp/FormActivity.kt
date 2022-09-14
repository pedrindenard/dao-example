package com.app.musicapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.app.musicapp.dao.DatabaseImpl
import com.app.musicapp.databinding.ActivityFormBinding
import com.app.musicapp.entity.MusicEntity
import com.app.musicapp.util.Utils.toEditable
import kotlinx.coroutines.launch

class FormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFormBinding

    private var name: String = ""
    private var actor: String = ""
    private var runtime: String = ""
    private var image: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launchWhenStarted {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                binding.musicFormAdd.text = getString(R.string.form_add)
                setListener()
                setWatchers()
                setData()
            }
        }
    }

    private fun setData() {
        binding.musicFormNameEditText.text = name.toEditable()
        binding.musicFormActorEditText.text = actor.toEditable()
        binding.musicFormRuntimeEditText.text = runtime.toEditable()
        binding.musicFormPosterEditText.text = image.toEditable()
    }

    private fun setWatchers() {
        binding.musicFormNameEditText.doAfterTextChanged { name = it.toString() }
        binding.musicFormActorEditText.doAfterTextChanged { actor = it.toString() }
        binding.musicFormRuntimeEditText.doAfterTextChanged { runtime = it.toString() }
        binding.musicFormPosterEditText.doAfterTextChanged { image = it.toString() }
    }

    private fun setListener() {
        binding.musicFormAdd.setOnClickListener {
            if (name.isBlank() || actor.isBlank() || runtime.isBlank()) {

                binding.musicFormNameEditText.error = getString(R.string.title_empty)
                binding.musicFormActorEditText.error = getString(R.string.actor_empty)
                binding.musicFormRuntimeEditText.error = getString(R.string.runtime_empty)

                Toast.makeText(this, getString(R.string.message_error), Toast.LENGTH_SHORT).show()

                return@setOnClickListener
            }

            val music = MusicEntity(
                name = name,
                actor = actor,
                runtime = runtime,
                image = image,
                id = 0
            )

            setMusic(music)
        }
    }

    private fun setMusic(music: MusicEntity) {
        lifecycleScope.launch {
            DatabaseImpl.getInstance(context = this@FormActivity).apply {
                musicDao().insert(music)
                finish()
            }
        }
    }
}