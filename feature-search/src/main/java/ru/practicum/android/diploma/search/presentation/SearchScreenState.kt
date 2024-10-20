package ru.practicum.android.diploma.search.presentation

internal sealed class SearchScreenState {
    data object IDLE : SearchScreenState()

    data object VACANCY_LIST_LOADED : SearchScreenState()

    data object LOADING_NEW_LIST : SearchScreenState()

    data object LOADING_NEW_PAGE : SearchScreenState()

    sealed class Error : SearchScreenState() {
        object SERVER_ERROR : Error()
        object NO_INTERNET_ERROR : Error()
        object FAILED_TO_FETCH_VACANCIES_ERROR : Error()
        object NEW_PAGE_SERVER_ERROR : Error()
        object NEW_PAGE_NO_INTERNET_ERROR : Error()
    }
}
