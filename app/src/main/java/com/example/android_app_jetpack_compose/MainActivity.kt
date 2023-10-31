package com.example.android_app_jetpack_compose

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.android_app_jetpack_compose.data.LoginData
import com.example.android_app_jetpack_compose.frontend.CreateUserPage
import com.example.android_app_jetpack_compose.frontend.EditUserPage
import com.example.android_app_jetpack_compose.frontend.Homepage
import com.example.android_app_jetpack_compose.frontend.RegisterScreen
import com.example.android_app_jetpack_compose.response.LoginResponse
import com.example.android_app_jetpack_compose.service.LoginService
import com.example.android_app_jetpack_compose.ui.theme.AndroidappjetpackcomposeTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidappjetpackcomposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val sharedPreferences: SharedPreferences =
                        LocalContext.current.getSharedPreferences("auth", Context.MODE_PRIVATE)
                    val navController = rememberNavController()

                    val startD: String
                    val jwt = sharedPreferences.getString("jwt", "")
                    startD = if (jwt.equals("")) {
                        "login"
                    } else {
                        "homepage"
                    }

                    NavHost(navController = navController, startDestination = startD) {
                        composable("login") {
                            LoginScreen(navController)
                        }
                        composable("homepage") {
                            Homepage(navController)
                        }
                        composable("register") {
                            RegisterScreen(navController)
                        }
                        composable("createuser") {
                            CreateUserPage(navController)
                        }
                        composable(
                            route = "edituserpage/{userid}/{username}/{email}",
                        ) {backStackEntry ->

                            EditUserPage(navController, backStackEntry.arguments?.getString("userid"), backStackEntry.arguments?.getString("username"), backStackEntry.arguments?.getString("email"))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AndroidappjetpackcomposeTheme {
        Greeting("Android")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController, context: Context = LocalContext.current) {
    val preferencesManager = remember { PreferencesManager(context = context) }

    val usernameField = remember { mutableStateOf(TextFieldValue("")) }
    val passwordField = remember { mutableStateOf(TextFieldValue("")) }
    val passwordVisible = remember { mutableStateOf(false) }

    val baseUrl = "http://10.0.2.2:1337/api/"
//    val baseUrl = "http://10.217.17.11:1337/api/" //KALAU TIDAK DI EMULATOR

    var jwt by remember { mutableStateOf("") }
    jwt = preferencesManager.getData("jwt")

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
                text = "Email",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                    color = Color(0xFF1E1E1E),
                    textAlign = TextAlign.Center,
                ),
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(top = 48.dp)
            )
            OutlinedTextField(
                value = usernameField.value,
                onValueChange = {
                    usernameField.value = it
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
                    .padding(top = 28.dp)
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
            Spacer(modifier = Modifier.padding(20.dp))
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
                    val retrofit =
                        Retrofit.Builder()
                            .baseUrl(baseUrl)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()
                            .create(LoginService::class.java)
                    val call = retrofit.getData(
                        LoginData(
                            usernameField.value.text,
                            passwordField.value.text
                        )
                    )
                    call.enqueue(object : Callback<LoginResponse> {
                        override fun onResponse(
                            call: Call<LoginResponse>,
                            response: Response<LoginResponse>
                        ) {
                            print(response.code())
                            if (response.code() == 200) {
                                jwt = response.body()?.jwt!!
                                preferencesManager.saveData("jwt", jwt)
                                navController.navigate("homepage")
                            } else if (response.code() == 400) {
                                print("bad request 400")
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
                    .padding(top = 14.dp)
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
                    text = "Belum punya akun?",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_regular)),
                        color = Color(0xFF1E1E1E),
                        textAlign = TextAlign.Left
                    ),
                    modifier = Modifier.padding(top = 48.dp)
                )
                Spacer(modifier = Modifier.padding(4.dp))
                ClickableText(
                    text = AnnotatedString("Daftar dulu"),
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_medium)),
                        color = Color(0xFF6650a4),
                        textAlign = TextAlign.Left
                    ),
                    modifier = Modifier.padding(top = 48.dp)
                ) {
                    navController.navigate("register")
                }
            }

        }
    }
}
