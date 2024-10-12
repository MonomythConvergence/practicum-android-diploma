package ru.practicum.android.diploma

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import org.koin.android.ext.android.inject
import ru.practicum.android.diploma.databinding.ActivityRootBinding
import ru.practicum.android.diploma.navigate.api.VacancyApi
import ru.practicum.android.diploma.navigate.observable.VacancyNavigateLiveData
import ru.practicum.android.diploma.navigate.state.NavigateEventState

private const val TAG = "RootActivity"
class RootActivity : AppCompatActivity() {

    private val vacancyNavigateLiveData: VacancyNavigateLiveData by inject()
    private val vacancyApi: VacancyApi<NavigateEventState> by inject()

    private var _binding: ActivityRootBinding? = null
    private val binding: ActivityRootBinding get() = _binding!!

    private val navController: NavController by lazy {
        (supportFragmentManager.findFragmentById(R.id.rootFragments) as NavHostFragment).navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.search_fragment, R.id.team_navigation, R.id.favorites_fragment, R.id.root_navigation,
                ru.practicum.android.diploma.team.R.id.teamFragment,
                -> {
                    binding.bottomNavigationMenu.isVisible = true
                    binding.navigationPanelDivider.isVisible = true
                }

                else -> {
                    binding.bottomNavigationMenu.isVisible = false
                    binding.navigationPanelDivider.isVisible = false
                }
            }
        }

        binding.bottomNavigationMenu.setupWithNavController(navController)

        // Пример использования access token для HeadHunter API
        networkRequestExample(accessToken = BuildConfig.HH_ACCESS_TOKEN)

    }

    override fun onStart() {
        super.onStart()
        vacancyNavigateLiveData.navigateEventState.observe(this) { state ->
            state?.let {
                when (it) {
                    is NavigateEventState.ToVacancyDataSourceNetwork -> {
                        navController.navigate(
                            R.id.action_searchFragment_to_vacancyFragment,
                            vacancyApi.createArgs(it)
                        )
                    }
                    is NavigateEventState.ToVacancyDataSourceDb -> {
                        navController.navigate(
                            R.id.action_favoritesFragment_to_vacancyFragment,
                            vacancyApi.createArgs(it)
                        )
                    }
                    is NavigateEventState.ToFilter -> {
                        navController.navigate(
                            R.id.action_search_fragment_to_filter_navigation
                        )
                    }
                }
            }
        }
    }

    private fun networkRequestExample(accessToken: String) {
        // ...
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
