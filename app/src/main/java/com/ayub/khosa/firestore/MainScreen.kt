package com.ayub.khosa.firestore

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlin.random.Random

@Composable
fun MainScreen(
    modifier: Modifier,
    viewModelfirestore: FireStoreViewModel
) {
    // Collect the StateFlow as a Compose State in a lifecycle-aware manner
    val myuserUiState by viewModelfirestore.uiState.collectAsStateWithLifecycle()
    var user = User(
        name = "test",
        email = "test@gmail.com",
        age = 10 , id = "test"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Button(onClick = {

            viewModelfirestore.insertUser(user)

        }) {
            Text(text = "Insert")
        }

        Spacer(modifier = Modifier.height(50.dp))

        Button(onClick = {
            val randomIntUntil = Random.nextInt(100)
            val randomIntage = Random.nextInt(100)
            val randomIntemail = Random.nextInt(100)



            var user = User(
                name = "new name "+randomIntUntil,
                email = "test"+randomIntemail+"@gmail.com",
                age = randomIntage , id = user.id
            )
            viewModelfirestore.updateUser(user)


        }) {
            Text(text = "Update")
        }

        Spacer(modifier = Modifier.height(50.dp))

        Button(onClick = {

            viewModelfirestore.getUser(user.id)

        }) {
            Text(text = "Get User")
        }

        Button(onClick = {

            viewModelfirestore.getAllUsers()

        }) {
            Text(text = "Get All User")
        }

        Text(text = "User id     -> "+myuserUiState.id )
        Text(text = "User name   -> "+myuserUiState.name)
        Text(text = "User age    -> "+myuserUiState.age)
        Text(text = "User email  -> "+myuserUiState.email)





    }
}


