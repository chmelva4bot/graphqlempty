package cz.applifting.graphqlempty.firebasechat.chat.ui

import android.net.Uri
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import cz.applifting.graphqlempty.firebasechat.common.TestTags
import cz.applifting.graphqlempty.firebasechat.auth.BasicUser
import cz.applifting.graphqlempty.firebasechat.auth.GetCurrentUserUseCaseFake
import cz.applifting.graphqlempty.firebasechat.chat.data.DisplayChatUseCase
import cz.applifting.graphqlempty.firebasechat.chat.data.IMessageRepository
import cz.applifting.graphqlempty.firebasechat.chat.data.IUploadImageUseCase
import cz.applifting.graphqlempty.firebasechat.chat.data.MessageRepositoryFake
import cz.applifting.graphqlempty.firebasechat.chat.data.SendMessageUseCase
import cz.applifting.graphqlempty.firebasechat.chat.data.UploadImageUseCaseFake
import cz.applifting.graphqlempty.ui.theme.GraphqlEmptyTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class ChatScreenIntegrationTest {

    @get:Rule
    val composeRule = createComposeRule()

    private lateinit var viewModel: cz.applifting.graphqlempty.firebasechat.chat.ui.ChatViewModel;
    private lateinit var fakeRepo: cz.applifting.graphqlempty.firebasechat.chat.data.IMessageRepository;
    private lateinit var fakeAuth: cz.applifting.graphqlempty.firebasechat.auth.GetCurrentUserUseCaseFake;
    private lateinit var fakeImageUpload: cz.applifting.graphqlempty.firebasechat.chat.data.IUploadImageUseCase;
    private lateinit var pickedFlow: MutableSharedFlow<Uri>;
    private lateinit var user: cz.applifting.graphqlempty.firebasechat.auth.BasicUser;

    @Before
    fun setup() {
//        Dispatchers.setMain(UnconfinedTestDispatcher())
        fakeAuth = cz.applifting.graphqlempty.firebasechat.auth.GetCurrentUserUseCaseFake()
        fakeRepo = cz.applifting.graphqlempty.firebasechat.chat.data.MessageRepositoryFake()
        fakeImageUpload = cz.applifting.graphqlempty.firebasechat.chat.data.UploadImageUseCaseFake()

        viewModel = cz.applifting.graphqlempty.firebasechat.chat.ui.ChatViewModel(
            fakeAuth,
            cz.applifting.graphqlempty.firebasechat.chat.data.DisplayChatUseCase(fakeRepo),
            cz.applifting.graphqlempty.firebasechat.chat.data.SendMessageUseCase(fakeRepo),
            fakeImageUpload
        )

        pickedFlow = MutableSharedFlow()
        user = cz.applifting.graphqlempty.firebasechat.auth.BasicUser("uid1", "user1", "https://picsum.photos/100/100")


        composeRule.setContent {
            GraphqlEmptyTheme {
                val scope = rememberCoroutineScope()
                LaunchedEffect(true) {this.launch { fakeAuth.userFlow.emit(user) }}
                cz.applifting.graphqlempty.firebasechat.chat.ui.ChatScreenContainer(viewModel = viewModel,
                    goToAuth = {},
                    imagePickedFlow = pickedFlow,
                    onSelectImageClicked = {
                        scope.launch { pickedFlow.emit(Uri.parse("https://picsum.photos/300/200")) }
                    })
            }
        }
    }

    @Test
    fun composeSendAndDisplayTextMessage() {
        val msg = "I am a message"
        composeRule.onNodeWithTag(cz.applifting.graphqlempty.firebasechat.common.TestTags.MESSAGE_INPUT).performTextInput(msg)
        composeRule.onNodeWithContentDescription("Send a message").performClick()

        composeRule.onNodeWithTag(cz.applifting.graphqlempty.firebasechat.common.TestTags.MESSAGE_INPUT).assertTextEquals("")

        composeRule.onNodeWithText(user.displayName).assertExists()
        composeRule.onNodeWithText(msg).assertExists()
        composeRule.onNodeWithContentDescription("User avatar").assertExists()

        composeRule.onNodeWithContentDescription("Image message").assertDoesNotExist()
    }

    @Test
    fun composeSendAndDisplayImageMessage() {
        composeRule.onNodeWithContentDescription("Pick an image").performClick()

        composeRule.onNodeWithText(user.displayName).assertExists()
        composeRule.onNodeWithContentDescription("User avatar").assertExists()
        composeRule.onNodeWithContentDescription("Image message").assertExists()
    }
}