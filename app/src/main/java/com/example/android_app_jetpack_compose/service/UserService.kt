package com.example.android_app_jetpack_compose.service

import com.example.android_app_jetpack_compose.data.RegisterData
import com.example.android_app_jetpack_compose.response.UserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

interface UserService {
    @GET("users")
    fun getData(): Call<List<UserResponse>>

    @DELETE("users/{id}")
    fun delete(@Path("id") id: Int): Call<UserResponse>
}