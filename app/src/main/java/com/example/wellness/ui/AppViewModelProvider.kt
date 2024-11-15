package com.example.wellness.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.wellness.WellnessApplication
import com.example.wellness.auth.FirebaseAuth
import com.example.wellness.data.UserInfoRepository
import com.example.wellness.data.WellnessAppContainer
import com.example.wellness.ui.viewModels.HomeViewModel
import com.example.wellness.ui.viewModels.LoginViewModel
import com.example.wellness.ui.viewModels.ProfileViewModel
import com.example.wellness.ui.viewModels.RegisterViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            HomeViewModel(
                SavedStateHandle(),
                getApplication().container.userInfoRepository
            )
        }

        initializer {
            LoginViewModel(
                getAuth(),
                getUserInfoRepository()
            )
        }

        initializer {
            RegisterViewModel(
                getAuth(),
                getUserInfoRepository()
            )
        }

        initializer {
            ProfileViewModel(
                getAuth(),
                getUserInfoRepository()
            )
        }
    }
}

fun CreationExtras.getApplication(): WellnessApplication =
    (this[APPLICATION_KEY] as WellnessApplication)

fun CreationExtras.getUserInfoRepository(): UserInfoRepository =
    (getApplication().container as WellnessAppContainer).userInfoRepository

fun CreationExtras.getAuth(): FirebaseAuth =
    (getApplication().container as WellnessAppContainer).auth