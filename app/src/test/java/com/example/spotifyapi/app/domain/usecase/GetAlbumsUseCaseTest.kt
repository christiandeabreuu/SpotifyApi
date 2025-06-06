package com.example.spotifyapi.app.domain.usecase

import androidx.paging.PagingData
import com.example.spotifyapi.app.data.model.Album
import com.example.spotifyapi.app.data.repository.AlbumsRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetAlbumsUseCaseTest {

    @MockK
    private lateinit var repository: AlbumsRepository

    private lateinit var useCase: GetAlbumsUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = GetAlbumsUseCase(repository)
    }

    @Test
    fun `getAlbumsPagingData deve retornar os albums corretamente`() = runTest {
        // Dados fictícios
        val fakeAlbums = listOf(
            Album(id = "1", name = "Album A", "release_date", emptyList(), "artist123"),
            Album(id = "2", name = "Album B", "", emptyList(), "artist1234"),
        )

        // Criando um fluxo de paginação com os dados fictícios
        val pagingData: Flow<PagingData<Album>> = flowOf(PagingData.from(fakeAlbums))

        // Mockando a chamada do repositório
        coEvery { repository.getAlbumsPaged("test_artist_id").flow } returns pagingData

        // Chamando o método do UseCase
        val result = useCase.getAlbumsPagingData("test_artist_id")

        // Verificando se o fluxo produzido é o esperado
        assertEquals(pagingData, result)
    }
}
