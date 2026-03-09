package com.example.contactos_app.ui.theme.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.contactos_app.model.Contact
import com.example.contactos_app.ui.theme.ContactViewModel
import com.example.contactos_app.ui.theme.screens.ContactDetailScreen
import com.example.contactos_app.ui.theme.screens.ContactFormScreen
import com.example.contactos_app.ui.theme.screens.ContactListScreen

@Composable
fun AppNavigation(viewModel: ContactViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "lista") {

        // Pantalla 1: Lista
        composable("lista") {
            ContactListScreen(
                viewModel = viewModel,
                onAddContact = {
                    viewModel.resetForm()
                    navController.navigate("formulario")
                },
                onContactClick = { contact ->
                    viewModel.loadContact(contact)
                    navController.navigate("detalle")
                }
            )
        }

        // Pantalla 2: Detalle
        composable("detalle") {
            // Reconstruimos el contacto actual desde el estado del ViewModel
            val contact = Contact(
                id = viewModel.id,
                name = viewModel.name,
                phone = viewModel.phone,
                email = viewModel.email,
                imagePath = viewModel.imagePath
            )
            
            ContactDetailScreen(
                contact = contact,
                onNavigateBack = { 
                    navController.popBackStack() 
                },
                onEditClick = {
                    viewModel.isEditing = true // Activamos modo edición
                    navController.navigate("formulario")
                },
                onDeleteClick = {
                    viewModel.deleteContact(contact) {
                        navController.popBackStack("lista", inclusive = false)
                    }
                }
            )
        }

        // Pantalla 3: Formulario
        composable("formulario") {
            ContactFormScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
