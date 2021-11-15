package com.ishanvohra.musicbox.models

import java.io.Serializable

data class GetPlaylistResponse(
    val shorts: List<Short>
) {

    data class Short(
        val audioPath: String,
        val creator: Creator,
        val dateCreated: String,
        val shortID: String,
        val title: String
    ) : Serializable

    data class Creator(
        val email: String,
        val userID: String
    ) : Serializable
}