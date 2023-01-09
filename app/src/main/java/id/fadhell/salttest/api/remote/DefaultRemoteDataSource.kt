package id.fadhell.salttest.api.remote

import id.fadhell.salttest.api.response.LoginResponse
import id.fadhell.salttest.api.ApiFactory
import id.fadhell.salttest.api.handler.handleRequestOnFlow
import id.fadhell.salttest.api.request.LoginRequest
import kotlinx.coroutines.flow.Flow

class DefaultRemoteDataSource(private val apiFactory: ApiFactory) : RemoteDataSource {

    override suspend fun login(request: LoginRequest): Flow<LoginResponse> =
        handleRequestOnFlow {
            apiFactory.login(request)
        }
}