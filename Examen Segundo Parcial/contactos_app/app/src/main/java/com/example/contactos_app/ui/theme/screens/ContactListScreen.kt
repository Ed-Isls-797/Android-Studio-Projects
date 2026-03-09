package com.example.contactos_app.ui.theme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.contactos_app.ui.theme.ContactViewModel
import com.example.contactos_app.ui.theme.components.ContactItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactListScreen(
    viewModel: ContactViewModel,
    onAddContact: () -> Unit,
    onContactClick: (com.example.contactos_app.model.Contact) -> Unit
) {
    val contacts by viewModel.contacts.collectAsState(initial = emptyList())
    var searchQuery by remember { mutableStateOf("") }
    
    // Filtrar contactos según la búsqueda
    val filteredContacts = if (searchQuery.isEmpty()) {
        contacts
    } else {
        contacts.filter { 
            it.name.contains(searchQuery, ignoreCase = true) || 
            it.phone.contains(searchQuery) 
        }
    }
    
    // Agrupar contactos filtrados por la primera letra
    val groupedContacts = filteredContacts.sortedBy { it.name }.groupBy { it.name.first().uppercaseChar() }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .statusBarsPadding()
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Buscar contactos...") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF3F51B5)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFE8EAF6),
                        unfocusedContainerColor = Color(0xFFE8EAF6),
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    singleLine = true
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddContact,
                containerColor = Color(0xFF3F51B5),
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.padding(16.dp).size(64.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar", modifier = Modifier.size(32.dp))
            }
        }
    ) { paddingValues ->
        if (filteredContacts.isEmpty() && searchQuery.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Text("No hay contactos aún.", color = Color.Gray)
            }
        } else if (filteredContacts.isEmpty() && searchQuery.isNotEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Text("No se encontraron resultados.", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color(0xFFF8F9FE)),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                groupedContacts.forEach { (initial, contactsInGroup) ->
                    item {
                        Text(
                            text = initial.toString(),
                            modifier = Modifier
                                .padding(start = 24.dp, top = 16.dp, bottom = 8.dp),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black.copy(alpha = 0.5f)
                        )
                    }
                    items(contactsInGroup) { contact ->
                        ContactItem(
                            contact = contact,
                            onClick = { onContactClick(contact) }
                        )
                    }
                }
            }
        }
    }
}
