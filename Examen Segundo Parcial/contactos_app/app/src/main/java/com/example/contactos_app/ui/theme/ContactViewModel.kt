package com.example.contactos_app.ui.theme

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactos_app.data.ContactRepository
import com.example.contactos_app.model.Contact
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class ContactViewModel(private val repository: ContactRepository) : ViewModel() {

    // Lista de contactos observada por la UI
    val contacts = repository.allContacts

    // --- ESTADO DEL FORMULARIO Y DETALLE ---
    var id by mutableStateOf(0)
    var name by mutableStateOf("")
    var phone by mutableStateOf("")
    var email by mutableStateOf("")
    var imagePath by mutableStateOf<String?>(null)
    var isEditing by mutableStateOf(false)

    // Función para limpiar el formulario
    fun resetForm() {
        id = 0
        name = ""
        phone = ""
        email = ""
        imagePath = null
        isEditing = false
    }

    fun isValidEmail(email: String): Boolean {
        if (email.isBlank()) return true
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isNameValid(): Boolean = name.trim().length in 3..25
    fun isPhoneValid(): Boolean = phone.trim().length == 10 && phone.all { it.isDigit() }

    fun canSave(): Boolean = isNameValid() && isPhoneValid() && isValidEmail(email)

    // Carga los datos en el estado del ViewModel
    fun loadContact(contact: Contact) {
        id = contact.id
        name = contact.name
        phone = contact.phone
        email = contact.email
        imagePath = contact.imagePath
        isEditing = false
    }

    // Guarda una copia de la imagen en el almacenamiento interno de la app
    fun saveImageToInternalStorage(context: Context, uri: Uri) {
        viewModelScope.launch {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val file = File(context.filesDir, "contact_${System.currentTimeMillis()}.jpg")
                val outputStream = FileOutputStream(file)
                inputStream?.use { input ->
                    outputStream.use { output ->
                        input.copyTo(output)
                    }
                }
                imagePath = file.absolutePath
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Función Guardar (Crea o Actualiza)
    fun saveContact(onSuccess: () -> Unit) {
        if (!canSave()) return

        val contact = Contact(id = id, name = name, phone = phone, email = email, imagePath = imagePath)

        viewModelScope.launch {
            if (isEditing) {
                repository.update(contact)
            } else {
                repository.insert(contact)
            }
            onSuccess()
        }
    }

    fun deleteContact(contact: Contact, onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.delete(contact)
            resetForm()
            onSuccess()
        }
    }
}
