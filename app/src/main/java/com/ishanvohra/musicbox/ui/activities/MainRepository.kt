package com.ishanvohra.musicbox.ui.activities

import com.ishanvohra.musicbox.networking.RetrofitClient

class MainRepository {

    private val baseUrl = "https://run.mocky.io/v3/"

    suspend fun getPlaylist(url: String) = RetrofitClient(baseUrl).instance.getPlayList(url)

}