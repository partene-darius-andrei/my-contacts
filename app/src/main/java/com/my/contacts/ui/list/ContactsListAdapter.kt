package com.my.contacts.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.my.contacts.R
import com.my.contacts.databinding.FragmentContactsListItemBinding
import com.my.contacts.models.Contact
import javax.inject.Inject

class ContactsListAdapter @Inject constructor() :
    RecyclerView.Adapter<ContactsListAdapter.ContactViewHolder>() {

    private val _contacts = mutableListOf<Contact>()
    var contacts: List<Contact> = _contacts
        set(value) {
            _contacts.clear()
            _contacts.addAll(value)
            notifyDataSetChanged()
        }

    var listener: ((Contact) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = FragmentContactsListItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(binding)
    }

    override fun getItemCount() = contacts.size

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        with(holder.binding) {
            contacts[position].let {
                Glide
                    .with(root.context)
                    .load(it.avatar)
                    .error(R.drawable.ic_error)
                    .circleCrop()
                    .into(avatar)
                firstName.text = it.firstName
                lastName.text = it.lastName
                root.setOnClickListener { _ -> listener?.invoke(it) }
            }
        }
    }

    inner class ContactViewHolder(val binding: FragmentContactsListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

}
