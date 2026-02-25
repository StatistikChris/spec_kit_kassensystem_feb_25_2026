package de.barpos.kassensystem.data.tse

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Retrofit service interface for the Deutsche Fiskal DFKA Cloud-TSE REST API.
 * Base URL is configured in [de.barpos.kassensystem.di.NetworkModule].
 */
interface TseApiService {

    @POST("api/v1/tse/transactions/start")
    suspend fun startTransaction(@Body request: TseStartRequest): TseSignResponse

    @POST("api/v1/tse/transactions/finish")
    suspend fun finishTransaction(@Body request: TseFinishRequest): TseSignResponse

    @GET("api/v1/tse/status")
    suspend fun status(): TseStatusResponse
}
