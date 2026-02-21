package exceptionallybad.nursesync.feature.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import exceptionallybad.nursesync.domain.model.Patient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class DashboardUiState(
    val patients: List<Patient> = emptyList(),
)

class DashboardViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState = _uiState.asStateFlow()
}
