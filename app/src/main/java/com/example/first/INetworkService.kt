package com.example.first

import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface INetworkService {
    @Headers("content-type: application/json")
    @POST("/auth/login")
    fun login(@Body params: HashMap<String, String>) : Call<TokenResponse>

    @GET("/")
    fun home() : Call<List<Int>>
}