package ru.practicum.android.diploma.network_client.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SalaryX(
    val currency: String,
    val from: Int,
    val gross: Boolean,
    val to: Int?,
) : Parcelable
