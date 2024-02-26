package com.my.contacts.ui.list

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.my.contacts.models.Contact
import com.my.contacts.repositories.ContactsRepository
import com.my.contacts.repositories.RoomRepository
import com.my.contacts.utils.isOnline
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@SuppressLint("StaticFieldLeak")
class ContactsListViewModel @Inject constructor(
    private val contactsRepository: ContactsRepository,
    private val roomRepository: RoomRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _state = MutableLiveData<ViewState>(ViewState.Loading)
    fun state() = _state

    init {
        viewModelScope.launch {
            _state.value = if (isOnline(context).not()) {
                _state.value = ViewState.NoInternetConnection
                ViewState.Data(roomRepository.getContacts())
            } else {
                with(contactsRepository.getContacts()) {
                    val result = body()?.data
                    if (isSuccessful && result != null) {
                        roomRepository.saveContacts(result)
                        ViewState.Data(result)
                    }
                    else {
                        _state.value = ViewState.Error
                        ViewState.Data(roomRepository.getContacts())
                    }
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