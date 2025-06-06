package com.example.spotifyapi.app.ui.topartists

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.spotifyapi.app.data.model.Image
import com.example.spotifyapi.app.data.model.UserProfile
import com.example.spotifyapi.app.domain.usecase.GetTopArtistsUseCase
import com.example.spotifyapi.app.domain.usecase.GetUserProfileUseCase
import com.example.spotifyapi.utils.Constants
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TopArtistsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // Para rodar coroutines no thread de teste
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    private val userProfileObserver: Observer<UserProfile?> = mockk(relaxed = true)


    private val getUserProfileUseCase: GetUserProfileUseCase = mockk(relaxed = true)
    private val getTopArtistsUseCase: GetTopArtistsUseCase = mockk(relaxed = true)

    private lateinit var viewModel: TopArtistsViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = TopArtistsViewModel(getUserProfileUseCase, getTopArtistsUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getUserProfile posts value to userProfileLiveData on success`() = runTest {
        // Arrange
        val userProfile = UserProfile("user_id", "User", emptyList())
        coEvery { getUserProfileUseCase.getUserProfile() } returns userProfile

        val observer = mockk<Observer<UserProfile?>>(relaxed = true)
        viewModel.userProfileLiveData.observeForever(observer)

        // Act
        viewModel.getUserProfile()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        verify { observer.onChanged(userProfile) }
        viewModel.userProfileLiveData.removeObserver(observer)
    }

    @Test
    fun `getUserProfile posts error to errorLiveData on failure`() = runTest {
        // Arrange
        coEvery { getUserProfileUseCase.getUserProfile() } throws RuntimeException("error")

        val observer = mockk<Observer<String>>(relaxed = true)
        viewModel.errorLiveData.observeForever(observer)

        // Act
        viewModel.getUserProfile()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        verify { observer.onChanged(any()) }
        viewModel.errorLiveData.removeObserver(observer)
    }

    @Test
    fun `preloadOfflineArtists calls useCase method`() = runTest {
        // Arrange
        coEvery { getTopArtistsUseCase.preloadAllTopArtists(Constants.MEDIUM_TERM) } just Runs

        // Act
        viewModel.preloadOfflineArtists()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        coVerify { getTopArtistsUseCase.preloadAllTopArtists(Constants.MEDIUM_TERM) }
    }
}