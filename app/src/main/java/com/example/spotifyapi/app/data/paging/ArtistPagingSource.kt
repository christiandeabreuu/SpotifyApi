package com.example.spotifyapi.app.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.spotifyapi.app.data.model.ArtistResponse
import com.example.spotifyapi.app.data.model.ImageArtistResponse
import com.example.spotifyapi.app.domain.usecase.GetTopArtistsUseCase
import com.example.spotifyapi.utils.Constants.MEDIUM_TERM

class ArtistPagingSource(
    private val useCaseTopArtists: GetTopArtistsUseCase
) : PagingSource<Int, ArtistResponse>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArtistResponse> {
        return try {
            val nextPageNumber = params.key ?: 0
            val response: List<ArtistResponse> = try {
                useCaseTopArtists.fetchAndSaveTopArtists(nextPageNumber).map { artist ->
                    ArtistResponse(
                        id = artist.id,
                        name = artist.name,
                        popularity = artist.popularity,
                        images = listOf(ImageArtistResponse(url = artist.imageUrl))
                    )
                }
            } catch (apiException: Exception) {
                emptyList()
            }

            val finalResponse = response.ifEmpty {
                useCaseTopArtists.getTopArtistsFromDB(20, nextPageNumber, MEDIUM_TERM)
                    .map { artist ->
                        ArtistResponse(
                            id = artist.id,
                            name = artist.name,
                            popularity = artist.popularity,
                            images = listOf(ImageArtistResponse(url = artist.imageUrl))
                        )
                    }
            }

            LoadResult.Page(
                data = finalResponse,
                prevKey = if (nextPageNumber == 0) null else nextPageNumber - 20,
                nextKey = if (finalResponse.isEmpty()) null else nextPageNumber + 20
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ArtistResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(20) ?: anchorPage?.nextKey?.minus(20)
        }
    }
}