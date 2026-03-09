package com.example.contactos_app.ui.theme.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.contactos_app.ui.theme.ContactViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactFormScreen(
    viewModel: ContactViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val title = if (viewModel.isEditing) "Editar Contacto" else "Nuevo Contacto"
    val buttonText = if (viewModel.isEditing) "Actualizar" else "Guardar"

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.saveImageToInternalStorage(context, it) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title, color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF3F51B5))
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF3F51B5))
                .padding(paddingValues)
        ) {
            // Header con Foto y Nombre
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE8EAF6))
                        .clickable { galleryLauncher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    if (viewModel.imagePath != null) {
                        AsyncImage(
                            model = viewModel.imagePath,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            Icons.Default.CameraAlt,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = Color(0xFF3F51B5)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = viewModel.name.ifBlank { "Nombre" },
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // Formulario
            Surface(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                color = Color(0xFFF8F9FE)
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    val nameError = !viewModel.isNameValid()
                    val phoneError = !viewModel.isPhoneValid() || viewModel.isDuplicatePhone
                    val emailError = (!viewModel.isValidEmail(viewModel.email) && viewModel.email.isNotEmpty()) || viewModel.isDuplicateEmail

                    // Campo Nombre
                    OutlinedTextField(
                        value = viewModel.name,
                        onValueChange = { 
                            if (it.length <= 25) {
                                viewModel.name = it
                            }
                        },
                        label = { Text("Nombre") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFF3F51B5)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        isError = nameError,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = if (nameError) Color(0xFFFDECEA) else Color.Transparent,
                            unfocusedContainerColor = if (nameError) Color(0xFFFDECEA) else Color.Transparent,
                            errorContainerColor = Color(0xFFFDECEA),
                            focusedBorderColor = if (nameError) Color(0xFFD32F2F) else Color(0xFF3F51B5),
                            unfocusedBorderColor = if (nameError) Color(0xFFD32F2F) else Color.Gray
                        )
                    )

                    // Campo Correo
                    OutlinedTextField(
                        value = viewModel.email,
                        onValueChange = { 
                            viewModel.email = it
                            viewModel.isDuplicateEmail = false // Reset error on change
                        },
                        label = { Text("Correo") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = Color(0xFF3F51B5)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        isError = emailError,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = if (viewModel.isDuplicateEmail) Color(0xFFFDECEA) else Color.Transparent,
                            unfocusedContainerColor = if (viewModel.isDuplicateEmail) Color(0xFFFDECEA) else Color.Transparent,
                            errorContainerColor = Color(0xFFFDECEA),
                            focusedBorderColor = if (emailError) Color(0xFFD32F2F) else Color(0xFF3F51B5),
                            unfocusedBorderColor = if (emailError) Color(0xFFD32F2F) else Color.Gray
                        ),
                        supportingText = {
                            if (viewModel.isDuplicateEmail) {
                                Text("Este correo ya está en uso")
                            }
                        }
                    )

                    // Campo Teléfono
                    OutlinedTextField(
                        value = viewModel.phone,
                        onValueChange = { 
                            if (it.length <= 10 && it.all { char -> char.isDigit() }) {
                                viewModel.phone = it
                                viewModel.isDuplicatePhone = false // Reset error on change
                            }
                        },
                        label = { Text("Móvil") },
                        leadingIcon = { Icon(Icons.Default.Call, contentDescription = null, tint = Color(0xFF3F51B5)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        isError = phoneError,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = if (phoneError) Color(0xFFFDECEA) else Color.Transparent,
                            unfocusedContainerColor = if (phoneError) Color(0xFFFDECEA) else Color.Transparent,
                            errorContainerColor = Color(0xFFFDECEA),
                            focusedBorderColor = if (phoneError) Color(0xFFD32F2F) else Color(0xFF3F51B5),
                            unfocusedBorderColor = if (phoneError) Color(0xFFD32F2F) else Color.Gray
                        ),
                        supportingText = {
                            if (viewModel.isDuplicatePhone) {
                                Text("Este número ya está en uso")
                            }
                        }
                    )

                    if (nameError || phoneError) {
                        Text(
                            text = "* Nombre y Móvil son obligatorios",
                            color = Color(0xFFD32F2F),
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.align(Alignment.Start).padding(start = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Botón Guardar/Actualizar
                    Button(
                        onClick = { viewModel.saveContact { onNavigateBack() } },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F51B5)),
                        enabled = viewModel.canSave()
                    ) {
                        Text(buttonText, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
