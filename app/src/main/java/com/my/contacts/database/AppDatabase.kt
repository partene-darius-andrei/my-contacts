package com.my.contacts.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.my.contacts.models.Contact

@Database(entities = [Contact::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): ContactsDao
}