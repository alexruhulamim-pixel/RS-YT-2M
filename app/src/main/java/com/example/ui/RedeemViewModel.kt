package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.RedeemRepository
import com.example.model.RedeemCategory
import com.example.model.RedeemCode
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class RedeemUiState(
    val selectedCategory: RedeemCategory = RedeemCategory.ALL,
    val searchQuery: String = "",
    val isBengali: Boolean = true,
    val hideUsed: Boolean = false,
    val showAddDialog: Boolean = false,
    val showGuideDialog: Boolean = false,
    val isRefreshing: Boolean = false,
    val secondsRemaining: Long = 18450L, // ~5 hours left until next batch reset
    val activeTab: Int = 0, // 0: Codes, 1: Website
    val targetWebUrl: String = "https://reward.ff.garena.com/"
)

class RedeemViewModel(
    private val repository: RedeemRepository = RedeemRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(RedeemUiState())
    val uiState: StateFlow<RedeemUiState> = _uiState.asStateFlow()

    val rawCodes: StateFlow<List<RedeemCode>> = repository.codes

    val filteredCodes: StateFlow<List<RedeemCode>> = combine(
        repository.codes,
        _uiState
    ) { codes, state ->
        codes.filter { code ->
            // Category filter
            val matchesCategory = when (state.selectedCategory) {
                RedeemCategory.ALL -> true
                RedeemCategory.FAVORITES -> code.isFavorite
                else -> code.category == state.selectedCategory
            }
            // Used filter
            val matchesUsed = !state.hideUsed || !code.isUsed
            // Search filter
            val query = state.searchQuery.trim().lowercase()
            val matchesQuery = query.isEmpty() ||
                    code.title.lowercase().contains(query) ||
                    code.titleBn.lowercase().contains(query) ||
                    code.code.lowercase().contains(query) ||
                    code.rewardSummary.lowercase().contains(query)

            matchesCategory && matchesUsed && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalCopiedCount: StateFlow<Int> = repository.codes.combine(_uiState) { codes, _ ->
        codes.sumOf { it.copyCount }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val activeCount: StateFlow<Int> = repository.codes.combine(_uiState) { codes, _ ->
        codes.count { !it.isUsed }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    init {
        // Countdown timer tick
        viewModelScope.launch {
            while (true) {
                delay(1000)
                _uiState.value = _uiState.value.let {
                    val nextSec = if (it.secondsRemaining > 0) it.secondsRemaining - 1 else 86400L
                    it.copy(secondsRemaining = nextSec)
                }
            }
        }
    }

    fun selectCategory(category: RedeemCategory) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    fun updateSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun toggleLanguage() {
        _uiState.value = _uiState.value.copy(isBengali = !_uiState.value.isBengali)
    }

    fun toggleHideUsed() {
        _uiState.value = _uiState.value.copy(hideUsed = !_uiState.value.hideUsed)
    }

    fun setShowAddDialog(show: Boolean) {
        _uiState.value = _uiState.value.copy(showAddDialog = show)
    }

    fun setShowGuideDialog(show: Boolean) {
        _uiState.value = _uiState.value.copy(showGuideDialog = show)
    }

    fun toggleFavorite(id: String) {
        repository.toggleFavorite(id)
    }

    fun toggleUsed(id: String) {
        repository.toggleUsed(id)
    }

    fun onCodeCopied(id: String) {
        repository.recordCopied(id)
    }

    fun refreshCodes() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true)
            delay(600)
            repository.refreshCodes()
            _uiState.value = _uiState.value.copy(isRefreshing = false)
        }
    }

    fun addNewCode(
        title: String,
        code: String,
        category: RedeemCategory,
        reward: String,
        url: String
    ) {
        repository.addCode(title, code, category, reward, url)
        _uiState.value = _uiState.value.copy(showAddDialog = false)
    }

    fun setActiveTab(tab: Int) {
        _uiState.value = _uiState.value.copy(activeTab = tab)
    }

    fun openInWebsite(url: String) {
        _uiState.value = _uiState.value.copy(targetWebUrl = url, activeTab = 1)
    }
}
