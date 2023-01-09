package id.fadhell.salttest.api.remote

import id.fadhell.salttest.api.request.LoginRequest
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RemoteRepository @Inject constructor(
    private var remoteDataSource: RemoteDataSource
) {
    suspend fun login(loginRequest: LoginRequest): Flow<String> =
        remoteDataSource.login(loginRequest).map { it.token.orEmpty() }
}