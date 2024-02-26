package com.my.contacts.ui.list

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.my.contacts.models.Contact
import com.my.contacts.repositories.ContactsRepository
import com.my.contacts.utils.isOnline
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@SuppressLint("StaticFieldLeak")
class ContactsListViewModel @Inject constructor(
    private val contactsRepository: ContactsRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _state: MutableLiveData<ViewState> = MutableLiveData(ViewState.Loading)
    fun state() = _state

    init {
        viewModelScope.launch {
            if (isOnline(context).not()) {
                _state.value = ViewState.NoInternetConnection
            } else {
                val result = contactsRepository.getContacts()
                if (result.isSuccessful && result.body()?.data != null) {
                    _state.value = ViewState.Data(result.body()!!.data)
                } else {
                    _state.value = ViewState.Error
                }
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