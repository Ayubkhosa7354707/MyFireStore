package com.ayub.khosa.firestore


import com.ayub.khosa.firestore.data.User
import com.ayub.khosa.firestore.utils.PrintLogs
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch


class FirestoreClient {

    // private val tag = "FirestoreClient: "

    private val db = FirebaseFirestore.getInstance()
    private val collection = "users"

    fun insertUser(
        user: User
    ): Flow<String?> {
        PrintLogs.printD(" FirestoreClient insertUser  ")
        return callbackFlow {

                db.collection("users").document( user.id).get().addOnSuccessListener {
                        document ->
                    PrintLogs.printD("insert user with id: ${document.id}")
                    PrintLogs.printD("insert user with id: ${document.id}")
                    CoroutineScope(Dispatchers.IO).launch {
                        updateUser(user.copy(id = document.id)).collect {}
                    }

                    trySend(document.id)


                }




                .addOnFailureListener { e ->
                    e.printStackTrace()
                    PrintLogs.printD("error inserting user: ${e.message}")
                    trySend(null)
                }

            awaitClose {}
        }
    }


    fun updateUser(
        user: User
    ): Flow<Boolean> {

        return callbackFlow {
            db.collection(collection)
                .document( user.id)
                .set(user.toHashMap())
                .addOnSuccessListener {
                    PrintLogs.printD("update user with id: ${user.id}")
                    trySend(true)
                }
                .addOnFailureListener { e ->
                    e.printStackTrace()
                    PrintLogs.printD("error updating user: ${e.message}")
                    trySend(false)
                }

            awaitClose {}
        }
    }

    fun getUser(
        id: String
    ): Flow<User?> {
        return callbackFlow {
            db.collection(collection)
                .get()
                .addOnSuccessListener { result ->
                    var user: User? = null

                    for (document in result) {
                        if (document.data["id"] == id) {
                            user = document.data.toUser()
                            PrintLogs.printD("user found: ${user.id}")
                            trySend(user)
                        }
                    }

                    if (user == null) {
                        PrintLogs.printD("user not found: $id")
                        trySend(null)
                    }

                }
                .addOnFailureListener { e ->
                    e.printStackTrace()
                    PrintLogs.printD("error getting user: ${e.message}")
                    trySend(null)
                }

            awaitClose {}
        }
    }

    fun getAllUsers()
            : Flow<User?> {
        return callbackFlow {
            db.collection(collection)
                .get()
                .addOnSuccessListener { result ->
                    var user: User? = null

                    for (document in result) {

                            user = document.data.toUser()
                            PrintLogs.printD("user Name:  "+user.name+" Email : "+user.email+" Age : "+user.age)
                            trySend(user)

                    }



                }
                .addOnFailureListener { e ->
                    e.printStackTrace()
                    PrintLogs.printD("error getting user: ${e.message}")
                    trySend(null)
                }

            awaitClose {}
        }
    }

    private fun User.toHashMap(): HashMap<String, Any> {
        return hashMapOf(
            "id" to id,
            "name" to name,
            "email" to email,
            "age" to age
        )
    }

    private fun Map<String, Any>.toUser(): User {
        return User(
            id = this["id"] as String,
            name = this["name"] as String,
            email = this["email"] as String,
            age = (this["age"] as Long).toInt(),
        )
    }

}