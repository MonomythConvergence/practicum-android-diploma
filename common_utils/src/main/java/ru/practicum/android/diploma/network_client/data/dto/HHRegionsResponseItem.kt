package ru.practicum.android.diploma.network_client.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HHRegionsResponseItem(
    val areas: List<AreaX>,
    val id: String,
    val name: String,
    val parent_id: String?
): Parcelable
