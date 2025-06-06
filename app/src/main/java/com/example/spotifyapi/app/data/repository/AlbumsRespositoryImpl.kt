package com.example.spotifyapi.app.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.spotifyapi.app.data.database.SpotifyDAO
import com.example.spotifyapi.app.data.local.AlbumDB
import com.example.spotifyapi.app.data.model.Album
import com.example.spotifyapi.app.data.model.AlbumsResponse
import com.example.spotifyapi.app.data.networking.SpotifyApiService
import com.example.spotifyapi.app.data.paging.AlbumsPagingSource
import com.example.spotifyapi.auth.data.repository.TokenRepository
import com.example.spotifyapi.utils.addBearer

class AlbumsRepositoryImpl(
    private val apiService: SpotifyApiService,
    private val spotifyDAO: SpotifyDAO,
    private val tokenRepository: TokenRepository
) : AlbumsRepository {

    override suspend fun getAlbumsFromApi(artistId: String): AlbumsResponse? {
        return try {
            val token = tokenRepository.getAccessToken().orEmpty()
            apiService.getAlbums(token.addBearer(), artistId)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getAlbumsFromApi(artistId: String, offset: Int): AlbumsResponse? {
        return try {
            val token = tokenRepository.getAccessToken().orEmpty()
            apiService.getAlbums(token.addBearer(), artistId, offset, limit = 20)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getAlbumsFromDB(artistId: String): List<AlbumDB> {
        return spotifyDAO.getLocalAlbumsByArtist(artistId)
    }

    override suspend fun insertLocalAlbums(albums: List<AlbumDB>) {
        spotifyDAO.insertLocalAlbums(albums)
    }

    override fun getAlbumsPaged(artistId: String): Pager<Int, Album> {
        return Pager(config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { AlbumsPagingSource(this, artistId) })
    }
}