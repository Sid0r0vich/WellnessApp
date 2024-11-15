package com.example.wellness.auth

import com.example.wellness.data.AuthData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface Auth {
    val authState: AuthState
    val authStateFlow: StateFlow<AuthState>
    val userId: StateFlow<String?>

    fun signIn(authData: AuthData, onSignIn: () -> Unit = {})
    fun signUp(authData: AuthData, onSignUp: () -> Unit = {})
    fun signOut()
}

class FirebaseAuth : Auth {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _authState: MutableStateFlow<AuthState> =
        MutableStateFlow(AuthState.Unauthenticated)
    override val authStateFlow: StateFlow<AuthState>
        get() = _authState
    override var authState: AuthState
        get() = _authState.value ?: AuthState.Loading
        private set(value) {
            _authState.value = value
        }

    private val _userId: MutableStateFlow<String?> =
        MutableStateFlow(firebaseAuth.currentUser?.uid)
    override val userId: StateFlow<String?>
        get() = _userId

    init {
        checkAuthStatus()
        firebaseAuth.addAuthStateListener {
            _userId.value = firebaseAuth.currentUser?.uid
        }
    }

    private fun checkAuthStatus() {
        if (_userId.value == null)
            authState = AuthState.Unauthenticated
        else authState = AuthState.Authenticated
    }

    override fun signIn(
        authData: AuthData,
        onSignIn: () -> Unit
    ) {
        authState = AuthState.Loading
        firebaseAuth.signInWithEmailAndPassword(
            authData.email,
            authData.password
        ).addAuthenticateListener(onSignIn)
    }

    override fun signUp(
        authData: AuthData,
        onSignUp: () -> Unit
    ) {
        authState = AuthState.Loading
        firebaseAuth.createUserWithEmailAndPassword(
            authData.email,
            authData.password
        ).addAuthenticateListener(onSignUp)
    }

    override fun signOut() {
        authState = AuthState.Loading
        firebaseAuth.signOut()
        authState = AuthState.Unauthenticated
    }

    private fun <TResult> Task<TResult>.addAuthenticateListener(
        onAuthenticate: () -> Unit
    ) {
        this.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                authState = AuthState.Authenticated
                onAuthenticate()
            }
            else
                authState = AuthState.Error(
                    task.exception?.message ?: UNKNOWN_EXCEPTION
                )
        }
    }

    companion object {
        private  const val UNKNOWN_EXCEPTION = "Something went wrong"
    }
}