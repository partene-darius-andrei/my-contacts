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
            _state.value = if (isOnline(context).not()) {
                ViewState.NoInternetConnection
            } else {
                with(contactsRepository.getContacts()) {
                    if (isSuccessful && body()?.data != null) ViewState.Data(body()!!.data)
                    else ViewState.Error
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