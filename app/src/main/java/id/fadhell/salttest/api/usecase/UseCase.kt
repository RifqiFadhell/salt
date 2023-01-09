package id.fadhell.salttest.api.usecase

interface UseCase<P, R> {
    suspend fun execute(params: P): R
}