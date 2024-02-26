package com.my.contacts.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

//TODO break into 3: ContactViewModel, ContactRoomModel, ContactResponseModel
@Entity
data class Contact(
    @PrimaryKey
    val id: Int,
    val email: String,
    @ColumnInfo(name = "first_name")
    @SerializedName("first_name")
    val firstName: String,
    @ColumnInfo(name = "last_name")
    @SerializedName("last_name")
    val lastName: String,
    val avatar: String
): Serializable