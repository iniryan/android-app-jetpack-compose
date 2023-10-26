package com.example.android_app_jetpack_compose.response

import com.google.gson.annotations.SerializedName

class LoginResponse {
    @SerializedName("jwt")
    var jwt: String = ""
}