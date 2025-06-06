package com.example.spotifyapi.app.ui.topartists

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.spotifyapi.R
import com.example.spotifyapi.app.data.model.ArtistResponse
import com.example.spotifyapi.app.data.model.UserProfile
import com.example.spotifyapi.app.domain.usecase.GetTopArtistsUseCase
import com.example.spotifyapi.app.domain.usecase.GetUserProfileUseCase
import com.example.spotifyapi.utils.Constants.MEDIUM_TERM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TopArtistsViewModel(
    private val userProfileUseCase: GetUserProfileUseCase,
    private val useCaseTopArtists: GetTopArtistsUseCase,
) : ViewModel() {

    private val _userProfileLiveData = MutableLiveData<UserProfile?>()
    val userProfileLiveData: LiveData<UserProfile?> get() = _userProfileLiveData

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> get() = _errorLiveData

    fun getArtistsPagingData(): Flow<PagingData<ArtistResponse>> =
        useCaseTopArtists.getTopArtistsPagingData().cachedIn(viewModelScope)

    fun getUserProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userProfile = userProfileUseCase.getUserProfile()
                _userProfileLiveData.postValue(userProfile)
            } catch (e: Exception) {
                _errorLiveData.postValue(R.string.error_message_search_profile.toString())
            }
        }
    }

    fun preloadOfflineArtists(timeRange: String = MEDIUM_TERM) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                useCaseTopArtists.preloadAllTopArtists(timeRange)
            } catch (e: Exception) {
                Log.d("TopArtistsViewModel", "Erro ao pré-carregar artistas offline: ${e.message}")
            }
        }
    }
}