package com.ishanvohra.musicbox.ui.activities

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ishanvohra.musicbox.models.GetPlaylistResponse
import kotlinx.coroutines.*
import androidx.lifecycle.viewModelScope

class MainViewModel : ViewModel() {

    val seriesList = MutableLiveData<GetPlaylistResponse>()

    //using view model scope which is a coroutine scope tied to this view model.
    //This scope will be canceled when ViewModel will be cleared, i.e ViewModel.onCleared is called.
    fun getPlaylist(url: String) =
        viewModelScope.launch {
            loadPlaylist(url)
        }

    //calling the get playlist function from MainRepository to get the list of songs to be played on home screen
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