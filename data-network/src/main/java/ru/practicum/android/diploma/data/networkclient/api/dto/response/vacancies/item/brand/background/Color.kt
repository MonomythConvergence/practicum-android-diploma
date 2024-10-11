package ru.practicum.android.diploma.data.networkclient.api.dto.response.vacancies.item.brand.background

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Color(
    val color: String,
    val position: Int,
) : Parcelable
