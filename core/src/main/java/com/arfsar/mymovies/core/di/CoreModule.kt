package com.arfsar.mymovies.core.di

import androidx.room.Room
import com.arfsar.mymovies.core.BuildConfig
import com.arfsar.mymovies.core.data.MoviesRepository
import com.arfsar.mymovies.core.data.source.local.LocalDataSource
import com.arfsar.mymovies.core.data.source.local.preferences.SettingsPreferences
import com.arfsar.mymovies.core.data.source.local.preferences.dataStore
import com.arfsar.mymovies.core.data.source.local.room.MoviesDatabase
import com.arfsar.mymovies.core.data.source.remote.RemoteDataSource
import com.arfsar.mymovies.core.data.source.remote.network.ApiService
import com.arfsar.mymovies.core.domain.repository.IMoviesRepository
import com.arfsar.mymovies.core.utils.AppExecutors
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val baseUrl = BuildConfig.baseUrl
const val token = BuildConfig.token

val databaseModule = module {
    factory { get<MoviesDatabase>().moviesDao() }
    single {
        Room.databaseBuilder(
            androidContext(),
            MoviesDatabase::class.java, "Movies.db"
        ).fallbackToDestructiveMigration().build()
    }
}

val dataStoreModule = module {
    single {
        SettingsPreferences(androidContext().dataStore)
    }
}

val networkModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }
    single {
        val authInterceptor = Interceptor { chain ->
            val req = chain.request()
            val requestHeaders = req.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            chain.proceed(requestHeaders)
        }
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get<OkHttpClient>().newBuilder().addInterceptor(authInterceptor).build())
            .build()
        retrofit.create(ApiService::class.java)
    }
}

val repositoryModule = module {
    single { LocalDataSource(get()) }
    single { RemoteDataSource(get()) }
    single { SettingsPreferences(get()) }
    factory { AppExecutors() }
    single<IMoviesRepository> {
        MoviesRepository(
            get(),
            get(),
            get(),
            get()
        )
    }
}
