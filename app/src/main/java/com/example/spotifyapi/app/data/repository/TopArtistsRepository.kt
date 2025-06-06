package com.example.spotifyapi.app.data.repository

import com.example.spotifyapi.app.data.local.ArtistDB

interface TopArtistsRepository {
    suspend fun getTopArtistsApi(offset: Int = 0, timeRange: String = "medium_term"): List<ArtistDB>
    suspend fun insertArtists(artists: List<ArtistDB>): List<Long>
    suspend fun getTopArtistsFromDB(limit: Int, offset: Int, timeRange: String): List<ArtistDB> // NOVO
}