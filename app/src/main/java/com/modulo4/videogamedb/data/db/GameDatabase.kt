package com.modulo4.videogamedb.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.modulo4.videogamedb.data.db.model.GameEntity
import com.modulo4.videogamedb.util.Constants

@Database(
    entities = [GameEntity::class],
    version = 1,
    exportSchema = true
)
abstract class GameDatabase : RoomDatabase(){
    abstract fun gameDao(): GameDAO

    companion object{
        @Volatile
        private var INSTANCE: GameDatabase? = null

        fun getDatabase(context: Context):GameDatabase{


            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GameDatabase::class.java,
                    Constants.DATABASE_NAME
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}