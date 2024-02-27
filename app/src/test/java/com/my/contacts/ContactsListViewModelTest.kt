package com.my.contacts

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.my.contacts.models.Contact
import com.my.contacts.repositories.ContactsRepository
import com.my.contacts.repositories.RoomRepository
import com.my.contacts.ui.list.ContactsListViewModel
import com.my.contacts.utils.NetworkHelper
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verifySequence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.kotlin.never
import java.lang.NullPointerException


@OptIn(ExperimentalCoroutinesApi::class)
class ContactsListViewModelTest {

    private lateinit var contactsListViewModel: ContactsListViewModel

    private lateinit var contactsRepository: ContactsRepository

    private lateinit var roomRepository: RoomRepository

    private lateinit var networkHelper: NetworkHelper

    private lateinit var observer: Observer<ContactsListViewModel.ViewState>

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setup() {
        contactsRepository = Mockito.mock()
        roomRepository = Mockito.mock()
        networkHelper = Mockito.mock()
        observer =
            mockk<Observer<ContactsListViewModel.ViewState>> { every { onChanged(any()) } just Runs }

        contactsListViewModel =
            ContactsListViewModel(contactsRepository, roomRepository, networkHelper)
        contactsListViewModel.state.observeForever(observer)

    }

    @Test
    fun `when offline then check correct states`() =
        runTest {

            val contacts = stubContacts()
            stubOffline()

            verifySequence {
                observer.onChanged(ContactsListViewModel.ViewState.Loading)
                observer.onChanged(ContactsListViewModel.ViewState.NoInternetConnection)
                observer.onChanged(ContactsListViewModel.ViewState.Data(contacts))
            }
        }

    @Test
    fun `when offline then check correct methods invoked`() =
        runTest {

            val contacts = stubContacts()
            stubOffline()

            verify(networkHelper).isOnline()
            verify(roomRepository).getContacts()
            verify(roomRepository, never()).saveContacts(contacts)
            verify(contactsRepository, never()).getContacts()
        }



    @Test
    fun `when request success then check correct states displayed`() =
        runTest {

            val contacts = stubContacts()
            stubRequest()

            verifySequence {
                observer.onChanged(ContactsListViewModel.ViewState.Loading)
                observer.onChanged(ContactsListViewModel.ViewState.Data(contacts))
            }
        }

    @Test
    fun `when request success then check correct methods invoked`() =
        runTest {

            val contacts = stubContacts()
            stubRequest()

            verify(networkHelper).isOnline()
            verify(roomRepository, never()).getContacts()
            verify(roomRepository).saveContacts(contacts)
            verify(contactsRepository).getContacts()
        }

    @Test
    fun `when request fails then show correct states displayed`() =
        runTest {

            val contacts = stubContacts()
            stubError()

            verifySequence {
                observer.onChanged(ContactsListViewModel.ViewState.Loading)
                observer.onChanged(ContactsListViewModel.ViewState.Error)
                observer.onChanged(ContactsListViewModel.ViewState.Data(contacts))
            }
        }

    @Test
    fun `when request fails then check correct methods invoked`() =
        runTest {
            val contacts = stubContacts()
            stubError()

            verify(networkHelper).isOnline()
            verify(roomRepository).getContacts()
            verify(roomRepository, never()).saveContacts(contacts)
            verify(contactsRepository).getContacts()
        }

    private fun stubContacts() = listOf(
        Contact(0, "", "John", "Smith", ""),
        Contact(1, "", "Jane", "Doe", "")
    )

    private suspend fun stubOffline() = runTest {
        val contacts = stubContacts()
        `when`(networkHelper.isOnline()).thenReturn(false)
        `when`(roomRepository.getContacts()).thenReturn(contacts)

        contactsListViewModel.loadData()
        advanceUntilIdle()
    }

    private suspend fun stubRequest() = runTest {
        val contacts = stubContacts()
        `when`(networkHelper.isOnline()).thenReturn(true)
        `when`(contactsRepository.getContacts()).thenReturn(contacts)

        contactsListViewModel.loadData()
        advanceUntilIdle()
    }

    private suspend fun stubError() = runTest {

        val contacts = stubContacts()
        `when`(networkHelper.isOnline()).thenReturn(true)
        `when`(contactsRepository.getContacts()).thenThrow(NullPointerException::class.java)
        `when`(roomRepository.getContacts()).thenReturn(contacts)

        contactsListViewModel.loadData()
        advanceUntilIdle()
    }

}