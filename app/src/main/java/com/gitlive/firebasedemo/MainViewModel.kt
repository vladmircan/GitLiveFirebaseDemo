package com.gitlive.firebasedemo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseException
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private val email = "example@gmail.com"
    private val password = "123456"

    val authenticationResult = MutableSharedFlow<AuthenticationResult>()

    fun loginWithCorrectPassword() = viewModelScope.launch(Dispatchers.IO) {
        try {
            Firebase.auth.signInWithEmailAndPassword(
                email = email,
                password = password
            )
            Firebase.auth.signOut()
            authenticationResult.emit(AuthenticationResult.Success)
        } catch (firebaseException: FirebaseException) {
            authenticationResult.emit(AuthenticationResult.Failure(firebaseException))
        }
    }

    fun loginWithIncorrectPassword() = viewModelScope.launch(Dispatchers.IO) {
        try {
            Firebase.auth.signInWithEmailAndPassword(
                email = email,
                password = password.plus("wrong")
            )
        } catch (firebaseException: FirebaseException) {
            authenticationResult.emit(AuthenticationResult.Failure(firebaseException))
        }
    }
}

sealed interface AuthenticationResult {
    object Success: AuthenticationResult

    @JvmInline
    value class Failure(val firebaseException: FirebaseException): AuthenticationResult
}