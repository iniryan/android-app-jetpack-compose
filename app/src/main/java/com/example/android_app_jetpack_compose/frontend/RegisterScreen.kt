package com.example.android_app_jetpack_compose.frontend

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.android_app_jetpack_compose.PreferencesManager
import com.example.android_app_jetpack_compose.R
import com.example.android_app_jetpack_compose.data.RegisterData
import com.example.android_app_jetpack_compose.response.LoginResponse
import com.example.android_app_jetpack_compose.service.RegisterService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController, context: Context = LocalContext.current) {
    val preferencesManager = remember { PreferencesManager(context = context) }

    val nameField = remember { mutableStateOf(TextFieldValue("")) }
    val emailField = remember { mutableStateOf(TextFieldValue("")) }
    val passwordField = remember { mutableStateOf(TextFieldValue("")) }
    val passwordVisible = remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Refoodbish",
                style = TextStyle(
                    fontSize = 48.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_medium)),
                    color = Color(0xFF6650a4),
                    textAlign = TextAlign.Left
                ),
                modifier = Modifier.align(Alignment.Start)
            )
            Text(
                text = "Don’t waste your food, help each other",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                    color = Color(0xFF1E1E1E),
                    textAlign = TextAlign.Left
                ),
                modifier = Modifier.align(Alignment.Start)
            )
            Text(
                text = "Username",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                    color = Color(0xFF1E1E1E),
                    textAlign = TextAlign.Center,
                ),
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(top = 14.dp)
            )
            OutlinedTextField(
                value = nameField.value,
                onValueChange = {
                    nameField.value = it
                },
                singleLine = true,
                modifier = Modifier
                    .align(Alignment.Start)
                    .fillMaxWidth()
                    .padding(2.dp)
                    .border(
                        width = 1.5.dp,
                        color = Color(0xFF6650a4),
                        shape = RoundedCornerShape(8.dp)
                    ),
                placeholder = { Text(text = "Contoh: James Doe") }
            )
            Text(
                text = "Email",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                    color = Color(0xFF1E1E1E),
                    textAlign = TextAlign.Center,
                ),
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(top = 14.dp)
            )
            OutlinedTextField(
                value = emailField.value,
                onValueChange = {
                    emailField.value = it
                },
                singleLine = true,
                modifier = Modifier
                    .align(Alignment.Start)
                    .fillMaxWidth()
                    .padding(2.dp)
                    .border(
                        width = 1.5.dp,
                        color = Color(0xFF6650a4),
                        shape = RoundedCornerShape(8.dp)
                    ),
                placeholder = { Text(text = "Contoh: example@test.com") }
            )
            Text(
                text = "Password",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                    color = Color(0xFF1E1E1E),
                    textAlign = TextAlign.Center,
                ),
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(top = 14.dp)
            )
            OutlinedTextField(
                value = passwordField.value,
                onValueChange = {
                    passwordField.value = it
                },
                singleLine = true,
                modifier = Modifier
                    .align(Alignment.Start)
                    .fillMaxWidth()
                    .padding(2.dp)
                    .border(
                        width = 1.5.dp,
                        color = Color(0xFF6650a4),
                        shape = RoundedCornerShape(8.dp)
                    ),
                placeholder = { Text(text = "Masukkan password") },
                visualTransformation =
                if (passwordVisible.value)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(
                        onClick = { passwordVisible.value = !passwordVisible.value },
                        modifier = Modifier.padding(end = 10.dp)
                    ) {
                        Icon(
                            painter =
                            if (passwordVisible.value)
                                painterResource(id = R.drawable.eye_open)
                            else
                                painterResource(id = R.drawable.eye_close),
                            contentDescription = "Toggle Password"
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.padding(10.dp))
            ElevatedButton(
                modifier = Modifier
                    .align(Alignment.Start)
                    .fillMaxWidth()
                    .padding(2.dp)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                ),
                shape = RoundedCornerShape(8.dp),
                onClick = {
                    val baseUrl = "http://10.0.2.2:1337/api/"
                    val retrofit = Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(RegisterService::class.java)
                    val call =
                        retrofit.saveData(RegisterData(emailField.value.text, nameField.value.text, passwordField.value.text))
                    call.enqueue(object : Callback<LoginResponse> {
                        override fun onResponse(
                            call: Call<LoginResponse>,
                            response: Response<LoginResponse>
                        ) {
                            if (response.isSuccessful) {
                                val jwt = response.body()?.jwt
                                preferencesManager.saveData("jwt", jwt.toString())
                                navController.navigate("login")
                            } else {
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            )
            {
                Text(
                    text = "Mulai Berbagi",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_semibold)),
                        color = Color.White,
                        textAlign = TextAlign.Center,
                    )
                )
            }
            Spacer(modifier = Modifier.padding(10.dp))
            Text(
                text = "Dengan mendaftar, kamu telah menyetujui Syarat & Ketentuan serta Kebijakan Privasi Refoodbish.",
                style = TextStyle(
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Center
                )
            )
            Text(
                text = "atau masuk dengan",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                    color = Color(0xFF1E1E1E),
                    textAlign = TextAlign.Center,
                ),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 28.dp)
                    .padding(bottom = 28.dp)
            )
            Row {
                Image(
                    painter = painterResource(id = R.drawable.google),
                    contentDescription = "login with google",
                    contentScale = ContentScale.None,
                    modifier = Modifier
                        .border(width = 0.5.dp, color = Color(0x4D1E1E1E), RoundedCornerShape(8.dp))
                        .padding(0.5.dp)
                        .width(139.dp)
                        .height(56.dp)
                )
                Spacer(modifier = Modifier.width(40.dp))
                Image(
                    painter = painterResource(id = R.drawable.facebook),
                    contentDescription = "login with facebook",
                    contentScale = ContentScale.None,
                    modifier = Modifier
                        .border(width = 0.5.dp, color = Color(0x4D1E1E1E), RoundedCornerShape(8.dp))
                        .padding(0.5.dp)
                        .width(139.dp)
                        .height(56.dp)
                )
            }
            Row {
                Text(
                    text = "Sudah punya akun?",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_regular)),
                        color = Color(0xFF1E1E1E),
                        textAlign = TextAlign.Left
                    ),
                    modifier = Modifier.padding(top = 28.dp)
                )
                Spacer(modifier = Modifier.padding(4.dp))
                ClickableText(
                    text = AnnotatedString("Masuk aja"),
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_medium)),
                        color = Color(0xFF6650a4),
                        textAlign = TextAlign.Left
                    ),
                    modifier = Modifier.padding(top = 28.dp)
                ) {
                    navController.navigate("login")
                }
            }

        }
    }
}