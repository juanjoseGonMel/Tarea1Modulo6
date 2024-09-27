package com.modulo4.videogamedb.application

import android.app.Application
import com.modulo4.videogamedb.data.GameRepository
import com.modulo4.videogamedb.data.db.GameDatabase

class VideoGamesDBApp: Application() {

    private  val database by lazy {
        GameDatabase.getDatabase(this@VideoGamesDBApp)
    }
    val repository by lazy {
        GameRepository(database.gameDao())
    }

}