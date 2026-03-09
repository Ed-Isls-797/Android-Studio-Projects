package com.example.contactos_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.contactos_app.data.ContactDatabase
import com.example.contactos_app.data.ContactRepository
import com.example.contactos_app.ui.theme.ContactViewModel
import com.example.contactos_app.ui.theme.Contactos_appTheme
import com.example.contactos_app.ui.theme.navigation.AppNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicialización manual (Para un proyecto real usarías Hilt o Koin)
        val db = ContactDatabase.getDatabase(this)
        val repository = ContactRepository(db.contactDao())
        val viewModel = ContactViewModel(repository)

        setContent {
            MaterialTheme {
                AppNavigation(viewModel)
            }
        }
    }
}