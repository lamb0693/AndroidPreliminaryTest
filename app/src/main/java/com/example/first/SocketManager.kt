package com.example.first

    import io.socket.client.IO
    import io.socket.client.Socket
    import io.socket.emitter.Emitter
    import java.net.URISyntaxException

    class SocketManager private constructor() {

    private var socket: Socket? = null

    init {
        try {
            val options = IO.Options()
            options.path = "/socket.io"
            // Set any additional options here if needed
            socket = IO.socket("http://192.168.200.154:3000/counsel", options)
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }

    companion object {
        private var instance: SocketManager? = null

        fun getInstance(): SocketManager {
            if (instance == null) {
                instance = SocketManager()
            }
            return instance!!
        }
    }

    fun connect() {
        socket?.connect()
    }

    fun disconnect() {
        socket?.disconnect()
    }

    fun sendMessage(message: String) {
        socket?.emit("chat_data", message)
    }

//    fun makeRoom(tel : String, callback :  (String, String) -> Unit) {
//        socket?.emit("create_room", tel, callback )
//    }
    fun makeRoom(tel : String) {
        socket?.emit("create_room", tel)
    }

    fun addEventListener(eventName: String, listener: Emitter.Listener) {
        socket?.on(eventName, listener)
    }

    fun removeEventListener(eventName: String, listener: Emitter.Listener) {
        socket?.off(eventName, listener)
    }

}

