package id.fadhell.salttest.api.handler

import id.fadhell.salttest.api.response.BaseResponseDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.json.Json
import org.json.JSONObject
import kotlinx.serialization.decodeFromString
import retrofit2.HttpException

fun <T : Any> handleRequestOnFlow(
    requestFunc: suspend () -> T
): Flow<T> {
    return flow {
        try {
            emit(requestFunc.invoke())
        } catch (ex: Throwable) {
            val exception = when (ex) {
                is HttpException -> {
                    val errorResponse = JSONObject(ex.response()?.errorBody()?.string().orEmpty())
                    val convertToErrorMessage = try {
                        Json.decodeFromString<BaseResponseDto>(errorResponse.toString())
                    } catch (e: Exception) {
                        null
                    }
                    CommonApiException(convertToErrorMessage?.message.orEmpty(), ex.code())
                }
                else -> {
                    CommonApiException("Error Message")
                }
            }
            throw exception
        }
    }.flowOn(Dispatchers.IO)
}

open class CommonApiException(message: String, statusCode: Int? = null) : Exception(message, Throwable(statusCode.toString()))