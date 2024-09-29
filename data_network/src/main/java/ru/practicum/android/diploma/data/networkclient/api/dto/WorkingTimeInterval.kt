package ru.practicum.android.diploma.data.networkclient.api.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class WorkingTimeInterval(
    val id: String,
    val name: String,
) : Parcelable
