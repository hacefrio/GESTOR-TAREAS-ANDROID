package com.example.gestortareas

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        // Inicialmente, carga el fragmento de ver tareas
        loadFragment(VerTareasFragment())

        // Configura el listener del botón para refrescar con el fragmento de ver tareas
        val buttonVerTareas: Button = findViewById(R.id.buttonVerTareas)
        buttonVerTareas.setOnClickListener {
            loadFragment(VerTareasFragment())
        }

        // Configura el listener del botón para refrescar con el fragmento de crear tareas
        val buttonAgregarTareas: Button = findViewById(R.id.buttonAgregarTareas)
        buttonAgregarTareas.setOnClickListener {
            loadFragment(CrearTareasFragment())
        }
    }

    private fun loadFragment(fragment: androidx.fragment.app.Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.navigationFragmentContainer, fragment)
        fragmentTransaction.commit()
    }
}
