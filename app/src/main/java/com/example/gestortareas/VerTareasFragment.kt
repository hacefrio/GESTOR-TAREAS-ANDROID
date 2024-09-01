package com.example.gestortareas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class VerTareasFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.ver_tareas, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewTasks)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Lee las tareas desde el archivo CSV
        val tasks = readTasksFromCSV()

        taskAdapter = TaskAdapter(tasks) { task ->
            // Muestra un mensaje al seleccionar una tarea
            Toast.makeText(requireContext(), "Tarea seleccionada: ${task.name} (ID: ${task.id})", Toast.LENGTH_SHORT).show()

            // Cargar el fragment de CrearTareas con los datos de la tarea seleccionada
            val bundle = Bundle().apply {
                putString("task_id", task.id)
                putString("task_name", task.name)
                putString("task_description", task.description)
                putString("task_status", task.status)
            }

            val fragment = CrearTareasFragment().apply {
                arguments = bundle
            }

            // Reemplazar el fragmento utilizando FragmentTransaction
            parentFragmentManager.beginTransaction()
                .replace(R.id.navigationFragmentContainer, fragment)
                .addToBackStack(null)
                .commit()
        }

        recyclerView.adapter = taskAdapter

        return view
    }

    private fun readTasksFromCSV(): List<Task> {
        val fileName = "tareas.csv"
        val file = File(requireContext().getExternalFilesDir(null), fileName)
        val taskList = mutableListOf<Task>()

        if (file.exists()) {
            file.forEachLine { line ->
                val parts = line.split(",")
                if (parts.size == 4) {  // Ajustado para leer el ID también
                    val task = Task(parts[0], parts[1], parts[2], parts[3])
                    taskList.add(task)
                }
            }
        }

        return taskList
    }
}
