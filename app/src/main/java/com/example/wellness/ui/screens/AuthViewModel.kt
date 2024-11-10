package com.example.wellness.ui.screens

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth

abstract class AuthViewModel(
    authState: MutableLiveData<AuthState>
) : ViewModel() {
    protected  val auth: FirebaseAuth = FirebaseAuth.getInstance()
    var authState: MutableLiveData<AuthState> = authState
        protected set

    open val uiState: LoginUiState = LoginUiState()

    init {
        checkAuthStatus()
    }

    private fun checkAuthStatus() {
        if (auth.currentUser == null)
            authState.value = AuthState.Unauthenticated
        else authState.value = AuthState.Authenticated
    }

    fun validateEmailFormat(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun validatePasswordFormat(password: String): Boolean {
        return password.length >= 6
    }

    protected  fun validateEmailAndPassword(
        email: String,
        password: String
    ): Boolean {
        if (!validateEmailFormat(email)) {
            authState.value = AuthState.Error(EMAIL_VALIDATION_EXCEPTION)
            return false
        }
        if (!validatePasswordFormat(password)) {
            authState.value = AuthState.Error(PASSWORD_VALIDATION_EXCEPTION)
            return false
        }

        return true
    }

    fun signIn(
        email: String,
        password: String,
    ) {
        authState.value = AuthState.Loading
        if (!validateEmailAndPassword(email, password)) return

        auth.signInWithEmailAndPassword(email, password)
            .addAuthenticateListener()
    }

    protected fun <TResult> Task<TResult>.addAuthenticateListener() {
        this.addOnCompleteListener { task ->
            if (task.isSuccessful)
                authState.value = AuthState.Authenticated
            else
                authState.value = AuthState.Error(
                    task.exception?.message ?: UNKNOWN_EXCEPTION
                )
        }
    }

    companion object {
        protected  const val UNKNOWN_EXCEPTION = "Something went wrong"
        protected  const val EMAIL_VALIDATION_EXCEPTION = "Wrong email!"
        protected  const val PASSWORD_VALIDATION_EXCEPTION = "Short password!"
    }
}

class LoginViewModel(
    authState: MutableLiveData<AuthState>
) : AuthViewModel(authState)

class RegisterViewModel(
    authState: MutableLiveData<AuthState>
) : AuthViewModel(authState) {
    override val uiState: RegisterUiState = RegisterUiState()

    fun signUp(
        email: String,
        password: String,
        sex: Sex,
        age: Int
    ) {
        authState.value = AuthState.Loading
        if (!validateEmailAndPassword(email, password)) return

        auth.createUserWithEmailAndPassword(email, password)
            .addAuthenticateListener()
    }
}

class SignOutViewModel(
    authState: MutableLiveData<AuthState>
) : AuthViewModel(authState) {
    fun signOut() {
        auth.signOut()
        authState.value = AuthState.Unauthenticated
    }
}

sealed class AuthState {
    data object Authenticated: AuthState()
    data object Unauthenticated: AuthState()
    data object Loading: AuthState()
    data class Error(val message: String): AuthState()
}