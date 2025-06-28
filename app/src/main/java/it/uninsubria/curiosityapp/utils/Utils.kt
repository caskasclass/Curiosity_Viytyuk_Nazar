package it.uninsubria.curiosityapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import it.uninsubria.curiosityapp.data.model.CuriosityFact
import it.uninsubria.curiosityapp.data.network.fetchImageUrlForCategory
import java.net.HttpURLConnection
import java.net.URL

fun Context.showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}
fun AppCompatActivity.loadFragment(containerId: Int, fragment: Fragment) {
    supportFragmentManager.beginTransaction()
        .replace(containerId, fragment)
        .addToBackStack(null)
        .commit()
}

fun isEmailValid(email: String): Boolean {
    val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
    return emailRegex.matches(email)
}

fun isUsernameValid(username: String): Boolean {
    val usernameRegex = Regex("^[a-zA-Z0-9_]{3,24}$")
    return usernameRegex.matches(username)
}

fun categorizeFact(text: String): String {
    val lower = text.lowercase()

    val animalKeywords = listOf(
        "animal", "donkey", "koala", "duck", "dog", "cat", "legs", "wings", "tail",
        "elephant", "zebra", "lion", "tiger", "bear", "fish", "whale", "insect", "six legs", "fur"
    )

    val personKeywords = listOf(
        "donald duck", "einstein", "tesla", "elon", "reiser", "gandhi", "president",
        "artist", "singer", "actor", "musician", "scientist", "inventor", "man", "woman", "people"
    )

    val mediaKeywords = listOf(
        "tv", "theme", "show", "series", "film", "movie", "piano", "song", "music", "album", "reiser", "episode"
    )

    val geographyKeywords = listOf(
        "finland", "france", "germany", "italy", "country", "city", "nation", "continent", "europe", "law", "flag", "state"
    )

    return when {
        animalKeywords.any { it in lower } -> "Animali"
        personKeywords.any { it in lower } -> "Persone/Famosi"
        mediaKeywords.any { it in lower } -> "TV/Media"
        geographyKeywords.any { it in lower } -> "Paesi/Geografia"
        else -> "Generiche"
    }


}
fun keywordForCategory(categoria: String): String {
    return when (categoria) {
        "Animali" -> "animal"
        "TV/Media" -> "tv"
        "Paesi/Geografia" -> "world map"
        "Persone/Famosi" -> "famous person"
        else -> "abstract"
    }

}
suspend fun createCuriosityFact(text:String): CuriosityFact {
    val categoria = categorizeFact(text)
    val keywork = keywordForCategory(categoria)
    val imageUrl = fetchImageUrlForCategory(keywork)
    return CuriosityFact(text,categoria,imageUrl)
}
suspend fun urlToBitmap(imageUrl: String): Bitmap? {
    return try {
        val url = URL(imageUrl)
        val connection = url.openConnection() as HttpURLConnection
        connection.doInput = true
        connection.connect()
        val inputStream = connection.inputStream
        BitmapFactory.decodeStream(inputStream)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}


