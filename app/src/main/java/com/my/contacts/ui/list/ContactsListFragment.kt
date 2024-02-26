package com.my.contacts.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.my.contacts.R
import com.my.contacts.databinding.FragmentContactsListBinding
import com.my.contacts.models.Contact
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ContactsListFragment : Fragment() {

    private var _binding: FragmentContactsListBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ContactsListViewModel>()

    @Inject
    lateinit var contactsListAdapter: ContactsListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.adapter = contactsListAdapter
        viewModel.state().observe(viewLifecycleOwner) {
            when (it) {
                ContactsListViewModel.ViewState.Loading -> showLoading()
                ContactsListViewModel.ViewState.Error -> showError()
                ContactsListViewModel.ViewState.NoInternetConnection -> showNoInternetConnection()
                is ContactsListViewModel.ViewState.Data -> showData(it.contacts)
            }
        }
    }

    private fun showLoading() {
        binding.loading.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.loading.visibility = View.GONE
    }

    private fun showError() {
        hideLoading()
        showSnackbar(R.string.error)
    }

    private fun showNoInternetConnection() {
        hideLoading()
        showSnackbar(R.string.no_internet_connection)
    }

    private fun showSnackbar(@StringRes message: Int) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun showData(contacts: List<Contact>) {
        hideLoading()
        contactsListAdapter.contacts = contacts
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}