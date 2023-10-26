package com.example.android_app_jetpack_compose.service

import com.example.android_app_jetpack_compose.data.LoginData
import com.example.android_app_jetpack_compose.response.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    @POST("auth/local")
    fun getData(@Body body: LoginData): Call<LoginResponse>
}