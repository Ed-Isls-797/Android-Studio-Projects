package com.example.contactos_app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.contactos_app.model.Contact
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Contact::class], version = 1, exportSchema = false)
abstract class ContactDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao

    companion object {
        @Volatile
        private var INSTANCE: ContactDatabase? = null

        fun getDatabase(context: Context): ContactDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ContactDatabase::class.java,
                    "contact_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(ContactDatabaseCallback(context))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class ContactDatabaseCallback(
            private val context: Context
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // Usamos un Scope para insertar los datos de forma asíncrona
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        val dao = database.contactDao()
                        // Insertar contactos de prueba iniciales
                        dao.insertContact(Contact(name = "Juan Pérez", phone = "5551234", email = "juan@mail.com"))
                        dao.insertContact(Contact(name = "María García", phone = "5555678", email = "maria@mail.com"))
                        dao.insertContact(Contact(name = "Desarrollador Gemini", phone = "9990000", email = "ai@google.com"))
                    }
                }
            }
        }
    }
}
