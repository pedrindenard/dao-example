package com.app.musicapp.model

import java.io.Serializable

data class Music(
    val id: Int,
    val name: String,
    val actor: String,
    val runtime: String,
    val image: String
) : Serializable