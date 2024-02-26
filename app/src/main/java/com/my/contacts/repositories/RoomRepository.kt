package com.my.contacts.repositories

import com.my.contacts.database.AppDatabase
import com.my.contacts.models.Contact
import javax.inject.Inject

class RoomRepository @Inject constructor(
    private val appDatabase: AppDatabase
) {

    suspend fun getContacts() = appDatabase.userDao().getAll()

    suspend fun saveContacts(contacts: List<Contact>) = appDatabase.userDao().insertAll(*contacts.toTypedArray())

}