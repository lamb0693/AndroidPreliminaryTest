package com.example.first

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.PointF
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.first.databinding.ActivityMainBinding
import com.example.first.ui.login.LoginActivity
import com.example.first.ui.login.LoginViewModel
import com.example.first.ui.login.LoginViewModelFactory
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import io.socket.emitter.Emitter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    lateinit var socketManager :SocketManager
    lateinit var binding : ActivityMainBinding

    lateinit var fusedLocationClient : FusedLocationProviderClient

    val curLocation : PointD = PointD()

    val callbackCreateRoom : (String, String) -> Unit = {
        result, roomName -> Log.i("callbackCreateRoom", "$result,$roomName")
    }

    private val onChatMessageReceived = Emitter.Listener { args ->
        runOnUiThread {
            val message = args[0].toString()
            Log.i("onChatMessageReceived>>", args[0].toString())
            binding.tvMessage.text = args[0].toString()

            // Handle the received message here
        }
    }

    private val onRooMInfoMessageReceived = Emitter.Listener { args ->
        runOnUiThread {
            val message = args[0].toString()
            Log.i("onRooMInfoMessageReceived>>", args[0].toString())
            // Handle the received message here
        }
    }

    private val onCreateRoomResultReceived = Emitter.Listener { args ->
        runOnUiThread {
            val message = args[0].toString()
            Log.i("onCreateRoomResultReceived>>", args[0].toString())
            // Handle the received message here
        }
    }

    val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        // xml에 있는 권한 file들 하나하나에 대한 설정값을 체크해서 각각
        if (it.all { permission -> permission.value == true }) {
            Toast.makeText(this, "permission permitted...", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "permission denied...", Toast.LENGTH_SHORT).show()
        }
    }

    fun checkPermission(){
        // permission을 확인하고, 없으면 요청한다
        val status = ContextCompat.checkSelfPermission(this,
            "android.permission.ACCESS_FINE_LOCATION")
        if (status == PackageManager.PERMISSION_GRANTED) {
            Log.d(">>", "Permission Granted")
        } else {
            Log.d(">>", "Permission Denied")
            permissionLauncher.launch(
                arrayOf(
                    "android.permission.ACCESS_FINE_LOCATION"
                )
            )
        }
        //End of request for permission
    }

    private fun getLastKnownLocation() {
        if(ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.ACCESS_FINE_LOCATION)  === PackageManager.PERMISSION_GRANTED){
            fusedLocationClient.lastLocation
                .addOnSuccessListener(this) { location ->
                    if (location != null) {
                        curLocation.x = location.latitude
                        curLocation.y = location.longitude
                        Toast.makeText(
                            this,
                            "Latitude: ${curLocation.x}, Longitude: ${curLocation.y}",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(this, "Location not available", Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkPermission()

        this.socketManager = SocketManager.getInstance()
        socketManager.connect()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
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

        binding.buttonSendMesssage.setOnClickListener{
            getLastKnownLocation()
            socketManager.sendMessage(binding.editChatMessage.text.toString() + curLocation.toString())
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        socketManager.removeEventListener("chat_data", onChatMessageReceived)
        socketManager.removeEventListener("counsel_rooms_info", onRooMInfoMessageReceived)
        socketManager.disconnect()
    }

    inner class PointD {
        var x : Double = 0.0
        var y : Double = 0.0
        override fun toString(): String {
            return "PointD(x=$x, y=$y)"
        }
    }

}