package ru.practicum.android.diploma.search.presentation.adapter

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.commonutils.Utils
import ru.practicum.android.diploma.commonutils.Utils.formatSalary
import ru.practicum.android.diploma.search.R
import ru.practicum.android.diploma.search.domain.models.Vacancy

private const val RADIUS_ROUND_VIEW = 12f

class VacancyListViewHolder(parent: View) : RecyclerView.ViewHolder(parent) {

    private val vacancyNameAndCity: TextView = itemView.findViewById(R.id.searchVacancyNameAndCity)
    private val vacancyCompany: TextView = itemView.findViewById(R.id.searchVacancyCompany)
    private val vacancySalary: TextView = itemView.findViewById(R.id.searchVacancySalary)
    private val vacancyImage: ImageView = itemView.findViewById(R.id.searchVacancyImage)

    @SuppressLint("SetTextI18n")
    fun bind(model: Vacancy) {
        vacancyNameAndCity.text = model.title + ", " + model.area.name + ""
        vacancyCompany.text = model.companyName
        vacancySalary.text = itemView.context.formatSalary(model.salaryMin, model.salaryMax, model.salaryCurrency)

        Glide.with(itemView).load(model.companyLogo).placeholder(R.drawable.placeholder_logo_item_favorite).transform(
            CenterCrop(), RoundedCorners(Utils.doToPx(RADIUS_ROUND_VIEW, itemView.context.applicationContext))
        ).transform().into(vacancyImage)
    }
}
