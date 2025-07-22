package home.usecase

import entity.Actor
import home.repository.HomeRepository
import kotlinx.coroutines.flow.Flow

class GetTrendingPeopleUseCase(
    private val homeRepository: HomeRepository
) {
    operator fun invoke(): Flow<List<Actor>> {
        return homeRepository.getTrendingPeople()
    }
}