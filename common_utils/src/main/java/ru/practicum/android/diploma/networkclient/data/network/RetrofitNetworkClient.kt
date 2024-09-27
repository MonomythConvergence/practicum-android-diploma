package ru.practicum.android.diploma.networkclient.data.network

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.commonutils.NetworkUtils
import ru.practicum.android.diploma.networkclient.data.NetworkClient
import ru.practicum.android.diploma.networkclient.data.dto.HHApiIndustriesRequest
import ru.practicum.android.diploma.networkclient.data.dto.HHApiRegionsRequest
import ru.practicum.android.diploma.networkclient.data.dto.HHApiVacanciesRequest
import ru.practicum.android.diploma.networkclient.data.dto.HHApiVacancyRequest
import ru.practicum.android.diploma.networkclient.data.dto.Response
import ru.practicum.android.diploma.networkclient.domain.models.HttpStatus

class RetrofitNetworkClient(
    private val hhApiService: HHApiService,
    private val context: Context,
) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        Log.d(TAG, "Starting request to HH")
        if (NetworkUtils().isConnected(context)) {
            return object : Response {
                override var resultCode = HttpStatus.NO_INTERNET
            }
        }
        if (!(dto is HHApiIndustriesRequest || dto is HHApiVacanciesRequest || dto is HHApiVacancyRequest || dto is HHApiRegionsRequest)) {
            Log.e(TAG, "Error is ${dto::class.qualifiedName}")
            return object : Response {
                override var resultCode = HttpStatus.CLIENT_ERROR
            }
        }
        return withContext(Dispatchers.IO) {
            try {
                makeActualRequest(dto)
            } catch (e: Exception) {
                Log.e(TAG, e.message ?: "")
                Log.e(TAG, e.stackTrace.toString())
                object : Response {
                    override var resultCode = HttpStatus.SERVER_ERROR
                }
            }
        }
    }

    private suspend fun makeActualRequest(dto: Any): Response {
        return when (dto) {
            is HHApiRegionsRequest -> {
                val request = if (dto.term.isNullOrEmpty()) null else mapOf(QUERY to dto.term)
                val response = hhApiService.searchRegions(request)
                response
            }

            is HHApiVacancyRequest -> {
                val response = hhApiService.getVacancy(dto.vacancyId)
                response
            }

            is HHApiVacanciesRequest -> {
                val request = mapOf("query" to dto.request)
                val response = hhApiService.searchVacancies(request)
                response
            }

            is HHApiIndustriesRequest -> {
                val request = if (dto.term.isNullOrEmpty()) null else mapOf(QUERY to dto.term)
                val response = hhApiService.searchIndustries(request)
                response
            }

            else -> {
                object : Response {
                    override var resultCode = HttpStatus.CLIENT_ERROR
                }
            }
        }

    }

    companion object {
        private const val TAG = "RetrofitNetworkClient"
        private const val QUERY = "query"
    }
}
