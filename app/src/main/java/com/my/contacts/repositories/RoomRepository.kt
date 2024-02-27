package com.my.contacts.repositories

import com.my.contacts.database.AppDatabase
import com.my.contacts.models.Contact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RoomRepository @Inject constructor(
    private val appDatabase: AppDatabase
) {

    suspend fun getContacts() = withContext(Dispatchers.IO) {
        appDatabase.userDao().getAll()
    }

    suspend fun saveContacts(contacts: List<Contact>) = withContext(Dispatchers.IO) {
        appDatabase.userDao().insertAll(*contacts.toTypedArray())
    }

}
