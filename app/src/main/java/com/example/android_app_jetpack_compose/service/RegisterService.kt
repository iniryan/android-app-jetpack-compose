package com.example.android_app_jetpack_compose.service

import com.example.android_app_jetpack_compose.data.RegisterData
import com.example.android_app_jetpack_compose.response.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
interface RegisterService {
    @POST("auth/local/register")
    fun saveData(@Body body: RegisterData) : Call<LoginResponse>
}