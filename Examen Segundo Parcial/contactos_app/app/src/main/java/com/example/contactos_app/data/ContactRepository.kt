package com.example.contactos_app.data
import com.example.contactos_app.model.Contact
import kotlinx.coroutines.flow.Flow

class ContactRepository(private val contactDao: ContactDao) {
    val allContacts: Flow<List<Contact>> = contactDao.getAllContacts()

    suspend fun insert(contact: Contact) = contactDao.insertContact(contact)

    suspend fun update(contact: Contact) = contactDao.updateContact(contact)

    suspend fun delete(contact: Contact) = contactDao.deleteContact(contact)

    suspend fun getContactById(id: Int) = contactDao.getContactById(id)
}
