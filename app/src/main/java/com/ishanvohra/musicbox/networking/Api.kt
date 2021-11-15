package com.ishanvohra.musicbox.networking

import com.ishanvohra.musicbox.models.GetPlaylistResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface Api {

    @GET
    fun getPlayList(@Url url: String) : Response<GetPlaylistResponse>

}