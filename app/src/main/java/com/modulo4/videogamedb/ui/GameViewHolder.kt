package com.modulo4.videogamedb.ui

import androidx.recyclerview.widget.RecyclerView
import com.modulo4.videogamedb.R
import com.modulo4.videogamedb.data.db.model.GameEntity
import com.modulo4.videogamedb.databinding.GameElementBinding

class GameViewHolder(
    private  val binding: GameElementBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(game: GameEntity){

        binding.apply {
            tvTitle.text = game.name
            tvGenre.text = game.spice
            tvDeveloper.text = game.description

            val context = binding.root.context
            val spinnerItems = context.resources.getStringArray(R.array.spinner_items)
            val imageResource = when (game.spice) {
                spinnerItems[1] -> R.drawable.perro
                spinnerItems[2] -> R.drawable.gato
                spinnerItems[3] -> R.drawable.hamster
                else -> R.drawable.gamepad
            }
            ivIcon.setImageResource(imageResource)
        }

    }


}