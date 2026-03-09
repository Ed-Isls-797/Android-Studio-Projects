package com.example.contactos_app.ui.theme.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.contactos_app.model.Contact

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactDetailScreen(
    contact: Contact,
    onNavigateBack: () -> Unit,
    onEditClick: (Contact) -> Unit,
    onDeleteClick: () -> Unit
) {
    val context = LocalContext.current
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            containerColor = Color.White,
            dragHandle = { BottomSheetDefaults.DragHandle() }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, bottom = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Eliminar contacto",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "¿Realmente quieres eliminar a ${contact.name}?",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(24.dp))
                
                Button(
                    onClick = {
                        showBottomSheet = false
                        onDeleteClick()
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Eliminar Contacto", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                TextButton(
                    onClick = { showBottomSheet = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancelar", color = Color.Gray, fontSize = 16.sp)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.padding(8.dp).background(Color(0x33FFFFFF), CircleShape)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(
                        onClick = { onEditClick(contact) },
                        modifier = Modifier.padding(8.dp).background(Color(0x33FFFFFF), CircleShape)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.White)
                    }
                    IconButton(
                        onClick = { showBottomSheet = true },
                        modifier = Modifier.padding(8.dp).background(Color(0x33FFFFFF), CircleShape)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.White)
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
                    .padding(top = 8.dp, bottom = 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE8EAF6)),
                    contentAlignment = Alignment.Center
                ) {
                    if (contact.imagePath != null) {
                        AsyncImage(
                            model = contact.imagePath,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Text(
                            text = contact.name.take(1).uppercase(),
                            fontSize = 64.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF3F51B5)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = contact.name,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // Body con traslape
            Box(modifier = Modifier.fillMaxSize()) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                    color = Color.White
                ) {
                    Column(
                        modifier = Modifier.padding(top = 60.dp, start = 24.dp, end = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Tarjeta de Información
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                InfoItem(icon = Icons.Default.Email, label = "Correo Electrónico", value = if(contact.email.isBlank()) "Sin correo" else contact.email)
                                Divider(modifier = Modifier.padding(vertical = 16.dp), color = Color(0xFFF0F0F0))
                                InfoItem(icon = Icons.Default.Call, label = "Teléfono", value = contact.phone)
                            }
                        }
                    }
                }

                // Acciones Rápidas Flotantes (Traslape)
                Row(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .offset(y = (-25).dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    ActionIconButton(icon = Icons.Default.Call, label = "Llamar") {
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${contact.phone}"))
                        context.startActivity(intent)
                    }
                    Spacer(modifier = Modifier.width(25.dp))
                    ActionIconButton(icon = Icons.Default.Email, label = "Correo") {
                        if (contact.email.isNotBlank()) {
                            val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:${contact.email}"))
                            context.startActivity(intent)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ActionIconButton(icon: ImageVector, label: String, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            modifier = Modifier
                .size(55.dp)
                .clickable { onClick() },
            shape = CircleShape,
            color = Color(0xFFE8EAF6),
            shadowElevation = 4.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = null, tint = Color.Black, modifier = Modifier.size(28.dp))
            }
        }
        Text(
            text = label, 
            style = MaterialTheme.typography.labelMedium, 
            color = Color.Gray,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
fun InfoItem(icon: ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier.size(40.dp).background(Color.White, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = Color(0xFF3F51B5), modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            Text(value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = Color.Black)
        }
    }
}
