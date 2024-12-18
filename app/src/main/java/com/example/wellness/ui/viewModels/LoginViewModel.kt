package com.example.wellness.ui.viewModels

import androidx.lifecycle.ViewModel
import com.example.wellness.auth.AuthData
import com.example.wellness.auth.AuthState
import com.example.wellness.auth.AuthStatus
import com.example.wellness.auth.AuthUiState
import com.example.wellness.auth.FirebaseAuth
import com.example.wellness.utils.DataValidator
import com.example.wellness.utils.toAuthStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val auth: FirebaseAuth,
) : ViewModel() {
    val authState = auth.authState
    val uiState: AuthUiState = AuthUiState()

    fun signIn(authData: AuthData, onComplete: (AuthStatus) -> Unit = {}) {
        DataValidator.validateLoginDataWithStatus(authData)
            .toAuthStatus()
            .also { if (it != AuthStatus.SUCCESS) { onComplete(it); return@signIn } }

        auth.signIn(authData, onComplete)
    }

    fun isAuthenticated(): Boolean = authState == AuthState.Authenticated
}