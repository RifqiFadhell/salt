package id.fadhell.salttest.api.remote

import id.fadhell.salttest.api.request.LoginRequest
import id.fadhell.salttest.api.response.LoginResponse
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {
    suspend fun login(loginRequest: LoginRequest): Flow<LoginResponse>
}