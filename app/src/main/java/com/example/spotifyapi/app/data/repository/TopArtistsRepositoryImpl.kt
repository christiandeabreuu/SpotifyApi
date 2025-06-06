package com.example.spotifyapi.app.data.repository

import com.example.spotifyapi.app.data.database.SpotifyDAO
import com.example.spotifyapi.app.data.local.ArtistDB
import com.example.spotifyapi.app.data.networking.SpotifyApiService
import com.example.spotifyapi.app.domain.mapper.TopArtistsMapper
import com.example.spotifyapi.auth.data.repository.TokenRepository
import com.example.spotifyapi.utils.addBearer

class TopArtistsRepositoryImpl(
    private val apiService: SpotifyApiService,
    private val spotifyDAO: SpotifyDAO,
    private val tokenRepository: TokenRepository
) : TopArtistsRepository {

    override suspend fun getTopArtistsApi(offset: Int, timeRange: String): List<ArtistDB> {
        val token = tokenRepository.getAccessToken().orEmpty()
        val response = apiService.getTopArtists(
            accessToken = token.addBearer(), limit = 50, timeRange = timeRange, offset = offset
        )

        return TopArtistsMapper.toArtistsDB(response, timeRange)
    }

    override suspend fun insertArtists(artists: List<ArtistDB>): List<Long> {
        return spotifyDAO.insertArtists(artists)
    }

    override suspend fun getTopArtistsFromDB(
        limit: Int, offset: Int, timeRange: String
    ): List<ArtistDB> {
        return spotifyDAO.getTopArtistsWithOffsetAndLimit(limit, offset, timeRange)
    }
}