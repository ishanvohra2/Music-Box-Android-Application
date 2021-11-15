package com.ishanvohra.musicbox.util

object ImageUrls {

    private val images =
        listOf<String>(
            "https://images.hdqwalls.com/download/little-prince-with-dog-colorful-science-fiction-fantasy-art-stars-artwork-4k-k7-1080x1920.jpg",
            "https://images.hdqwalls.com/download/cowboy-bebop-anime-animated-series-scifi-artwork-h0-1080x1920.jpg",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRrtL-gjZo2YdfuafJkQU1SS6bGLuEDBYY8ag&usqp=CAU",
            "https://images.hdqwalls.com/download/scifi-astronaut-space-mars-is-1080x1920.jpg",
            "https://singularityhub.com/wp-content/uploads/2019/01/The-Verge_Better-Worlds-1068x601.jpg"
        )

    fun getRandomImage() : String = images[(0..4).random()]
}