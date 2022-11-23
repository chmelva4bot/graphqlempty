package cz.applifting.graphqlempty.firebasechat.ui

import android.net.Uri
import cz.applifting.graphqlempty.firebasechat.ImmediateTestDispatcher
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ChatViewModelTest {

    private lateinit var vm: cz.applifting.graphqlempty.firebasechat.chat.ui.ChatViewModel
    private lateinit var fakeAuth: cz.applifting.graphqlempty.firebasechat.auth.GetCurrentUserUseCaseFake
    private lateinit var fakeRepo: cz.applifting.graphqlempty.firebasechat.chat.data.MessageRepositoryFake
    private lateinit var fakeImageUpload: cz.applifting.graphqlempty.firebasechat.chat.data.UploadImageUseCaseFake


    fun setup(dispatcher: CoroutineDispatcher) {
        Dispatchers.setMain(dispatcher)

        fakeAuth = cz.applifting.graphqlempty.firebasechat.auth.GetCurrentUserUseCaseFake()
        fakeRepo = cz.applifting.graphqlempty.firebasechat.chat.data.MessageRepositoryFake()
        fakeImageUpload = cz.applifting.graphqlempty.firebasechat.chat.data.UploadImageUseCaseFake()

        vm = cz.applifting.graphqlempty.firebasechat.chat.ui.ChatViewModel(
            fakeAuth,
            cz.applifting.graphqlempty.firebasechat.chat.data.DisplayChatUseCase(fakeRepo),
            cz.applifting.graphqlempty.firebasechat.chat.data.SendMessageUseCase(fakeRepo),
            fakeImageUpload
        )
    }

    @After
    fun teardown() { Dispatchers.resetMain()}

    @Test
    fun `properly checks for user login`(): Unit = runTest {
        setup(UnconfinedTestDispatcher())

        val collectJob = launch(UnconfinedTestDispatcher()) { vm.state.collect() {} }
        assertFalse("User not checked at start", vm.state.value.isUserChecked)

        fakeAuth.userFlow.emit(null)
        assertTrue("User checked after action", vm.state.value.isUserChecked)
        assertNull("User is null after action", vm.state.value.user)

        collectJob.cancel()
    }
    @Test
    fun `properly stores user in state`(): Unit = runTest {
        setup(UnconfinedTestDispatcher())
        val collectJob = launch(UnconfinedTestDispatcher()) { vm.state.collect() {} }
        fakeAuth.userFlow.emit(null)

        assertNull("User is null at first", vm.state.value.user)

        val user = cz.applifting.graphqlempty.firebasechat.auth.BasicUser("uid", "name1", "photo1")
        fakeAuth.userFlow.emit(user)

        assertEquals(user.uid, vm.state.value.user?.uid)
        assertEquals(user.displayName, vm.state.value.user?.displayName)
        assertEquals(user.photoUrl, vm.state.value.user?.photoUrl)

        collectJob.cancel()
    }


    @Test
    fun `properly stores msg text in state`(): Unit = runTest {
        setup(UnconfinedTestDispatcher())
        val collectJob = launch(UnconfinedTestDispatcher()) { vm.state.collect() {} }

        val msgText = "Message text 1"
        vm.sendAction(cz.applifting.graphqlempty.firebasechat.chat.ui.ChatAction.UpdateMsgText(msgText))

        assertEquals(msgText, vm.state.value.msgText)

        collectJob.cancel()
    }

    @Test
    fun `properly sends text msg`(): Unit = runTest {
        setup(UnconfinedTestDispatcher())
        val collectJob = launch(UnconfinedTestDispatcher()) { vm.state.collect() {} }

        val user = cz.applifting.graphqlempty.firebasechat.auth.BasicUser("uid", "name1", "photo1")
        fakeAuth.userFlow.emit(user)

        val msgText = "Message text 1"

        vm.sendAction(cz.applifting.graphqlempty.firebasechat.chat.ui.ChatAction.UpdateMsgText(msgText))
        vm.sendAction(cz.applifting.graphqlempty.firebasechat.chat.ui.ChatAction.SendMessage)

        assertEquals(1, vm.state.value.messages.size)
        val msg = vm.state.value.messages[0]
        assertEquals(user.displayName, msg.name)
        assertEquals(user.photoUrl, msg.photoUrl)
        assertEquals(msgText, msg.text)
        assertNull("Image null during text message", msg.imageUrl)

        collectJob.cancel()
    }

    @Test
    fun `properly sends image msg_test_dispatcher`(): Unit = runTest {
        setup(StandardTestDispatcher())
        val collectJob = launch(UnconfinedTestDispatcher()) { vm.state.collect() {} }

        val user = cz.applifting.graphqlempty.firebasechat.auth.BasicUser("uid", "name1", "photo1")
        fakeAuth.userFlow.emit(user)
        advanceUntilIdle()
        assertNull(vm.state.value.user)
        assertEquals(0, vm.state.value.messages.size)

        val imgUriString = "https://picsum.photos/aaa"
        val uriMock = mockk<Uri>()
        every { uriMock.lastPathSegment } returns "aaa"
        every { uriMock.toString() } returns imgUriString

        vm.sendAction(cz.applifting.graphqlempty.firebasechat.chat.ui.ChatAction.SendImageMessage(uriMock))
       runCurrent()

        assertEquals(1, vm.state.value.messages.size)
        var msg = vm.state.value.messages[0]
        assertEquals(user.displayName, msg.name)
        assertEquals(user.photoUrl, msg.photoUrl)
        assertEquals(cz.applifting.graphqlempty.firebasechat.chat.ui.ChatViewModel.LOADING_IMAGE_URL, msg.imageUrl)
        assertNull("Text null in img message", msg.text)

        advanceUntilIdle()
        assertEquals(1, vm.state.value.messages.size)
        msg = vm.state.value.messages[0]
        assertEquals(user.displayName, msg.name)
        assertEquals(user.photoUrl, msg.photoUrl)
        assertEquals(imgUriString, msg.imageUrl)
        assertNull("Text null in img message", msg.text)

        collectJob.cancel()
    }

    @Test
    fun `properly sends image msg`(): Unit = runBlocking(ImmediateTestDispatcher()) {
        setup(ImmediateTestDispatcher())

        val imgUriString = "https://picsum.photos/aaa"
        val uriMock = mockk<Uri>()
        every { uriMock.lastPathSegment } returns "aaa"
        every { uriMock.toString() } returns imgUriString

        val user = cz.applifting.graphqlempty.firebasechat.auth.BasicUser("uid", "name1", "photo1")
        fakeAuth.userFlow.emit(user)

        val collectedStates = mutableListOf<cz.applifting.graphqlempty.firebasechat.chat.ui.ChatState>()
        val collectJob = launch(UnconfinedTestDispatcher()) { vm.state.collect { collectedStates.add(it)} }

        vm.sendAction(cz.applifting.graphqlempty.firebasechat.chat.ui.ChatAction.SendImageMessage(uriMock))

        assertEquals(3, collectedStates.size)

        assertEquals(0, collectedStates[0].messages.size)

        val secondStateMsg = collectedStates[1].messages[0]
        assertEquals(user.displayName, secondStateMsg.name)
        assertEquals(user.photoUrl, secondStateMsg.photoUrl)
        assertEquals(cz.applifting.graphqlempty.firebasechat.chat.ui.ChatViewModel.LOADING_IMAGE_URL, secondStateMsg.imageUrl)
        assertNull(secondStateMsg.text)

        val finalMessage = collectedStates[2].messages[0]
        assertEquals(user.displayName, finalMessage.name)
        assertEquals(user.photoUrl, finalMessage.photoUrl)
        assertEquals(imgUriString, finalMessage.imageUrl)
        assertNull(finalMessage.text)

        collectJob.cancel()
    }
}