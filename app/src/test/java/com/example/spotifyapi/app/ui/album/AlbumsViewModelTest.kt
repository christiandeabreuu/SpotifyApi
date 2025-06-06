import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import app.cash.turbine.test
import com.example.spotifyapi.app.data.local.ImageArtist
import com.example.spotifyapi.app.data.model.Album
import com.example.spotifyapi.app.domain.usecase.GetAlbumsUseCase
import com.example.spotifyapi.app.ui.album.AlbumsViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import androidx.paging.AsyncPagingDataDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AlbumsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var getAlbumsUseCase: GetAlbumsUseCase
    private lateinit var viewModel: AlbumsViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getAlbumsUseCase = mockk()
        viewModel = AlbumsViewModel(getAlbumsUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getAlbumsPagingData should delegate to use case and emit paging data`() = runTest {
        // Arrange
        val fakeImageArtist = listOf(
            ImageArtist(url = "image1", artistId = 1, databaseId = 0),
            ImageArtist(url = "image2", artistId = 1, databaseId = 1),
        )
        val artistId = "artist123"
        val album = Album(
            id = "1",
            name = "Album 1",
            images = fakeImageArtist,
            releaseDate = "",
            artistId = artistId,
        )
        val pagingData = PagingData.from(listOf(album))
        coEvery { getAlbumsUseCase.getAlbumsPagingData(artistId) } returns flowOf(pagingData)

        // Act
        val flow = viewModel.getAlbumsPagingData(artistId)

        val differ = AsyncPagingDataDiffer(
            diffCallback = object : DiffUtil.ItemCallback<Album>() {
                override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean =
                    oldItem.id == newItem.id

                override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean =
                    oldItem == newItem
            },
            updateCallback = NoopListUpdateCallback(),
            workerDispatcher = testDispatcher
        )

        flow.test {
            val pagingData = awaitItem()
            differ.submitData(pagingData)
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(1, differ.snapshot().size)
            assertEquals(album, differ.snapshot()[0])
            cancelAndIgnoreRemainingEvents()
        }
    }


    class NoopListUpdateCallback : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}
