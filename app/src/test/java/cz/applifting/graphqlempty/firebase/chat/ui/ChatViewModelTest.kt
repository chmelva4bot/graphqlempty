package cz.applifting.graphqlempty.firebase.chat.ui

import cz.applifting.graphqlempty.firebase.chat.data.DisplayChatUseCase
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ChatViewModelTest {

    @Test
    fun `properly checks fo user login`(): Unit = runTest {

        Dispatchers.setMain(StandardTestDispatcher())

        val vm = ChatViewModel(mockk(), mockk(), mockk())

        assertFalse("User not checked at start", vm.state.value.isUserChecked)

        vm.sendAction(ChatAction.CheckUser)
        advanceUntilIdle()

        assertTrue("User checked after action", vm.state.value.isUserChecked)
        Dispatchers.resetMain()
    }

}