package br.com.voxali.android

import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    private val syncUrl = "https://voxali.com.br/api/sync"

    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val status = TextView(this).apply {
            text = "Voxali\n\nConectando..."
            textSize = 20f
            setPadding(48, 80, 48, 48)
        }
        setContentView(status)

        lifecycleScope.launch {
            while (isActive) {
                status.text = if (sync()) {
                    "Voxali\n\nSincronizado ✓\nAtualização automática: 5 segundos"
                } else {
                    "Voxali\n\nAguardando servidor...\nNova tentativa em 5 segundos"
                }
                delay(5000)
            }
        }
    }

    private fun sync(): Boolean {
        return try {
            val request = Request.Builder()
                .url(syncUrl)
                .get()
                .header("Accept", "application/json")
                .build()

            client.newCall(request).execute().use { it.isSuccessful }
        } catch (_: Exception) {
            false
        }
    }
}
