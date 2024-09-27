package ru.practicum.android.diploma.network_client.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VideoVacancy(
    val cover_picture: CoverPicture,
    val video_url: String,
) : Parcelable
