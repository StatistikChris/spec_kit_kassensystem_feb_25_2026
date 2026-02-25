package de.barpos.kassensystem.data.tse

/**
 * T027 — Abstraction over the Deutsche Fiskal DFKA Cloud-TSE REST API.
 * Allows the domain layer to remain TSE-provider-agnostic and enables easy mocking in tests.
 */
interface TseClient {

    /**
     * Opens a new TSE transaction.
     * @return [TseSignResponse] with the assigned transaction number.
     */
    suspend fun startTransaction(request: TseStartRequest): TseSignResponse

    /**
     * Closes an existing TSE transaction and produces the fiscal signature.
     */
    suspend fun finishTransaction(request: TseFinishRequest): TseSignResponse

    /**
     * Queries the current TSE health / status.
     */
    suspend fun status(): TseStatusResponse
}
