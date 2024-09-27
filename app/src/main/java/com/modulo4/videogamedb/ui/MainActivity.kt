package com.modulo4.videogamedb.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.modulo4.videogamedb.R
import com.modulo4.videogamedb.application.VideoGamesDBApp
import com.modulo4.videogamedb.data.GameRepository
import com.modulo4.videogamedb.data.db.model.GameEntity
import com.modulo4.videogamedb.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    private var games: MutableList<GameEntity> = mutableListOf()
    private lateinit var repository: GameRepository

    private lateinit var gameAdapter: GameAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repository = (application as VideoGamesDBApp).repository

        gameAdapter = GameAdapter{selectedgame ->

            val dialog = GameDialog(
                newGame = false,
                game = selectedgame,
                updateUI = {updateUI()},
                {text ->
                    messageUI(text)
                })
            dialog.show(supportFragmentManager,"dialog2")


        }

        binding.rvGames.layoutManager = LinearLayoutManager(this)
        binding.rvGames.adapter = gameAdapter

        updateUI()

    }

    fun click(view: View) {
        val dialog = GameDialog(
            newGame = true,
            updateUI = {updateUI()},
            message = { text ->
                messageUI(text)
            }
        )
        dialog.show(supportFragmentManager, getString(R.string.btnsave))
    }

    private fun messageUI(text: String){

        Snackbar.make(
            binding.cl,
            text,
            Snackbar.LENGTH_SHORT
        )
            .setTextColor(getColor(R.color.white))
            .setBackgroundTint(getColor(R.color.snackbar))
            .show()
    }

    private fun updateUI(){
        lifecycleScope.launch {
            games = repository.getAllGames()
            binding.tvSinRegistros.visibility =
                if(games.isNotEmpty()) View.INVISIBLE else View.VISIBLE
            gameAdapter.updateList(games)
        }
    }
}