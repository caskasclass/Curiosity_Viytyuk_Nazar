package it.uninsubria.curiosityapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val email: String,
    val username: String,
    val profilePicturePath: String? = null,
    val sliderPreference: Float? = null,
    val knewCount: Int = 0,
    val didntKnowCount: Int = 0
)