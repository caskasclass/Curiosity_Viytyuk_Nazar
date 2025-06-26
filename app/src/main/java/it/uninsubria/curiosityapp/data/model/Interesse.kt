package it.uninsubria.curiosityapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "interessi")
data class Interesse (
    @PrimaryKey val nome: String
)

