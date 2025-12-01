package com.ayub.khosa.firestore.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayub.khosa.firestore.FirestoreClient
import com.ayub.khosa.firestore.data.User
import com.ayub.khosa.firestore.utils.PrintLogs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FireStoreViewModel @Inject constructor(
) : ViewModel() {


    private val _uiState = MutableStateFlow(User())
    val uiState: StateFlow<User> = _uiState.asStateFlow() // Expose as StateFlow


    val firestoreClient = FirestoreClient()
    fun insertUser(insetuser: User) {

        viewModelScope.launch {
            firestoreClient.insertUser(insetuser)
        }

    }


    fun updateUser(user: User) {

        viewModelScope.launch {
            firestoreClient.updateUser(user).collect { result ->
                PrintLogs.printD("FirestoreClient: is updated = $result")
            }
        }

    }

    fun getUser(id: String) {
        viewModelScope.launch {
            firestoreClient.getUser(id).collect { result ->

                if (result != null) {
                    if(result.id == id){
                    PrintLogs.printD("FirestoreClient: get user id = ${result.id}")
                    PrintLogs.printD("FirestoreClient: get user name = ${result.name}")
                    PrintLogs.printD("FirestoreClient: get user email = ${result.email}")
                    PrintLogs.printD("FirestoreClient: get user age = ${result.age}")
                        var user: User = User(result.id, result.name, result.email, result.age)


                        _uiState.update { currentState ->
                            currentState.copy(user.id, name = user.name, email = user.email, age = user.age)
                        }
                    }




                } else {
                    PrintLogs.printD("FirestoreClient: did not get user")
                }

            }


        }
    }

    fun getAllUsers(){


        viewModelScope.launch {
            firestoreClient.getAllUsers().collect { result ->
                val data = kotlin.collections.ArrayList<User>()
                if (result != null) {

                    PrintLogs.printD("FirestoreClient: get user id = ${result.id}")
                    PrintLogs.printD("FirestoreClient: get user name = ${result.name}")
                    PrintLogs.printD("FirestoreClient: get user email = ${result.email}")
                    PrintLogs.printD("FirestoreClient: get user age = ${result.age}")
                    data.add(result)
                } else {
                    PrintLogs.printD("FirestoreClient: did not get user")
                }
            }


        }

    }


}