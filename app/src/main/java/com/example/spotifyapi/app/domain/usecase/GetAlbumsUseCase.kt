package com.example.spotifyapi.app.domain.usecase

import androidx.paging.PagingData
import com.example.spotifyapi.app.data.model.Album
import com.example.spotifyapi.app.data.repository.AlbumsRepository
import kotlinx.coroutines.flow.Flow

class GetAlbumsUseCase(private val repository: AlbumsRepository) {

    fun getAlbumsPagingData(artistId: String): Flow<PagingData<Album>> {
        return repository.getAlbumsPaged(artistId).flow
    }
}