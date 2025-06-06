package com.example.spotifyapi.app.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.spotifyapi.app.data.model.Album
import com.example.spotifyapi.app.data.repository.AlbumsRepository
import com.example.spotifyapi.app.domain.mapper.AlbumMapper.toAlbum
import com.example.spotifyapi.app.domain.mapper.AlbumMapper.toAlbumDB

class AlbumsPagingSource(
    private val repository: AlbumsRepository, private val artistId: String
) : PagingSource<Int, Album>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Album> {
        val offset = params.key ?: 0
        return try {
            val response = repository.getAlbumsFromApi(artistId, offset)
            val albumsFromApi = response?.items?.map { it.toAlbumDB(artistId) } ?: emptyList()
            if (albumsFromApi.isNotEmpty()) {
                repository.insertLocalAlbums(albumsFromApi)
            }

            val albums = if (response?.items.isNullOrEmpty()) {
                repository.getAlbumsFromDB(artistId).map { it.toAlbum() }
            } else {
                response?.items?.map { it } ?: emptyList()
            }

            val nextKey = if (albums.size < 20) null else offset + 20
            LoadResult.Page(
                data = albums, prevKey = if (offset == 0) null else offset - 20, nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Album>): Int? {
        return state.anchorPosition?.let { position ->
            val anchorPage = state.closestPageToPosition(position)
            anchorPage?.prevKey?.plus(20) ?: anchorPage?.nextKey?.minus(20)
        }
    }
}