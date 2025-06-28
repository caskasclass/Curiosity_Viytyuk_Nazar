package it.uninsubria.curiosityapp.data.network

import android.util.Log
import com.google.gson.Gson
import it.uninsubria.curiosityapp.data.model.CuriosityFact
import it.uninsubria.curiosityapp.data.model.CuriosityTextRequester
import it.uninsubria.curiosityapp.utils.defaultImageUrl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

suspend fun fetchCuriosityFact(): CuriosityTextRequester? {
    return withContext(Dispatchers.IO) {
        try {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url("https://uselessfacts.jsph.pl/api/v2/facts/random?language=en")
                .build()

            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val body = response.body?.string()
                body?.let {
                    return@withContext Gson().fromJson(it, CuriosityTextRequester::class.java)
                }
            }
            null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
suspend fun fetchImageUrlForCategory(keyword: String): String {
    return try {
        val response = RetrofitInstance.api.searchPhotos(keyword)
        response.results.randomOrNull()?.urls?.regular ?: defaultImageUrl
    } catch (e: Exception) {
        Log.e("Unsplash", "Errore: ${e.message}")
        defaultImageUrl
    }
}