package ru.practicum.android.diploma.data.networkclient.api.network

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.commonutils.Utils
import ru.practicum.android.diploma.commonutils.network.isConnected
import ru.practicum.android.diploma.data.networkclient.api.NetworkClient
import ru.practicum.android.diploma.data.networkclient.api.dto.request.HHApiIndustriesRequest
import ru.practicum.android.diploma.data.networkclient.api.dto.request.HHApiRegionsRequest
import ru.practicum.android.diploma.data.networkclient.api.dto.request.HHApiVacanciesRequest
import ru.practicum.android.diploma.data.networkclient.api.dto.request.HHApiVacancyRequest
import ru.practicum.android.diploma.commonutils.network.HttpStatus
import ru.practicum.android.diploma.commonutils.network.Response

private const val TAG = "RetrofitNetworkClient"
internal class RetrofitNetworkClient(
    private val hhApiService: HHApiService,
    private val context: Context,
) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        Log.d(TAG, "Starting request to HH")
        if (!context.isConnected()) {
            return object : Response {
                override var resultCode = HttpStatus.NO_INTERNET
            }
        }
        return withContext(Dispatchers.IO) {
            runCatching {
                when (dto) {
                    is HHApiIndustriesRequest -> industriesRequest(
                        dto
                    )
                    is HHApiVacanciesRequest -> vacancyListRequest(
                        dto
                    )
                    is HHApiVacancyRequest -> vacancyRequest(dto)
                    is HHApiRegionsRequest -> regionsRequest(dto)
                    else -> throw Exception("Error is ${dto::class.qualifiedName}")
                }
            }.fold(
                onSuccess = { response ->
                    response.apply { resultCode = HttpStatus.OK }
                },
                onFailure = { e ->
                    Utils.outputStackTrace(TAG, e)
                    object : Response {
                        override var resultCode = HttpStatus.CLIENT_ERROR
                    }
                }
            )
        }
    }

    private suspend fun regionsRequest(dto: HHApiRegionsRequest): Response {
        val regions = hhApiService.searchRegions(dto.options)
        Log.d(TAG, regions.toString())
        return regions
    }

    private suspend fun vacancyListRequest(dto: HHApiVacanciesRequest): Response {
        return hhApiService.searchVacancies(dto.request)
    }

    private suspend fun vacancyRequest(dto: HHApiVacancyRequest): Response {
        val vacancy = hhApiService.getVacancy(dto.vacancyId)
        Log.d(TAG, vacancy.toString())
        return vacancy
    }

    private suspend fun industriesRequest(dto: HHApiIndustriesRequest): Response {
        val industries = hhApiService.searchIndustries(dto.options)
        Log.d(TAG, industries.toString())
        return industries
    }
}
