package id.fadhell.salttest.api

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import kotlinx.serialization.ExperimentalSerializationApi
import dagger.hilt.components.SingletonComponent
import id.fadhell.salttest.api.remote.DefaultRemoteDataSource
import id.fadhell.salttest.api.remote.RemoteDataSource
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Provides
    @Singleton
    fun provideBaseUrl() = "https://reqres.in/api/"

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(10000, TimeUnit.SECONDS)
            .readTimeout(10000, TimeUnit.SECONDS)
            .retryOnConnectionFailure(false)
            .build()
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, BASE_URL:String, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit) = retrofit.create(ApiFactory::class.java)

    @Provides
    @Singleton
    fun provideUserDataSource(httpClient: ApiFactory): RemoteDataSource {
        return DefaultRemoteDataSource(httpClient)
    }

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }
}