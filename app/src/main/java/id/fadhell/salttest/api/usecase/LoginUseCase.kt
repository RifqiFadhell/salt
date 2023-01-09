package id.fadhell.salttest.api.usecase

import id.fadhell.salttest.api.remote.RemoteRepository
import id.fadhell.salttest.api.request.LoginRequest
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class LoginUseCase @Inject constructor(private val repository: RemoteRepository) :
    UseCase<LoginRequest, Flow<String>> {

    override suspend fun execute(params: LoginRequest): Flow<String> {
        return repository.login(params)
    }
}