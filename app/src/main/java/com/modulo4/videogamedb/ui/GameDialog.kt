package com.modulo4.videogamedb.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.modulo4.videogamedb.R
import com.modulo4.videogamedb.application.VideoGamesDBApp
import com.modulo4.videogamedb.data.GameRepository
import com.modulo4.videogamedb.data.db.model.GameEntity
import com.modulo4.videogamedb.databinding.GameDialogBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class GameDialog(
    private val newGame: Boolean = true,
    private var game : GameEntity = GameEntity(
        title = "",
        genre = "",
        developer = ""
    ),
    private val updateUI: () -> Unit,
    private val message: (String) -> Unit
): DialogFragment() {

    private var _binding: GameDialogBinding? = null
    private val binding get() = _binding!!

    private lateinit var builder: AlertDialog.Builder
    private lateinit var dialog: Dialog

    private var saveButton: Button? = null

    private lateinit var repository: GameRepository

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = GameDialogBinding.inflate(requireActivity().layoutInflater)

        repository = (requireContext().applicationContext as VideoGamesDBApp).repository

        builder = AlertDialog.Builder(requireContext())

        binding.apply {
            tietTitle.setText(game.title)
            tietGenre.setText(game.genre)
            tietDeveloper.setText(game.developer)
        }

        dialog = if(newGame)
            buildDialog("Guardar","Cancelar",{
                binding.apply {
                    game.title = tietTitle.text.toString()
                    game.genre = tietGenre.text.toString()
                    game.developer = tietDeveloper.text.toString()
                }
                try {
                    lifecycleScope.launch(Dispatchers.IO) {
                        val result = async {
                            repository.insertGame(game)
                        }
                        result.await()
                        withContext(Dispatchers.Main){
                            message("Juego guardado exitosamente")
                            updateUI()
                        }
                    }


                }catch (e: IOException){
                    message("Error al guardad el juego")
                }
            },{
                //Cancelar
            })
        else
            buildDialog("Actualizar","Borrar",{
                binding.apply {
                    game.title = tietTitle.text.toString()
                    game.genre = tietGenre.text.toString()
                    game.developer = tietDeveloper.text.toString()
                }
                try {

                    lifecycleScope.launch(Dispatchers.IO) {
                        val result = async {
                            repository.updateGame(game)
                        }
                        result.await()
                        withContext(Dispatchers.Main){
                            message("Juego actualizar exitosamente")
                            updateUI()
                        }
                    }



                }catch (e: IOException){
                    message("Error al actualizar el juego")
                }
            },{

                AlertDialog.Builder(requireContext())
                    .setTitle("Confirmacion")
                    .setMessage("¿Realmente desea eliminar el juego ${game.title}?")
                    .setPositiveButton("Aceptar"){_,_ ->

                        try {
                            lifecycleScope.launch(Dispatchers.IO) {
                                val result = async {
                                    repository.delateGame(game)
                                }
                                result.await()
                                withContext(Dispatchers.Main){
                                    message("Juego borrado exitosamente")
                                    updateUI()
                                }
                            }
                        }
                        catch (e: IOException){
                            message("Error al actualizar el juego")
                        }


                    }
                    .setNegativeButton("Cancelar"){dialog,_ ->
                        dialog.dismiss()
                    }
                    .create().show()

            })

       /* dialog = builder.setView(binding.root)
            .setTitle(getString(R.string.title))
            .setPositiveButton("Guardar", DialogInterface.OnClickListener{ _, _ ->
                binding.apply {
                    game.title = tietTitle.text.toString()
                    game.genre = tietGenre.text.toString()
                    game.developer = tietDeveloper.text.toString()
                }
                try {
                    lifecycleScope.launch(Dispatchers.IO) {
                        repository.insertGame(game)
                    }
                    Toast.makeText(requireContext(),
                        "Juego guardado exitosamente",
                        Toast.LENGTH_SHORT).show()

                    updateUI()

                }catch (e: IOException){
                    Toast.makeText(requireContext(),
                        "Error al guardad el juego",
                        Toast.LENGTH_SHORT).show()
                }
            })
            .setNegativeButton("Cancelar"){_, _ ->

            }
            .create()*/
        return dialog
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onStart() {
        super.onStart()

        val alertDialog = dialog as AlertDialog
        saveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        saveButton?.isEnabled = false

        binding.tietTitle.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }

        })

        binding.tietGenre.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }

        })

        binding.tietDeveloper.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }

        })

    }

    private fun validateFields(): Boolean
        = binding.tietTitle.text.toString().isNotEmpty() &&
            binding.tietGenre.text.toString().isNotEmpty() &&
            binding.tietDeveloper.text.toString().isNotEmpty()

    private fun buildDialog(
        btn1Text: String,
        btn2Text: String,
        positiveButton: () -> Unit,
        negativeButton: () -> Unit
    ): Dialog =
        builder.setView(binding.root)
            .setTitle(R.string.title)
            .setPositiveButton(btn1Text){ _, _ ->
                //Acción para el botón positivo
                positiveButton()
            }.setNegativeButton(btn2Text){ _, _ ->
                //Acción para el botón negativo
                negativeButton()
            }
            .create()
}