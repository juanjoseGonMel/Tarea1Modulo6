package com.modulo4.videogamedb.data

import com.modulo4.videogamedb.data.db.GameDAO
import com.modulo4.videogamedb.data.db.model.GameEntity

class GameRepository(private val gameDao : GameDAO){
    suspend fun insertGame(game: GameEntity){
        gameDao.insertGame(game)
    }
    suspend fun getAllGames():MutableList<GameEntity> = gameDao.getAllGames()

    suspend fun updateGame(game: GameEntity){
        gameDao.updateGame(game)
    }
    suspend fun delateGame(game: GameEntity){
        gameDao.delateGame(game)
    }
}
