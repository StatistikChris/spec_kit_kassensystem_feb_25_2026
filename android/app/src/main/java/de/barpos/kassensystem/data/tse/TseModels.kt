package de.barpos.kassensystem.data.tse

/**
 * Request payload sent to the Deutsche Fiskal DFKA Cloud-TSE REST API to start a transaction.
 */
data class TseStartRequest(
    val clientId: String,
    val processType: String = "Kassenbeleg-V1",
    val processData: String
)

/**
 * Request payload to finish a transaction.
 */
data class TseFinishRequest(
    val clientId: String,
    val transactionNumber: Long,
    val processType: String = "Kassenbeleg-V1",
    val processData: String
)

/**
 * Response received from the TSE after signing a transaction.
 */
data class TseSignResponse(
    val transactionNumber: Long,
    val signatureCounter: Long,
    val signatureBase64: String,
    val logTime: String,
    val logTimeFormat: String,
    val serialNumber: String,
    val processType: String,
    val processData: String
)

/**
 * Response from the TSE health-check endpoint.
 */
data class TseStatusResponse(
    val signatureAlgorithm: String,
    val publicKey: String,
    val serialNumber: String,
    val certificateExpirationDate: String,
    val signatureCreationDeviceFailed: Boolean,
    val remainingCapacity: Int?
)
