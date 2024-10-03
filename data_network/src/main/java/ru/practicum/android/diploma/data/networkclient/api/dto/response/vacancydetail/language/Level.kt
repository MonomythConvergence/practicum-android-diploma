package ru.practicum.android.diploma.data.networkclient.api.dto.response.vacancydetail.language

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Level(
    val id: String,
    val name: String,
) : Parcelable
