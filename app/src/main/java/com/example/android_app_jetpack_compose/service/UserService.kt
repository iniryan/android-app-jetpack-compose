package com.example.android_app_jetpack_compose.service

import com.example.android_app_jetpack_compose.data.UpdateData
import com.example.android_app_jetpack_compose.response.LoginResponse
import com.example.android_app_jetpack_compose.response.UserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserService {
    @GET("users")
    fun getData(): Call<List<UserResponse>>

    @DELETE("users/{id}")
    fun delete(@Path("id") id: Int): Call<UserResponse>

    @PUT("users/{id}")
    fun update(@Path("id") id: String?, @Body body: UpdateData): Call<LoginResponse>
}