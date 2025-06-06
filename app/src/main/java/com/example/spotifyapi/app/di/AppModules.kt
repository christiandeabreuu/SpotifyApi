package com.example.spotifyapi.app.di

import com.example.spotifyapi.app.data.paging.AlbumsPagingSource
import androidx.room.Room
import com.example.spotifyapi.app.data.database.SpotifyDatabase
import com.example.spotifyapi.app.data.networking.SpotifyApiService
import com.example.spotifyapi.app.data.paging.ArtistPagingSource
import com.example.spotifyapi.app.data.repository.AlbumsRepository
import com.example.spotifyapi.app.data.repository.AlbumsRepositoryImpl
import com.example.spotifyapi.app.data.repository.CreatePlaylistRepository
import com.example.spotifyapi.app.data.repository.CreatePlaylistRepositoryImpl
import com.example.spotifyapi.app.data.repository.PlaylistRepository
import com.example.spotifyapi.app.data.repository.PlaylistRepositoryImpl
import com.example.spotifyapi.app.data.repository.TopArtistsRepository
import com.example.spotifyapi.app.data.repository.TopArtistsRepositoryImpl
import com.example.spotifyapi.app.data.repository.UserProfileRepository
import com.example.spotifyapi.app.data.repository.UserProfileRepositoryImpl
import com.example.spotifyapi.app.domain.usecase.CreatePlaylistUseCase
import com.example.spotifyapi.app.domain.usecase.GetAlbumsUseCase
import com.example.spotifyapi.app.domain.usecase.GetPlaylistsUseCase
import com.example.spotifyapi.app.domain.usecase.GetTopArtistsUseCase
import com.example.spotifyapi.app.domain.usecase.GetUserProfileUseCase
import com.example.spotifyapi.app.ui.album.AlbumsViewModel
import com.example.spotifyapi.app.ui.createplaylist.CreatePlaylistViewModel
import com.example.spotifyapi.app.ui.playlist.PlaylistViewModel
import com.example.spotifyapi.app.ui.profile.ProfileViewModel
import com.example.spotifyapi.app.ui.topartists.TopArtistsAdapter
import com.example.spotifyapi.app.ui.topartists.TopArtistsViewModel
import com.example.spotifyapi.auth.data.plugin.ResourcesPlugin
import com.example.spotifyapi.auth.data.plugin.ResourcesPluginImpl
import com.example.spotifyapi.utils.Constants.BASE_URL_SPOTIFY_APP
import com.example.spotifyapi.utils.Constants.SPOTIFY_DATABASE
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.converter.gson.GsonConverterFactory

val appModules = module {

    viewModel { TopArtistsViewModel(get(), get()) }
    viewModel { AlbumsViewModel(get()) }
    viewModel { ProfileViewModel(get(), get()) }
    viewModel { PlaylistViewModel(get(), get(), get()) }
    viewModel { CreatePlaylistViewModel(get(), get()) }

    factory { GetAlbumsUseCase(get()) }
    factory { GetUserProfileUseCase(get()) }
    factory { GetPlaylistsUseCase(get()) }
    factory { CreatePlaylistUseCase(get(), get()) }
    factory { GetTopArtistsUseCase(get()) }

    factory<AlbumsRepository> { AlbumsRepositoryImpl(get(), get(), get()) }
    factory<UserProfileRepository> { UserProfileRepositoryImpl(get(), get(), get()) }
    factory<PlaylistRepository> { PlaylistRepositoryImpl(get(), get(), get()) }
    factory<CreatePlaylistRepository> { CreatePlaylistRepositoryImpl(get(), get()) }
    factory<TopArtistsRepository> { TopArtistsRepositoryImpl(get(), get(), get()) }

    factory { ArtistPagingSource(get()) }
    factory { AlbumsPagingSource(get(), get()) }
    factory { TopArtistsAdapter(get()) }
    factory<ResourcesPlugin> { ResourcesPluginImpl(get()) }
}

val networkAppModule = module {
    single {
        retrofit2.Retrofit.Builder().baseUrl(BASE_URL_SPOTIFY_APP)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(SpotifyApiService::class.java)
    }
}

val databaseModule = module {

    single {
        Room.databaseBuilder(
            get(), SpotifyDatabase::class.java, SPOTIFY_DATABASE
        ).fallbackToDestructiveMigration().build()
    }

    single { get<SpotifyDatabase>().spotifyDao() }
}
