package com.example.first

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.first.databinding.ActivityMainBinding
import com.example.first.ui.login.LoginActivity
import com.example.first.ui.login.LoginViewModel
import com.example.first.ui.login.LoginViewModelFactory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.socket.emitter.Emitter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    lateinit var socketManager :SocketManager

    // kotlin 에서는 callback 함수를 보내면 nodejs가 처리를 못하는 듯
//    val callbackCreateRoom : (String, String) -> Unit = {
//        result, roomName -> Log.i("callbackCreateRoom", "$result,$roomName")
//    }
    // 대신 서버에서 메시지 보내게 처리 하기로 함
    private val onCreateRoomResultReceived = Emitter.Listener { args ->
        runOnUiThread {
            val message = args[0].toString()
            Log.i("onCreateRoomResultReceived>>", args[0].toString())
        }
    }

    private val onChatMessageReceived = Emitter.Listener { args ->
        runOnUiThread {
            val message = args[0].toString()
            Log.i("onChatMessageReceived>>", args[0].toString())
        }
    }

    private val onRooMInfoMessageReceived = Emitter.Listener { args ->
        runOnUiThread {
            val message = args[0].toString()
            Log.i("onRooMInfoMessageReceived>>", args[0].toString())
            var roomArray = ArrayList<RoomInfo>()
            val gson : Gson = Gson()
            roomArray = gson.fromJson(args[0].toString(), object : TypeToken<ArrayList<RoomInfo>>() {}.type)
            for(room in roomArray){
                Log.i("onRooMInfoMessageReceived>>", room.toString())
            }
        }
    }

    inner class RoomInfo{
        var roomName : String? = null
        var roomSize : Int? = null
        override fun toString(): String {
            return "RoomInfo(roomName=$roomName, roomSize=$roomSize)"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.socketManager = SocketManager.getInstance()
        socketManager.connect()


        binding.btnLogin.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        })


        binding.buttonLogin.setOnClickListener(View.OnClickListener{
            Log.i(">>", "clicked")
            socketManager.sendMessage("test");
            socketManager.makeRoom("01031795981")
        });

        socketManager.addEventListener("chat_data", onChatMessageReceived)
        socketManager.addEventListener("counsel_rooms_info", onRooMInfoMessageReceived)
        socketManager.addEventListener("create_room_result", onCreateRoomResultReceived)

    }

    override fun onDestroy() {
        super.onDestroy()
        socketManager.removeEventListener("chat_data", onChatMessageReceived)
        socketManager.removeEventListener("counsel_rooms_info", onRooMInfoMessageReceived)
        socketManager.disconnect()
    }
}