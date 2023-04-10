package com.app.procom.di

import com.app.procom.mappers.MovieDataMapper
import com.app.procom.navigation.NavigationManager
import com.app.procom.repository.movies.MoviesRepository
import com.app.procom.repository.movies.MoviesRepositoryDependencies
import com.app.procom.repository.movies.MoviesRepositoryImpl
import com.app.procom.repository.movies.local.MovieLocalDataSource
import com.app.procom.repository.movies.local.MovieLocalDataSourceImpl
import com.app.procom.repository.movies.remote.MoviesRemoteDataSource
import com.app.procom.repository.movies.remote.MoviesRemoteDataSourceImpl
import com.app.procom.services.MoviesApi
import com.app.procom.storage.AppDatabase
import com.app.procom.ui.home.MoviesViewModel
import com.app.procom.usecase.MoviesUseCase
import com.app.procom.util.Constants.MOVIES_BASE_URL
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val appModule = module {
    single<MoviesApi> {
        Retrofit.Builder()
            .baseUrl(MOVIES_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(OkHttpClient())
            .build()
            .create(MoviesApi::class.java)
    }
    factory { AppDatabase.getAppDatabase(context = get()).movieDao() }
    single { NavigationManager() }
}

val moviesModules = module {

    viewModel { MoviesViewModel(useCase = get(), navigationManager = get()) }
    factory { MoviesUseCase(repository = get(), dataMapper = get()) }
    factory { MovieDataMapper() }
    factory<MovieLocalDataSource> { MovieLocalDataSourceImpl(dao = get()) }
    factory<MoviesRemoteDataSource> { MoviesRemoteDataSourceImpl(moviesApi = get()) }
    factory {
        MoviesRepositoryDependencies(
            dataMapper = get(),
            localDataSource = get(),
            remoteDataSource = get()
        )
    }

    factory<MoviesRepository> { MoviesRepositoryImpl(dependencies = get()) }
}