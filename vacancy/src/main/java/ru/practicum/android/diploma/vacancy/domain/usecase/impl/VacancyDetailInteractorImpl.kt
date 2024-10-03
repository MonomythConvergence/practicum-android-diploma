package ru.practicum.android.diploma.vacancy.domain.usecase.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.commonutils.Resource
import ru.practicum.android.diploma.vacancy.domain.model.Vacancy
import ru.practicum.android.diploma.vacancy.domain.repository.VacancyDetailRepository
import ru.practicum.android.diploma.vacancy.domain.usecase.VacancyDetailInteractor

internal class VacancyDetailInteractorImpl(private val repository: VacancyDetailRepository) : VacancyDetailInteractor {

    override fun getVacancyNetwork(id: String): Flow<Pair<Vacancy?, String?>> {
        return repository.getVacancyNetwork(id).map { result ->
            Resource.handleResource(result)
        }
    }

    override fun getVacancyDb(id: Int): Flow<Pair<Vacancy?, String?>> {
        return repository.getVacancyDb(id).map { resource -> Resource.handleResource(resource) }
    }

}
