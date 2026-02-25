package de.barpos.kassensystem.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.barpos.kassensystem.BuildConfig
import de.barpos.kassensystem.data.tse.TseApiService
import de.barpos.kassensystem.data.tse.TseClient
import de.barpos.kassensystem.data.tse.TseClientImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {

    companion object {

        // Base URL from BuildConfig; set in app/build.gradle.kts via buildConfigField
        private const val TSE_BASE_URL = "https://dfka.fiskal.cloud/"

        @Provides
        @Singleton
        fun provideOkHttpClient(): OkHttpClient =
            OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .apply {
                    if (BuildConfig.DEBUG) {
                        addInterceptor(
                            HttpLoggingInterceptor().apply {
                                level = HttpLoggingInterceptor.Level.BODY
                            }
                        )
                    }
                }
                .build()

        @Provides
        @Singleton
        fun provideTseRetrofit(okHttpClient: OkHttpClient): Retrofit =
            Retrofit.Builder()
                .baseUrl(TSE_BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        @Provides
        @Singleton
        fun provideTseApiService(retrofit: Retrofit): TseApiService =
            retrofit.create(TseApiService::class.java)
    }

    @Binds
    @Singleton
    abstract fun bindTseClient(impl: TseClientImpl): TseClient
}
