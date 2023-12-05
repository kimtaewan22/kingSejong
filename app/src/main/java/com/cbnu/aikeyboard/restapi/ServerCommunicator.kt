package com.cbnu.aikeyboard.restapi

import android.os.AsyncTask
import android.util.Log
import org.json.JSONObject
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

object ServerCommunicator {

    private const val SERVER_URL = "http://127.0.0.1:5000/receive-typed-content"  // Use the correct IP address or hostname

    fun sendTypedContent(content: String) {
        SendTypedContentTask().execute(content)
    }

    private class SendTypedContentTask : AsyncTask<String, Void, Void>() {
        override fun doInBackground(vararg params: String): Void? {
            try {
                val content = params[0]

                // Create a JSON object to hold the typed content
                val json = JSONObject()
                json.put("content", content)

                // Create a URL object with the server URL
                val url = URL(SERVER_URL)

                // Open a connection to the server
                val urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.doOutput = true
                urlConnection.requestMethod = "POST"
                urlConnection.setRequestProperty("Content-Type", "application/json")

                // Get the output stream of the connection and write the JSON data
                urlConnection.outputStream.use { outputStream ->
                    outputStream.write(json.toString().toByteArray(Charsets.UTF_8))
                }

                // Get the response code from the server
                val responseCode = urlConnection.responseCode

                // Handle the response code as needed
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    Log.d("ServerCommunicator", "Typed content sent successfully")
                } else {
                    Log.e("ServerCommunicator", "Failed to send typed content. Response code: $responseCode")
                }

                // Disconnect the connection
                urlConnection.disconnect()
            } catch (e: Exception) {
                Log.e("ServerCommunicator", "Exception while sending typed content", e)
            }
            return null
        }
    }
}
