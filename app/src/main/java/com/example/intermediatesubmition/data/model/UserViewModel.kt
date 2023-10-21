package com.example.intermediatesubmition.data.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.intermediatesubmition.data.database.UserPreferences
import kotlinx.coroutines.launch

class UserViewModel(private val pref: UserPreferences): ViewModel() {
    fun getToken(): LiveData<String>{
        return pref.getToken().asLiveData()
    }

    fun saveToken(token: String){
        viewModelScope.launch {
            pref.saveToken(token)
        }
    }
}