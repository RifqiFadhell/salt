package id.fadhell.salttest.api

import id.fadhell.salttest.api.request.LoginRequest
import id.fadhell.salttest.api.response.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiFactory {

    @POST("login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}