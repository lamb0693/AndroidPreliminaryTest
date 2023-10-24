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
<<<<<<< HEAD
            socket = IO.socket("http://10.100.203.29:3000/counsel", options)
=======
            socket = IO.socket("http://192.168.200.182:3000/counsel", options)
>>>>>>> d85eb58bd459418bf73b5e1cc457f10c424bad24
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

