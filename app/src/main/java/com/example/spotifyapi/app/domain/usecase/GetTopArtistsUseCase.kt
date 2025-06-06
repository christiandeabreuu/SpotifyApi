package com.example.spotifyapi.app.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.spotifyapi.app.data.local.ArtistDB
import com.example.spotifyapi.app.data.model.ArtistResponse
import com.example.spotifyapi.app.data.paging.ArtistPagingSource
import com.example.spotifyapi.app.data.repository.TopArtistsRepository
import com.example.spotifyapi.utils.Constants.MEDIUM_TERM
import kotlinx.coroutines.flow.Flow

class GetTopArtistsUseCase(
    private val repository: TopArtistsRepository
) {

    fun getTopArtistsPagingData(): Flow<PagingData<ArtistResponse>> =
        Pager(config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { ArtistPagingSource(this) }).flow

    suspend fun getTopArtistsFromDB(limit: Int, offset: Int, timeRange: String): List<ArtistDB> {
        return repository.getTopArtistsFromDB(limit, offset, timeRange)
    }

    suspend fun fetchAndSaveTopArtists(
        offset: Int = 0, timeRange: String = MEDIUM_TERM
    ): List<ArtistDB> {
        val responseApi = repository.getTopArtistsApi(offset, timeRange)
        repository.insertArtists(responseApi)
        return responseApi
    }

    suspend fun preloadAllTopArtists(timeRange: String = MEDIUM_TERM) {
        var offset = 0
        val pageSize = 50
        while (true) {
            val response = repository.getTopArtistsApi(offset, timeRange)
            if (response.isEmpty()) break
            repository.insertArtists(response)
            offset += pageSize
        }
    }
}