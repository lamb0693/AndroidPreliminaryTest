package com.example.first.data

import android.util.Log
import com.example.first.INetworkService
import com.example.first.TokenResponse
import com.example.first.data.model.LoggedInUser
import com.example.first.ui.login.LoginViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.lang.Exception

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(username: String, password: String): Result<LoggedInUser> {

        try {
            val retrofit : Retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.200.157:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            var networkService : INetworkService = retrofit.create(INetworkService::class.java)
            Log.i("networkservice" ,networkService.toString())

            val paramMap  = HashMap<String, String>()
            paramMap["tel"] = username
            paramMap["password"] = password
            val loginCall = networkService.login(paramMap)
            //val homeCall = networkService.home()

            loginCall.enqueue(object : Callback<TokenResponse> {
                override fun onResponse(
                    call: Call<TokenResponse>,
                    response: Response<TokenResponse>
                ) {
                    Log.i(">>", "success")
                    var result = response.body()
                    Log.i(">>", "accessToken : ${result?.accessToken})")
                    Log.i(">>", "refreshToken : ${result?.refreshToken}")
                }

                override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                    Log.i(">>", "fail")
                }

            });

            // TODO: handle loggedInUser authentication
            val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), "Jane Doe")

            return Result.Success(fakeUser)
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}