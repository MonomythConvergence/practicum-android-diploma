package ru.practicum.android.diploma.data.networkclient.api.dto.response.vacancydetail.skill

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class KeySkill(
    val name: String,
) : Parcelable
