package cz.applifting.graphqlempty.firebase.chat.ui

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
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import cz.applifting.graphqlempty.common.TestTags
import cz.applifting.graphqlempty.firebase.auth.BasicUser
import cz.applifting.graphqlempty.firebase.auth.GetCurrentUserUseCase
import cz.applifting.graphqlempty.firebase.auth.GetCurrentUserUseCaseFake
import cz.applifting.graphqlempty.firebase.auth.IGetCurrentUserUseCase
import cz.applifting.graphqlempty.firebase.chat.data.DisplayChatUseCase
import cz.applifting.graphqlempty.firebase.chat.data.IMessageRepository
import cz.applifting.graphqlempty.firebase.chat.data.IUploadImageUseCase
import cz.applifting.graphqlempty.firebase.chat.data.MessageRepositoryFake
import cz.applifting.graphqlempty.firebase.chat.data.SendMessageUseCase
import cz.applifting.graphqlempty.firebase.chat.data.UploadImageUseCaseFake
import cz.applifting.graphqlempty.ui.theme.GraphqlEmptyTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain

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

    private lateinit var viewModel: ChatViewModel;
    private lateinit var fakeRepo: IMessageRepository;
    private lateinit var fakeAuth: GetCurrentUserUseCaseFake;
    private lateinit var fakeImageUpload: IUploadImageUseCase;
    private lateinit var pickedFlow: MutableSharedFlow<Uri>;
    private lateinit var user: BasicUser;

    @Before
    fun setup() {
//        Dispatchers.setMain(UnconfinedTestDispatcher())
        fakeAuth = GetCurrentUserUseCaseFake()
        fakeRepo = MessageRepositoryFake()
        fakeImageUpload = UploadImageUseCaseFake()

        viewModel = ChatViewModel(fakeAuth, DisplayChatUseCase(fakeRepo), SendMessageUseCase(fakeRepo), fakeImageUpload)

        pickedFlow = MutableSharedFlow()
        user = BasicUser("uid1", "user1", "https://picsum.photos/100/100")


        composeRule.setContent {
            GraphqlEmptyTheme {
                val scope = rememberCoroutineScope()
                LaunchedEffect(true) {this.launch { fakeAuth.userFlow.emit(user) }}
                ChatScreenContainer(
                    viewModel = viewModel, goToAuth = {}, imagePickedFlow = pickedFlow, onSelectImageClicked = {
                        scope.launch { pickedFlow.emit(Uri.parse("https://picsum.photos/300/200")) }
                    }
                )
            }
        }
    }

    @Test
    fun composeSendAndDisplayTextMessage() {
        val msg = "I am a message"
        composeRule.onNodeWithTag(TestTags.MESSAGE_INPUT).performTextInput(msg)
        composeRule.onNodeWithContentDescription("Send a message").performClick()

        composeRule.onNodeWithTag(TestTags.MESSAGE_INPUT).assertTextEquals("")

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