package com.my.contacts.ui.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.my.contacts.models.Contact
import com.my.contacts.repositories.ContactsRepository
import com.my.contacts.repositories.RoomRepository
import com.my.contacts.utils.NetworkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ContactsListViewModel @Inject constructor(
    private val contactsRepository: ContactsRepository,
    private val roomRepository: RoomRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {

    private val _state = MutableLiveData<ViewState>()
    val state = _state

    fun loadData() {
        viewModelScope.launch {
            _state.value = ViewState.Loading
            _state.value = (if (networkHelper.isOnline()) handleRequest() else handleOffline())
        }
    }

    private suspend fun handleRequest() = try {
        val result = contactsRepository.getContacts()
        roomRepository.saveContacts(result)
        ViewState.Data(result)
    } catch (exception: Exception) {
        Timber.e(exception)
        _state.value = ViewState.Error
        ViewState.Data(roomRepository.getContacts())
    }

    private suspend fun handleOffline(): ViewState {
        _state.value = ViewState.NoInternetConnection
        return ViewState.Data(roomRepository.getContacts())
    }

    sealed interface ViewState {
        data object Loading : ViewState
        data object NoInternetConnection : ViewState
        data object Error : ViewState
        data class Data(val contacts: List<Contact>) : ViewState
    }

}
