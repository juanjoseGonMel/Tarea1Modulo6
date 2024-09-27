package com.modulo4.videogamedb.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.modulo4.videogamedb.util.Constants

@Entity(tableName = Constants.DATABASE_GAME_TABLE)
data class GameEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "pet_id")
    var id: Long = 0,
    @ColumnInfo(name = "pet_name")
    var name: String,
    @ColumnInfo(name = "pet_spice")
    var spice: String,
    @ColumnInfo(name = "pet_description", defaultValue = "Desconocido")
    var description: String
)
