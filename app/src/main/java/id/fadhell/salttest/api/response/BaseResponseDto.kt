package id.fadhell.salttest.api.response

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Serializable
@Keep
data class BaseResponseDto(
    val statusCode: Int?,
    val message: String?,
    val error: String?
)