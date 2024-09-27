package com.modulo4.videogamedb.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.modulo4.videogamedb.data.db.model.GameEntity
import com.modulo4.videogamedb.util.Constants

@Dao
interface GameDAO {
    //funciones para base de datos
    //Create

    @Insert
    suspend fun insertGame(game: GameEntity)
    @Insert
    suspend fun insertGames(games: MutableList<GameEntity>)

    //Read
    @Query("SELECT * FROM ${Constants.DATABASE_GAME_TABLE}")
    suspend fun getAllGames():MutableList<GameEntity>
    //Update
    @Update
    suspend fun updateGame(game: GameEntity)
    //Delete
    @Delete
    suspend fun delateGame(game: GameEntity)
}