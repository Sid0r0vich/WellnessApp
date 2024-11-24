package com.example.wellness.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.wellness.R
import com.example.wellness.auth.AuthData
import com.example.wellness.auth.AuthStatus
import com.example.wellness.auth.MessageNotifier
import com.example.wellness.ui.components.AuthButton
import com.example.wellness.ui.components.AuthEmailField
import com.example.wellness.ui.components.AuthFieldsInvalidation
import com.example.wellness.ui.components.AuthPasswordField
import com.example.wellness.ui.components.Header
import com.example.wellness.ui.viewModels.LoginViewModel
import com.example.wellness.ui.viewModels.getAuth

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onPerformLogin: () -> Unit,
    onRegisterClick: () -> Unit,
) {
    val viewModel: LoginViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                LoginViewModel(getAuth(), onPerformLogin)
            }
        }
    )
    val messageNotifier = MessageNotifier(LocalContext.current)
    val uiState = viewModel.uiState
    AuthFieldsInvalidation(uiState)

    DefaultScreen {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Header(R.string.login)
            Spacer(modifier = Modifier.padding(PaddingValues(12.dp)))
            AuthEmailField(uiState = uiState)
            Spacer(modifier = Modifier.padding(PaddingValues(8.dp)))
            AuthPasswordField(uiState = uiState)
            TextButton(
                onClick = {  },
                modifier = Modifier.align(Alignment.Start)
            ) {
                Text(
                    text = stringResource(R.string.forget_password)
                )
            }
            AuthButton(
                authState = viewModel.authState,
                uiState = uiState,
                textId = R.string.perform_login
            ) {
                viewModel.signIn(
                    AuthData(
                        uiState.email,
                        uiState.password
                    )
                ) { status ->
                    messageNotifier.notifyUser(status)
                    if (status == AuthStatus.SUCCESS) onPerformLogin()
                }
            }
            TextButton(
                onClick = onRegisterClick,
            ) {
                Text(
                    text = stringResource(R.string.have_not_account)
                )
            }
        }
    }
}