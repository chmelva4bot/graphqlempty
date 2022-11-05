package cz.applifting.graphqlempty.firebase.chat.ui

import android.net.Uri
import app.cash.turbine.test
import cz.applifting.graphqlempty.ImmediateTestDispatcher
import cz.applifting.graphqlempty.firebase.auth.BasicUser
import cz.applifting.graphqlempty.firebase.auth.GetCurrentUserUseCaseFake
import cz.applifting.graphqlempty.firebase.chat.data.DisplayChatUseCase
import cz.applifting.graphqlempty.firebase.chat.data.MessageRepositoryFake
import cz.applifting.graphqlempty.firebase.chat.data.SendMessageUseCase
import cz.applifting.graphqlempty.firebase.chat.data.UploadImageUseCaseFake
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ChatViewModelTest {

    @Test
    fun `properly checks for user login`(): Unit = runTest {

        Dispatchers.setMain(UnconfinedTestDispatcher())
        val fakeAuth = GetCurrentUserUseCaseFake()

        val vm = ChatViewModel(fakeAuth ,mockk(), mockk(), mockk())
        val collectJob = launch(UnconfinedTestDispatcher()) { vm.state.collect() {} }
        assertFalse("User not checked at start", vm.state.value.isUserChecked)
        fakeAuth.userFlow.emit(null)

        assertTrue("User checked after action", vm.state.value.isUserChecked)
        assertNull("User is null after action", vm.state.value.user)

        collectJob.cancel()
        Dispatchers.resetMain()
    }
    @Test
    fun `properly stores user in state`(): Unit = runTest {

        Dispatchers.setMain(UnconfinedTestDispatcher())
        val fakeAuth = GetCurrentUserUseCaseFake()
        val fakeRepo = MessageRepositoryFake()

        val vm = ChatViewModel(fakeAuth, DisplayChatUseCase(fakeRepo), mockk(), mockk())
        val collectJob = launch(UnconfinedTestDispatcher()) { vm.state.collect() {} }
        fakeAuth.userFlow.emit(null)

        assertNull("User is null after action", vm.state.value.user)

        val user = BasicUser("uid", "name1", "photo1")
        fakeAuth.userFlow.emit(user)

        assertEquals(user.uid, vm.state.value.user?.uid)
        assertEquals(user.displayName, vm.state.value.user?.displayName)
        assertEquals(user.photoUrl, vm.state.value.user?.photoUrl)

        collectJob.cancel()
        Dispatchers.resetMain()
    }


    @Test
    fun `properly stores msg text in state`(): Unit = runTest {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        val fakeAuth = GetCurrentUserUseCaseFake()
        val fakeRepo = MessageRepositoryFake()

        val vm = ChatViewModel(fakeAuth, DisplayChatUseCase(fakeRepo), SendMessageUseCase(fakeRepo), mockk())
        val collectJob = launch(UnconfinedTestDispatcher()) { vm.state.collect() {} }

        val msgText = "Message text 1"
        vm.sendAction(ChatAction.UpdateMsgText(msgText))

        assertEquals(msgText, vm.state.value.msgText)

        collectJob.cancel()
        Dispatchers.resetMain()
    }

    @Test
    fun `properly sends text msg`(): Unit = runTest {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        val fakeAuth = GetCurrentUserUseCaseFake()
        val fakeRepo = MessageRepositoryFake()

        val vm = ChatViewModel(fakeAuth, DisplayChatUseCase(fakeRepo), SendMessageUseCase(fakeRepo), mockk())
        val collectJob = launch(UnconfinedTestDispatcher()) { vm.state.collect() {} }

        val user = BasicUser("uid", "name1", "photo1")
        fakeAuth.userFlow.emit(user)

        val msgText = "Message text 1"

        vm.sendAction(ChatAction.UpdateMsgText(msgText))

        vm.sendAction(ChatAction.SendMessage)

        assertEquals(1, vm.state.value.messages.size)
        val msg = vm.state.value.messages[0]
        assertEquals(user.displayName, msg.name)
        assertEquals(user.photoUrl, msg.photoUrl)
        assertEquals(msgText, msg.text)
        assertNull("Image null during text message", msg.imageUrl)

        collectJob.cancel()
        Dispatchers.resetMain()
    }

    @Test
    fun `properly sends image msg_test_dispatcher`(): Unit = runTest {
        Dispatchers.setMain(StandardTestDispatcher())
        val fakeAuth = GetCurrentUserUseCaseFake()
        val fakeRepo = MessageRepositoryFake()
        val fakeUploadImage = UploadImageUseCaseFake()

        val vm = ChatViewModel(fakeAuth, DisplayChatUseCase(fakeRepo), SendMessageUseCase(fakeRepo), fakeUploadImage)
        val collectJob = launch(UnconfinedTestDispatcher()) { vm.state.collect() {} }

        val user = BasicUser("uid", "name1", "photo1")
        fakeAuth.userFlow.emit(user)
        advanceUntilIdle()
        assertNull(vm.state.value.user)
        assertEquals(0, vm.state.value.messages.size)

        val imgUriString = "https://picsum.photos/aaa"
        val uriMock = mockk<Uri>()
        every { uriMock.lastPathSegment } returns "aaa"
        every { uriMock.toString() } returns imgUriString

        vm.sendAction(ChatAction.SendImageMessage(uriMock))
       runCurrent()

        assertEquals(1, vm.state.value.messages.size)
        var msg = vm.state.value.messages[0]
        assertEquals(user.displayName, msg.name)
        assertEquals(user.photoUrl, msg.photoUrl)
        assertEquals(ChatViewModel.LOADING_IMAGE_URL, msg.imageUrl)
        assertNull("Text null in img message", msg.text)

        advanceUntilIdle()
        assertEquals(1, vm.state.value.messages.size)
        msg = vm.state.value.messages[0]
        assertEquals(user.displayName, msg.name)
        assertEquals(user.photoUrl, msg.photoUrl)
        assertEquals(imgUriString, msg.imageUrl)
        assertNull("Text null in img message", msg.text)

        collectJob.cancel()
        Dispatchers.resetMain()
    }

    @Test
    fun `properly sends image msg`(): Unit = runBlocking(ImmediateTestDispatcher()) {
        Dispatchers.setMain(ImmediateTestDispatcher())
        val fakeAuth = GetCurrentUserUseCaseFake()
        val fakeRepo = MessageRepositoryFake()
        val fakeUploadImage = UploadImageUseCaseFake()
        val imgUriString = "https://picsum.photos/aaa"
        val uriMock = mockk<Uri>()
        every { uriMock.lastPathSegment } returns "aaa"
        every { uriMock.toString() } returns imgUriString
        val vm = ChatViewModel(fakeAuth, DisplayChatUseCase(fakeRepo), SendMessageUseCase(fakeRepo), fakeUploadImage)
        val user = BasicUser("uid", "name1", "photo1")
        fakeAuth.userFlow.emit(user)
        val collectedStates = mutableListOf<ChatState>()
        val collectJob = launch(UnconfinedTestDispatcher()) { vm.state.collect { collectedStates.add(it)} }


        vm.sendAction(ChatAction.SendImageMessage(uriMock))
//        advanceUntilIdle()


        assertEquals(3, collectedStates.size)

        assertEquals(0, collectedStates[0].messages.size)

        val secondStateMsg = collectedStates[1].messages[0]
        assertEquals(user.displayName, secondStateMsg.name)
        assertEquals(user.photoUrl, secondStateMsg.photoUrl)
        assertEquals(ChatViewModel.LOADING_IMAGE_URL, secondStateMsg.imageUrl)
        assertNull(secondStateMsg.text)


        val finalMessage = collectedStates[2].messages[0]
        assertEquals(user.displayName, finalMessage.name)
        assertEquals(user.photoUrl, finalMessage.photoUrl)
        assertEquals(imgUriString, finalMessage.imageUrl)
        assertNull(finalMessage.text)


        collectJob.cancel()
        Dispatchers.resetMain()
    }

}