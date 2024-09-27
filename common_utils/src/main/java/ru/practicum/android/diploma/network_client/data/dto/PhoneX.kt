package ru.practicum.android.diploma.network_client.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PhoneX(
    val city: String,
    val comment: String?,
    val country: String,
    val number: String,
) : Parcelable
