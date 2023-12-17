package com.example.myapplication

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.repository.RickMortyRepository
import com.example.myapplication.repository.model.RickMorty
import com.example.myapplication.state.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class MainViewModel: ViewModel() {

    private val rickMortyRepository = RickMortyRepository()
    private val mutableRickMortyData = MutableLiveData<UiState<List<RickMorty>>>()
    val immutableRickMortyData: LiveData<UiState<List<RickMorty>>> = mutableRickMortyData

    fun getData() {
        mutableRickMortyData.postValue(UiState(isLoading = true))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val request = rickMortyRepository.getRickMortyResponse()
                Log.d("MainViewModel", "request code -> ${request.code()}")

                if(request.isSuccessful) {
                    val rickMorty = request.body()?.results
                    mutableRickMortyData.postValue(UiState(data = rickMorty))
                } else {
                    mutableRickMortyData.postValue(UiState(error = "${request.code()}"))
                    Log.d("MainViewModel", "request code -> ${request.code()}")
                }
            } catch (ex: Exception) {
                mutableRickMortyData.postValue(UiState(error = "Exc: $ex"))
                Log.e("MainViewModel: ", "Operacja nie powiodla sie", ex)
            }
        }
    }
}