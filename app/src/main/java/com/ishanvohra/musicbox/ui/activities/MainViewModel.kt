package com.ishanvohra.musicbox.ui.activities

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ishanvohra.musicbox.models.GetPlaylistResponse
import kotlinx.coroutines.*
import androidx.lifecycle.viewModelScope

class MainViewModel : ViewModel() {

    val seriesList = MutableLiveData<GetPlaylistResponse>()

    fun getPlaylist(url: String) =
        viewModelScope.launch {
            loadPlaylist(url)
        }

    private suspend fun loadPlaylist(url: String){
        try{
            val response = MainRepository().getPlaylist(url)
            Log.d(javaClass.simpleName, "loadPebbleSeries call ${response.body()}}")
            if(response.isSuccessful){
                seriesList.postValue(response.body())
            }
        }
        catch (e: Exception){
            e.printStackTrace()
        }
    }

}