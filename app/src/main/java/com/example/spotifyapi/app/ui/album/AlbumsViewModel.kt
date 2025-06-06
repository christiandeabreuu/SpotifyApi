package com.example.spotifyapi.app.ui.album

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.spotifyapi.app.data.model.Album
import com.example.spotifyapi.app.domain.usecase.GetAlbumsUseCase
import kotlinx.coroutines.flow.Flow

class AlbumsViewModel(
    private val getAlbumsUseCase: GetAlbumsUseCase
) : ViewModel() {

    private val _albumsLiveData = MutableLiveData<List<Album>>()
    val albumsLiveData: LiveData<List<Album>> get() = _albumsLiveData

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> get() = _errorLiveData

    fun getAlbumsPagingData(artistId: String): Flow<PagingData<Album>> {
        return getAlbumsUseCase.getAlbumsPagingData(artistId).cachedIn(viewModelScope)
    }
}