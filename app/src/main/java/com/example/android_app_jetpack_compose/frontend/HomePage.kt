package com.example.android_app_jetpack_compose.frontend

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.android_app_jetpack_compose.response.UserResponse
import com.example.android_app_jetpack_compose.service.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Homepage(navController: NavController, context: Context = LocalContext.current) {
    val listUser = remember { mutableStateListOf<UserResponse>() }

    val baseUrl = "http://10.0.2.2:1337/api/"
    val retrofit =
        Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
            .build().create(UserService::class.java)
    val call = retrofit.getData()
    call.enqueue(object : Callback<List<UserResponse>> {
        override fun onResponse(
            call: Call<List<UserResponse>>, response: Response<List<UserResponse>>
        ) {
            if (response.code() == 200) {
                listUser.clear()
                response.body()?.forEach { userResponse ->
                    listUser.add(userResponse)
                }
            } else if (response.code() == 400) {
                print("error login")
                var toast = Toast.makeText(
                    context, "Username atau password salah", Toast.LENGTH_SHORT
                ).show()
            }
        }

        override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {
            print(t.message)
        }

    })
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("createuser")
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        },
        topBar = {
            TopAppBar(
                title = { Text(text = "List User") },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            LazyColumn {
                listUser.forEach { user ->
                    item {
                        Card(modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .background(MaterialTheme.colorScheme.surface)
                            .clickable { /* Handle item click if needed */ }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.primary),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .weight(1f)
                                ) {
                                    Text(
                                        text = user.username,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = user.email,
                                        color = Color.White
                                    )
                                }
                                IconButton(modifier = Modifier.size(48.dp), onClick = {
                                    val retrofit = Retrofit.Builder().baseUrl(baseUrl)
                                        .addConverterFactory(GsonConverterFactory.create()).build()
                                        .create(UserService::class.java)
                                    val call = retrofit.delete(user.id)
                                    call.enqueue(object : Callback<UserResponse> {
                                        override fun onResponse(
                                            call: Call<UserResponse>,
                                            response: Response<UserResponse>
                                        ) {
                                            print(response.code())
                                            if (response.code() == 200) {
                                                listUser.remove(user)
                                            } else if (response.code() == 400) {
                                                print("error login")
                                                var toast = Toast.makeText(
                                                    context,
                                                    "Username atau password salah",
                                                    Toast.LENGTH_SHORT
                                                ).show()

                                            }
                                        }

                                        override fun onFailure(
                                            call: Call<UserResponse>, t: Throwable
                                        ) {
                                            print(t.message)
                                        }

                                    })
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete",
                                        tint = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}