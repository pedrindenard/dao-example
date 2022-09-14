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

class AlterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFormBinding

    private var name: String = ""
    private var actor: String = ""
    private var runtime: String = ""
    private var image: String = ""
    private var id: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getIntentData()

        lifecycleScope.launchWhenStarted {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                binding.musicFormAdd.text = getString(R.string.form_edit)
                setListener()
                setWatchers()
                setData()
            }
        }
    }

    private fun getIntentData() {
        name = intent.getSerializableExtra("name") as String
        actor = intent.getSerializableExtra("actor") as String
        runtime = intent.getSerializableExtra("runtime") as String
        image = intent.getSerializableExtra("image") as String
        id = intent.getSerializableExtra("id") as Int
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

                binding.musicFormNameEditText.error = "Título não pode ser vazio"
                binding.musicFormActorEditText.error = "Autor não pode ser vazio"
                binding.musicFormRuntimeEditText.error = "Duração não pode ser vazio"

                Toast.makeText(this, getString(R.string.message_error), Toast.LENGTH_SHORT).show()

                return@setOnClickListener
            }

            val music = MusicEntity(
                name = name,
                actor = actor,
                runtime = runtime,
                image = image,
                id = id
            )

            updateMusic(music)
        }
    }

    private fun updateMusic(music: MusicEntity) {
        lifecycleScope.launch {
            DatabaseImpl.getInstance(context = this@AlterActivity).apply {
                musicDao().update(music)
                finish()
            }
        }
    }
}