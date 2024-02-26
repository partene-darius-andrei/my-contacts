package com.my.contacts.ui.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.my.contacts.models.Contact
import com.my.contacts.repositories.ContactsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactsListViewModel @Inject constructor(
    private val contactsRepository: ContactsRepository
) : ViewModel() {

    private val _state: MutableLiveData<ViewState> = MutableLiveData(ViewState.Loading)
    fun state() = _state

    init {
        viewModelScope.launch {
            val result = contactsRepository.getContacts()
            if (result.isSuccessful && result.body()?.data != null) {
                _state.value = ViewState.Data(result.body()!!.data)
            } else {
                _state.value = ViewState.Error
            }
        }
    }

    sealed interface ViewState {
        data object Loading : ViewState
        data object NoInternetConnection : ViewState
        data object Error : ViewState
        data class Data(val contacts: List<Contact>) : ViewState
    }

}