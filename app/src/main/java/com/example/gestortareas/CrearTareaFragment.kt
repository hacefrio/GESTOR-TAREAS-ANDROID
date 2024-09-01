package com.example.gestortareas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.io.File
import java.util.UUID

class CrearTareasFragment : Fragment() {

    private lateinit var spinner: Spinner
    private var taskId: String? = null
    private var isEditing: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.crear_tarea, container, false)

        spinner = view.findViewById(R.id.spinnerTaskStatus)
        val statusOptions = arrayOf("Completada", "Pendiente", "Eliminada")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, statusOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // Recibir datos de la tarea seleccionada
        arguments?.let {
            taskId = it.getString("task_id")
            view.findViewById<EditText>(R.id.editTextTaskName).setText(it.getString("task_name"))
            view.findViewById<EditText>(R.id.editTextTaskDescription).setText(it.getString("task_description"))
            val taskStatus = it.getString("task_status")
            val statusPosition = adapter.getPosition(taskStatus)
            spinner.setSelection(statusPosition)

            isEditing = true // Indicar que estamos en modo de edición
        }

        val buttonGrabar: Button = view.findViewById(R.id.buttonSaveTask)
        buttonGrabar.setOnClickListener {
            val taskName = view.findViewById<EditText>(R.id.editTextTaskName).text.toString()
            val taskDescription = view.findViewById<EditText>(R.id.editTextTaskDescription).text.toString()
            val taskStatus = spinner.selectedItem.toString()

            // Validación de los campos
            if (taskName.isBlank() || taskDescription.isBlank()) {
                Toast.makeText(requireContext(), "Por favor, ingresa todos los datos.", Toast.LENGTH_SHORT).show()
            } else {
                val success = if (isEditing) {
                    updateTaskInCSV(taskId!!, taskName, taskDescription, taskStatus)
                } else {
                    saveTaskToCSV(taskId ?: UUID.randomUUID().toString(), taskName, taskDescription, taskStatus)
                }

                if (success) {
                    Toast.makeText(requireContext(), "Tarea guardada exitosamente", Toast.LENGTH_SHORT).show()
                    resetFormFields()
                } else {
                    Toast.makeText(requireContext(), "Error al guardar la tarea", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return view
    }

    private fun saveTaskToCSV(taskId: String, taskName: String, taskDescription: String, taskStatus: String): Boolean {
        return try {
            val fileName = "tareas.csv"
            val file = File(requireContext().getExternalFilesDir(null), fileName)

            // Escribir en el archivo CSV
            file.appendText("$taskId,$taskName,$taskDescription,$taskStatus\n")
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun updateTaskInCSV(taskId: String, taskName: String, taskDescription: String, taskStatus: String): Boolean {
        return try {
            val fileName = "tareas.csv"
            val file = File(requireContext().getExternalFilesDir(null), fileName)
            val tempFile = File(requireContext().getExternalFilesDir(null), "tareas_temp.csv")

            if (file.exists()) {
                file.forEachLine { line ->
                    val parts = line.split(",")
                    if (parts[0] == taskId) {
                        // Escribir la tarea actualizada
                        tempFile.appendText("$taskId,$taskName,$taskDescription,$taskStatus\n")
                    } else {
                        // Escribir la tarea sin cambios
                        tempFile.appendText(line + "\n")
                    }
                }

                // Reemplazar el archivo original con el archivo temporal
                if (file.delete()) {
                    tempFile.renameTo(file)
                }
            }

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun resetFormFields() {
        view?.findViewById<EditText>(R.id.editTextTaskName)?.setText("")
        view?.findViewById<EditText>(R.id.editTextTaskDescription)?.setText("")
        spinner.setSelection((spinner.adapter as ArrayAdapter<String>).getPosition("Pendiente"))

        taskId = null
        isEditing = false
    }
}
