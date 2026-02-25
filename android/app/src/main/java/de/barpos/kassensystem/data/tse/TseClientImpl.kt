package de.barpos.kassensystem.data.tse

import javax.inject.Inject

/**
 * T028 — Live TSE client backed by Retrofit.
 * All calls are suspend functions; Room off-loading happens in the use-case layer.
 */
class TseClientImpl @Inject constructor(
    private val api: TseApiService
) : TseClient {

    override suspend fun startTransaction(request: TseStartRequest): TseSignResponse =
        api.startTransaction(request)

    override suspend fun finishTransaction(request: TseFinishRequest): TseSignResponse =
        api.finishTransaction(request)

    override suspend fun status(): TseStatusResponse =
        api.status()
}
