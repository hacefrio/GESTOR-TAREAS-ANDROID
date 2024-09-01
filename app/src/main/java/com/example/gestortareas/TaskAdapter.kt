package com.example.gestortareas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class Task(val id: String, val name: String, val description: String, val status: String)

class TaskAdapter(
    private val tasks: List<Task>,
    private val onItemClick: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.bind(task, onItemClick)
    }

    override fun getItemCount(): Int = tasks.size

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val taskName: TextView = itemView.findViewById(R.id.textViewTaskName)
        private val taskDescription: TextView = itemView.findViewById(R.id.textViewTaskDescription)
        private val taskStatus: TextView = itemView.findViewById(R.id.textViewTaskStatus)


        fun bind(task: Task, onItemClick: (Task) -> Unit) {
            taskName.text = task.name
            taskDescription.text = task.description
            taskStatus.text = task.status

            itemView.setOnClickListener { onItemClick(task) }
        }
    }
}