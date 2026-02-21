package exceptionallybad.nursesync.feature.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import exceptionallybad.nursesync.domain.usecase.auth.LoginUseCase
import exceptionallybad.nursesync.core.util.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val employeeId: String = "",
    val pin: String = "",
    val loginState: UiState<Unit> = UiState.Empty,
)

class AuthViewModel(
    private val loginUseCase: LoginUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    fun onEmployeeIdChanged(id: String) {
        _uiState.value = _uiState.value.copy(employeeId = id)
    }

    fun onPinChanged(pin: String) {
        _uiState.value = _uiState.value.copy(pin = pin)
    }

    fun onLoginClicked() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loginState = UiState.Loading)
            loginUseCase(_uiState.value.employeeId, _uiState.value.pin)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(loginState = UiState.Success(Unit))
                }
                .onFailure {
                    _uiState.value = _uiState.value.copy(loginState = UiState.Error(it.message ?: "Login failed"))
                }
        }
    }
}
