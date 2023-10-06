package com.example.savannahwonders.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.savannahwonders.data.auth.AuthRepository
import com.example.savannahwonders.data.auth.Resource
import com.example.savannahwonders.data.auth.SignInState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val repository: AuthRepository): ViewModel() {
    val _registerState = Channel<SignInState>()
    var registerState = _registerState.receiveAsFlow()
    fun registerUser(email: String, password: String){
        viewModelScope.launch {
            repository.registerUser(email, password).collect{result ->
                when (result){
                    is Resource.Success -> {
                        _registerState.send(SignInState(isSuccess = "Register Successful"))
                    }
                    is Resource.Loading -> {
                        _registerState.send(SignInState(isLoading = true))
                    }
                    is Resource.Error  -> {
                        _registerState.send(SignInState(isError = result.message))
                    }
                }
            }
        }
    }
    fun logOut(){
        viewModelScope.launch {
            repository.logOut()
        }
    }
}