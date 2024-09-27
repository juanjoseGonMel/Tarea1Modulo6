package com.modulo4.videogamedb.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.modulo4.videogamedb.data.db.model.GameEntity
import com.modulo4.videogamedb.databinding.GameElementBinding

class GameAdapter(
    private val onGameClicked: (GameEntity) -> Unit
): RecyclerView.Adapter<GameViewHolder>() {

    private var games: MutableList<GameEntity> = mutableListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val binding = GameElementBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return  GameViewHolder(binding)
    }

    override fun getItemCount(): Int = games.size

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = games[position]
        holder.bind(game)
        //CLCK
        holder.itemView.setOnClickListener{
            onGameClicked(game)
        }
    }

    fun updateList(list:MutableList<GameEntity>){
        games = list
        notifyDataSetChanged() //DiffUtill
    }


}