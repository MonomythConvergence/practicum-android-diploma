package ru.practicum.android.diploma.filter.filter.presentation.ui

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.commonutils.Utils.closeKeyBoard
import ru.practicum.android.diploma.filter.R
import ru.practicum.android.diploma.filter.databinding.FragmentFilterBinding
import ru.practicum.android.diploma.filter.filter.domain.model.FilterSettings
import ru.practicum.android.diploma.filter.filter.presentation.viewmodel.FilterViewModel

internal class FilterFragment : Fragment() {

    private val viewModel: FilterViewModel by viewModel()

    private var filterSettings: FilterSettings = emptyFilterSetting()
    private var hasPlace = false
    private var hasProfession = false
    private var hasSalary = false
    private var hasDoNotShowWithoutSalary = false

    private var colorsEditTextFilterEmpty: IntArray? = null
    private var colorsEditTextFilterNoEmpty: IntArray? = null
    private var statesEditTextFilter: Array<IntArray>? = null

    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterBinding.inflate(inflater, container, false)
        initColorEditTextFilter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.filterOptionsListLiveData.observe(viewLifecycleOwner) {
            filterSettings = it
            render(it)
        }

        binding.workPlace.setOnClickListener(listener)
        binding.inputWorkPlaceLayout.setOnClickListener(listener)
        binding.inputWorkPlace.setOnClickListener(listener)
        binding.clickWorkPlace.setOnClickListener(listener)

        binding.workIndustry.setOnClickListener(listener)
        binding.inputWorkIndustryLayout.setOnClickListener(listener)
        binding.inputWorkIndustry.setOnClickListener(listener)
        binding.clickWorkIndustry.setOnClickListener(listener)

        binding.clickWorkPlaceClear.setOnClickListener(listener)
        binding.clickWorkIndustryClear.setOnClickListener(listener)
        binding.buttonBack.setOnClickListener(listener)
        binding.buttonApply.setOnClickListener(listener)
        binding.buttonCancel.setOnClickListener(listener)

        binding.editTextFilter.setOnEditorActionListener(editorActionListener)
        binding.editTextFilter.onFocusChangeListener = onFocusChangeListener

