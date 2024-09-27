package com.modulo4.videogamedb.ui

import androidx.recyclerview.widget.RecyclerView
import com.modulo4.videogamedb.data.db.model.GameEntity
import com.modulo4.videogamedb.databinding.GameElementBinding

class GameViewHolder(
    private  val binding: GameElementBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(game: GameEntity){

        binding.apply {
            tvTitle.text = game.title
            tvGenre.text = game.genre
            tvDeveloper.text = game.developer
        }

    }


}