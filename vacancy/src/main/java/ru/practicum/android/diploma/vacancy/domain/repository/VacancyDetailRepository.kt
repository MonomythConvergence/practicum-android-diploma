package ru.practicum.android.diploma.vacancy.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.commonutils.Resource
import ru.practicum.android.diploma.vacancy.domain.model.Vacancy

interface VacancyDetailRepository {
    fun getVacancyNetwork(id: String): Flow<Resource<Vacancy>>
    fun getVacancyDb(id: Int): Flow<Resource<Vacancy?>>
}
