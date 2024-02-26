package com.my.contacts.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.my.contacts.databinding.FragmentContactsListItemBinding
import com.my.contacts.models.Contact
import javax.inject.Inject

class ContactsListAdapter @Inject constructor()
    : RecyclerView.Adapter<ContactsListAdapter.ContactViewHolder>() {

    private val _contacts = mutableListOf<Contact>()
    var contacts: List<Contact> = _contacts
        set(value) {
            _contacts.clear()
            _contacts.addAll(value)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = FragmentContactsListItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(binding)
    }

    override fun getItemCount() = contacts.size

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {

    }

    inner class ContactViewHolder(val binding: FragmentContactsListItemBinding)
        :RecyclerView.ViewHolder(binding.root)

}