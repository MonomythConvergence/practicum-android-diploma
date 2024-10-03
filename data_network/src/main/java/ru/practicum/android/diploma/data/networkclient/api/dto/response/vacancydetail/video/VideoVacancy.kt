package ru.practicum.android.diploma.data.networkclient.api.dto.response.vacancydetail.video

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class VideoVacancy(
    @SerializedName("cover_picture")
    val coverPicture: CoverPicture,
    @SerializedName("video_url")
    val videoUrl: String,
) : Parcelable
