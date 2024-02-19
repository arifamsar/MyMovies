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
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import okhttp3.CertificatePinner
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
        val passphrase: ByteArray = SQLiteDatabase.getBytes("arfsar".toCharArray())
        val factory = SupportFactory(passphrase)
        Room.databaseBuilder(
            androidContext(),
            MoviesDatabase::class.java, "Movies.db"
        ).fallbackToDestructiveMigration()
            .openHelperFactory(factory)
            .build()
    }
}

val dataStoreModule = module {
    single {
        SettingsPreferences(androidContext().dataStore)
    }
}

val networkModule = module {
    single {
        val hostname = "api.themoviedb.org"
        val certificatePinner = CertificatePinner.Builder()
            .add(hostname, "sha256/5VLcahb6x4EvvFrCF2TePZulWqrLHS2jCg9Ywv6JHog=")
            .add(hostname, "sha256/vxRon/El5KuI4vx5ey1DgmsYmRY0nDd5Cg4GfJ8S+bg=")
            .add(hostname, "sha256/++MBgDH5WGvL9Bcn5Be30cRcL0f5O+NyoXuWtQdX1aI=")
            .add(hostname, "sha256/KwccWaCgrnaw6tsrrSO61FgLacNgG2MMLq8GE6+oP5I=")
            .build()
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .certificatePinner(certificatePinner)
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
