package it.uninsubria.curiosityapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "interesseUtente",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["email"],
            childColumns = ["userEmail"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Interesse::class,
            parentColumns = ["nome"],
            childColumns = ["interesseNome"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("userEmail"), Index("interesseNome")]
)
data class InteresseUtente(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userEmail: String,
    val interesseNome: String
)