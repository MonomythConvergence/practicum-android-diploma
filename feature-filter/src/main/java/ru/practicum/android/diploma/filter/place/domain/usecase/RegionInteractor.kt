package ru.practicum.android.diploma.filter.place.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.filter.place.domain.model.AreaInReference
import ru.practicum.android.diploma.filter.place.domain.model.Place

interface RegionInteractor {
    fun listAreas(): Flow<Pair<List<AreaInReference>?, String?>>
    suspend fun getPlaceDataFilter() : Place?
    suspend fun updatePlaceInDataFilter(placeDto: Place) : Int
}