        binding.editTextFilter.addTextChangedListener(inputSearchWatcher)
    }

    private fun initColorEditTextFilter() {

        statesEditTextFilter = arrayOf(
            intArrayOf(-android.R.attr.state_focused),
            intArrayOf(android.R.attr.state_focused)
        )

        colorsEditTextFilterEmpty = intArrayOf(
            ContextCompat.getColor(
                requireContext(),
                ru.practicum.android.diploma.ui.R.color.hintDefaultTextInputLayout
            ),
            ContextCompat.getColor(
                requireContext(),
                ru.practicum.android.diploma.ui.R.color.blue
            )
        )

        colorsEditTextFilterNoEmpty = intArrayOf(
            ContextCompat.getColor(
                requireContext(),
                ru.practicum.android.diploma.ui.R.color.black
            ),
            ContextCompat.getColor(
                requireContext(),
                ru.practicum.android.diploma.ui.R.color.blue
            )
        )
    }

    private val inputSearchWatcher = object : TextWatcher {
        override fun beforeTextChanged(oldText: CharSequence?, p1: Int, p2: Int, p3: Int) {
            val text = oldText.toString()
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        override fun onTextChanged(inputText: CharSequence?, p1: Int, p2: Int, p3: Int) {
            val colors = if(inputText.isNullOrEmpty()) colorsEditTextFilterEmpty else colorsEditTextFilterNoEmpty
            binding.textViewSalary.hintTextColor = ColorStateList(statesEditTextFilter, colors)
            binding.textViewSalary.setDefaultHintTextColor(ColorStateList(statesEditTextFilter, colors))
        }

        override fun afterTextChanged(resultText: Editable?) {
            val text = resultText.toString()
        }
    }

    @SuppressLint("ResourceAsColor")
    private val onFocusChangeListener: OnFocusChangeListener = OnFocusChangeListener { view, b ->

    }

    @SuppressLint("ResourceAsColor")
    private val editorActionListener: OnEditorActionListener = OnEditorActionListener { v, actionId, event ->
        if (actionId == EditorInfo.IME_ACTION_DONE ||
            (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
        ) {
            v.clearFocus()
            requireContext().closeKeyBoard(v)
            true
        } else {
            false
        }
    }

    private fun render(filter: FilterSettings) {
        renderPlaceFilter(filter)
        renderProfessionFilter(filter)
        renderExpectedSalaryFilter(filter)
        renderDoNotShowWithoutSalaryFilter(filter)
    }

    private fun renderDoNotShowWithoutSalaryFilter(filter: FilterSettings) {
        binding.checkBox.isChecked = filter.doNotShowWithoutSalary
        if (binding.checkBox.isChecked) {
            hasDoNotShowWithoutSalary = true
            binding.buttonApply.visibility = View.VISIBLE
            binding.buttonCancel.visibility = View.VISIBLE
        } else {
            renderStateButtonApply()
            hasDoNotShowWithoutSalary = false
        }
    }

    private fun renderExpectedSalaryFilter(filter: FilterSettings) {
        val salary = filter.expectedSalary
        if (salary != null) {
            hasSalary = true
            binding.buttonApply.visibility = View.VISIBLE
            binding.buttonCancel.visibility = View.VISIBLE
            binding.editTextFilter.setText(salary)
        } else {
            hasSalary = false
            renderStateButtonApply()
            binding.editTextFilter.text?.clear()
        }
    }

    private fun renderProfessionFilter(filter: FilterSettings) {
        val profession = filter.branchOfProfession
        if (profession != null) {
            binding.inputWorkIndustry.setText(profession)
            hasProfession = true
            binding.buttonApply.visibility = View.VISIBLE
            binding.buttonCancel.visibility = View.VISIBLE
            binding.clickWorkIndustry.visibility = View.GONE
            binding.clickWorkIndustryClear.visibility = View.VISIBLE
        } else {
            renderProfessionFilterClear()
        }
    }

    private fun renderProfessionFilterClear() {
        hasProfession = false
        renderStateButtonApply()
        binding.inputWorkIndustry.text?.clear()
        binding.clickWorkIndustry.visibility = View.VISIBLE
        binding.clickWorkIndustryClear.visibility = View.GONE
    }

    private fun renderPlaceFilter(filter: FilterSettings) {
        val place = filter.placeSettings
        if (place != null) {
            when {
                place.nameCountry != null && place.nameRegion != null -> {
                    binding.inputWorkPlace.setText(
                        getString(
                            ru.practicum.android.diploma.ui.R.string.filter_place_form,
                            place.nameCountry,
                            place.nameRegion
                        )
                    )
                    hasPlace = true
                    binding.buttonApply.visibility = View.VISIBLE
                    binding.buttonCancel.visibility = View.VISIBLE
                    binding.clickWorkPlace.visibility = View.GONE
                    binding.clickWorkPlaceClear.visibility = View.VISIBLE
                }

                place.nameCountry != null && place.nameRegion == null -> {
                    binding.inputWorkPlace.setText(place.nameCountry)
                    hasPlace = true
                    binding.buttonApply.visibility = View.VISIBLE
                    binding.buttonCancel.visibility = View.VISIBLE
                    binding.clickWorkPlace.visibility = View.GONE
                    binding.clickWorkPlaceClear.visibility = View.VISIBLE
                }

                else -> {
                    renderPlaceFilterClear()
                }
            }
        } else {
            renderPlaceFilterClear()
        }
    }

    private fun renderPlaceFilterClear() {
        hasPlace = false
        renderStateButtonApply()
        binding.inputWorkPlace.text?.clear()
        binding.clickWorkPlace.visibility = View.VISIBLE
        binding.clickWorkPlaceClear.visibility = View.GONE
    }

    private fun renderStateButtonApply() {
        if (shouldRenderButton()) {
            binding.buttonApply.visibility = View.GONE
            binding.buttonCancel.visibility = View.GONE
        }
    }

    private fun shouldRenderButton(): Boolean {
        return !(hasPlace || hasProfession || hasSalary || hasDoNotShowWithoutSalary)
    }

    private fun emptyFilterSetting(): FilterSettings {
        return FilterSettings(
            placeSettings = null,
            branchOfProfession = null,
            expectedSalary = null,
            doNotShowWithoutSalary = false
        )
    }

    private val listener: View.OnClickListener = View.OnClickListener { view ->
        when (view.id) {
            R.id.workPlace, R.id.inputWorkPlaceLayout, R.id.inputWorkPlace, R.id.clickWorkPlace -> {
                findNavController().navigate(R.id.action_filterFragment_to_placeFragment)
            }

            R.id.workIndustry, R.id.inputWorkIndustryLayout, R.id.inputWorkIndustry, R.id.clickWorkIndustry -> {
                findNavController().navigate(R.id.action_filterFragment_to_industryFragment)
            }

            R.id.clickWorkPlaceClear -> {
                renderPlaceFilterClear()
            }

            R.id.clickWorkIndustryClear -> {
                renderProfessionFilterClear()
            }

            R.id.buttonApply -> {
                viewModel.setSalaryInDataFilter(binding.editTextFilter.text.toString())
                viewModel.setDoNotShowWithoutSalaryInDataFilter(binding.checkBox.isChecked)
                if (hasPlace) {
                    viewModel.clearPlaceInDataFilter()
                }
                if (hasProfession) {
                    viewModel.clearProfessionInDataFilter()
                }
                findNavController().navigateUp()
            }

            R.id.buttonBack -> {
                findNavController().navigateUp()
            }

            R.id.buttonCancel -> {

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        inputSearchWatcher.let { binding.editTextFilter.removeTextChangedListener(it) }
        colorsEditTextFilterEmpty = null
        colorsEditTextFilterNoEmpty = null
        statesEditTextFilter = null
    }
}
