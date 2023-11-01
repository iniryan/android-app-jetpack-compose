package com.example.android_app_jetpack_compose.frontend

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.android_app_jetpack_compose.R
import com.example.android_app_jetpack_compose.data.UpdateData
import com.example.android_app_jetpack_compose.response.LoginResponse
import com.example.android_app_jetpack_compose.service.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditUserPage(
    navController: NavController,
    userid: String?,
    usernameParameter: String?,
    emailParameter: String?,
    context: Context = LocalContext.current
) {
    val nameField = remember { mutableStateOf(usernameParameter ?: "") }
    val emailField = remember { mutableStateOf(emailParameter ?: "") }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Edit User", style = TextStyle(
                    fontSize = 48.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_medium)),
                    color = Color(0xFF6650a4),
                    textAlign = TextAlign.Left
                ), modifier = Modifier.align(Alignment.Start)
            )
            Text(
                text = "Update your profile here", style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                    color = Color(0xFF1E1E1E),
                    textAlign = TextAlign.Left
                ), modifier = Modifier.align(Alignment.Start)
            )
            Text(
                text = "Username", style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                    color = Color(0xFF1E1E1E),
                    textAlign = TextAlign.Center,
                ), modifier = Modifier
                    .align(Alignment.Start)
                    .padding(top = 14.dp)
            )
            OutlinedTextField(value = nameField.value,
                onValueChange = {
                    nameField.value = it
                },
                singleLine = true,
                modifier = Modifier
                    .align(Alignment.Start)
                    .fillMaxWidth()
                    .padding(2.dp)
                    .border(
                        width = 1.5.dp, color = Color(0xFF6650a4), shape = RoundedCornerShape(8.dp)
                    ),
                placeholder = { Text(text = "Contoh: James Doe") })
            Text(
                text = "Email", style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                    color = Color(0xFF1E1E1E),
                    textAlign = TextAlign.Center,
                ), modifier = Modifier
                    .align(Alignment.Start)
                    .padding(top = 14.dp)
            )
            OutlinedTextField(value = emailField.value,
                onValueChange = {
                    emailField.value = it
                },
                singleLine = true,
                modifier = Modifier
                    .align(Alignment.Start)
                    .fillMaxWidth()
                    .padding(2.dp)
                    .border(
                        width = 1.5.dp, color = Color(0xFF6650a4), shape = RoundedCornerShape(8.dp)
                    ),
                placeholder = { Text(text = "Contoh: example@test.com") })
            Spacer(modifier = Modifier.padding(10.dp))

            ElevatedButton(modifier = Modifier
                .align(Alignment.Start)
                .fillMaxWidth()
                .padding(2.dp)
                .height(48.dp), colors = ButtonDefaults.buttonColors(
                contentColor = Color.White,
            ), shape = RoundedCornerShape(8.dp), onClick = {
                val baseUrl = "http://10.0.2.2:1337/api/"
                val retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(UserService::class.java)
                val call = retrofit.update(userid, UpdateData(nameField.value, emailField.value))
                call.enqueue(object : Callback<LoginResponse> {
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        print(response.code())
                        if (response.code() == 200) {
                            navController.navigate("homepage")
                        } else if (response.code() == 400) {
                            print("error login")
                            Toast.makeText(
                                context,
                                "Username atau password salah",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        print(t.message)
                    }

                })
            }) {
                Text(
                    text = "Update User", style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_semibold)),
                        color = Color.White,
                        textAlign = TextAlign.Center,
                    )
                )
            }

            Spacer(modifier = Modifier.padding(10.dp))
            ElevatedButton(modifier = Modifier
                .align(Alignment.Start)
                .fillMaxWidth()
                .padding(2.dp)
                .height(48.dp), border = BorderStroke(2.dp, Color(0xFF6650a4)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                ), shape = RoundedCornerShape(8.dp), onClick = {
                    navController.navigate("homepage")
                }) {
                Text(
                    text = "Kembali", style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_semibold)),
                        color = Color(0xFF6650a4),
                        textAlign = TextAlign.Center,
                    )
                )
            }
            Spacer(modifier = Modifier.padding(14.dp))

        }
    }
}