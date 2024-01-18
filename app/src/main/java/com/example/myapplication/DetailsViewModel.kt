package com.example.myapplication

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.repository.RickMortyRepository
import com.example.myapplication.repository.model.RickMortyDetails
import com.example.myapplication.state.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailsViewModel: ViewModel() {

    private val rickMortyRepository = RickMortyRepository()
    private val mutableRickMortyDetailsData = MutableLiveData<UiState<RickMortyDetails>>()
    val immutableRickMortyDetailsData: LiveData<UiState<RickMortyDetails>> = mutableRickMortyDetailsData

    fun getData(id: String) {
        mutableRickMortyDetailsData.postValue(UiState(isLoading = true))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val req = rickMortyRepository.getRickMortyDetailsResponse(id)
                Log.d("DetailsViewModel", "request code -> ${req.code()}")

                if (req.isSuccessful){
                    val rickMortyDetails = req.body()
                    val firstEpisodeId = rickMortyDetails?.episode?.get(0)?.filter { it.isDigit() }
                    val firstEpisodeReq =  rickMortyRepository.getRickMortyDetailsEpisodeResponse(firstEpisodeId!!)
                    if(firstEpisodeReq.isSuccessful) {
                        val rickMortyDetailsEpisode = firstEpisodeReq.body()
                        if (rickMortyDetailsEpisode != null) {
                            rickMortyDetails.first = rickMortyDetailsEpisode
                        }
                    }

                    mutableRickMortyDetailsData.postValue(UiState(data = rickMortyDetails))
                    Log.d("DetailsViewModelEpisode", "request code -> ${req.code()}")
                } else {
                    mutableRickMortyDetailsData.postValue(UiState(error = "Exc: ${req.code()}"))
                    Log.d("DetailsViewModel", "request code -> ${req.code()}")
                }
            } catch (ex: Exception) {
                mutableRickMortyDetailsData.postValue(UiState(error = "Exc: $ex"))
            }
        }
    }
}