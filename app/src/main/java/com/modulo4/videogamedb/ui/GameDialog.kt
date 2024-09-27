package com.modulo4.videogamedb.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
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
        name = "",
        spice = "",
        description = ""
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


        val dato = resources.getStringArray(R.array.spinner_items).toMutableList()
        val adapter = object : ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            dato
        ){
            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent) as TextView
                if (position == 0) {
                    view.visibility = View.INVISIBLE
                    view.layoutParams = AbsListView.LayoutParams(0, 0)
                } else {
                    view.visibility = View.VISIBLE
                    view.layoutParams = AbsListView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                }
                return view
            }
        }
            .also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        binding.spinnerSpice.adapter = adapter


        if (game.spice.isNotEmpty()) {
            val spinnerItems = resources.getStringArray(R.array.spinner_items)
            val defaultIndex = spinnerItems.indexOf(game.spice)
            if (defaultIndex != -1) {
                binding.spinnerSpice.setSelection(defaultIndex)
            }
        }

        binding.apply {
            tietTitle.setText(game.name)
            tietDeveloper.setText(game.description)
        }



        dialog = if(newGame)
            buildDialog(getString(R.string.btnsave), getString(R.string.btncancel),{
                binding.apply {
                    game.name = tietTitle.text.toString()
                    game.spice = spinnerSpice.selectedItem.toString()
                    game.description = tietDeveloper.text.toString()
                }
                try {
                    lifecycleScope.launch(Dispatchers.IO) {
                        val result = async {
                            repository.insertGame(game)
                        }
                        result.await()
                        withContext(Dispatchers.Main){
                            message(getString(R.string.pet_saved_successfully))
                            updateUI()
                        }
                    }

                }catch (e: IOException){
                    message(getString(R.string.pet_error_save))
                }
            },{
                //Cancelar
            })
        else
            buildDialog(getString(R.string.btnupdate), getString(R.string.btndelate),{
                binding.apply {
                    game.name = tietTitle.text.toString()
                    game.spice = spinnerSpice.selectedItem.toString()
                    game.description = tietDeveloper.text.toString()
                }
                try {

                    lifecycleScope.launch(Dispatchers.IO) {
                        val result = async {
                            repository.updateGame(game)
                        }
                        result.await()
                        withContext(Dispatchers.Main){
                            message(getString(R.string.pet_update_successfully))
                            updateUI()
                        }
                    }



                }catch (e: IOException){
                    message(getString(R.string.pet_error_update))
                }
            },{

                val context = requireContext()
                AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.dialog_title_confirmation))
                    .setMessage(getString(R.string.dialog_message_delete_pet, game.name))
                    .setPositiveButton(getString(R.string.btnaceptar)){_,_ ->

                        try {
                            lifecycleScope.launch(Dispatchers.IO) {
                                val result = async {
                                    repository.delateGame(game)
                                }
                                result.await()
                                withContext(Dispatchers.Main){
                                    message(context.getString(R.string.pet_deleted_successfully))
                                    updateUI()
                                }
                            }
                        }
                        catch (e: IOException){
                            message(context.getString(R.string.pet_error_deleted))
                        }


                    }
                    .setNegativeButton(getString(R.string.btncancel)){dialog,_ ->
                        dialog.dismiss()
                    }
                    .create().show()



            })

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

        binding.spinnerSpice.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                //val selectedItem = parent.getItemAtPosition(position).toString()
                saveButton?.isEnabled = validateFields()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                saveButton?.isEnabled = validateFields()
            }
        }


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

    private fun validateFields(): Boolean{

        val isSpinnerItemSelected = binding.spinnerSpice.selectedItemPosition != 0

        return binding.tietTitle.text.toString().isNotEmpty() &&
                isSpinnerItemSelected &&
                binding.tietDeveloper.text.toString().isNotEmpty()
    }

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