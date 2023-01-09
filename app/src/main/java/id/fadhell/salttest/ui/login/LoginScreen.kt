package id.fadhell.salttest.ui.login

import android.R.attr.password
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import id.fadhell.salttest.api.request.LoginRequest
import id.fadhell.salttest.api.usecase.LoginUseCase
import id.fadhell.salttest.ui.CustomLoading
import id.fadhell.salttest.ui.destinations.HomeScreenDestination
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch


@Destination
@Composable
fun LoginScreen(navigator: DestinationsNavigator, viewModel: LoginViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    val activity = (LocalContext.current as? Activity)
    BackHandler {
        activity?.finish()
    }
    RenderView(navigator = navigator, state = state, viewModel = viewModel)
}

@Composable
fun RenderView(
    navigator: DestinationsNavigator,
    viewModel: LoginViewModel,
    state: LoginViewModel.LoginUiEvent
) {
    var isShowError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    LoginView(viewModel = viewModel, isShowError, errorMessage)
    when (state) {
        is LoginViewModel.LoginUiEvent.Loading -> {
            if (state.isShow) {
                Dialog(
                    onDismissRequest = { },
                    DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
                ) {
                    CustomLoading(modifier = Modifier.fillMaxSize())
                }
            }
        }
        is LoginViewModel.LoginUiEvent.SuccessLogin -> {
            navigator.navigate(HomeScreenDestination)
        }
        is LoginViewModel.LoginUiEvent.FetchError -> {
            isShowError = true
            errorMessage = state.message.orEmpty()
        }
    }
}

@Composable
fun LoginView(
    viewModel: LoginViewModel,
    isShowError: Boolean?,
    errorMessage: String?
) {
    val scaffoldState = rememberScaffoldState()
    val scrollState = rememberScrollState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Scaffold(scaffoldState = scaffoldState,
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
            ) {
                Text(
                    text = "Selamat datang, Salt!", modifier = Modifier
                        .padding(
                            top = 16.dp,
                            start = 16.dp,
                            end = 16.dp
                        )
                        .align(Alignment.CenterHorizontally)
                )
                Text(
                    text = "Welcome back, you’ve been missed!",
                    modifier = Modifier
                        .padding(
                            top = 8.dp,
                            start = 16.dp,
                            end = 16.dp
                        )
                        .align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center
                )
                InputEmailWidget(
                    title = "Email",
                    placeholders = "Masukkan email",
                    isError = isShowError?: false,
                    errorMessage = errorMessage, onChange = {
                        email = it
                    })
                InputPasswordWidget(
                    title = "Password",
                    placeholders = "Masukkan Password"
                ) {
                    password = it
                }
            }
        },
        bottomBar = {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Button(modifier = Modifier.fillMaxWidth(),onClick = {
                    viewModel.login(email, password)
                }) {
                    Text(text = "Login")
                }
            }
        })
}

@Composable
fun InputEmailWidget(title: String, placeholders: String, onChange: (String) -> Unit, isError: Boolean, errorMessage: String?) {
    var number by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    Column {
        Text(
            title,
            modifier = Modifier.padding(
                top = 24.dp,
                start = 16.dp,
                end = 16.dp
            )
        )
        OutlinedTextField(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 6.dp)
                .fillMaxWidth()
                .height(56.dp),
            value = number,
            placeholder = {
                Text(modifier = Modifier.align(Alignment.CenterHorizontally), text = placeholders)
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Gray,
                cursorColor = Color.Black,
                unfocusedBorderColor = Color.Gray
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }),
            onValueChange = {
                number = it
                onChange(number)
            },
        )
        if (isError) {
            Text(
                text = errorMessage.orEmpty(),
                color = Color.Red,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}

@Composable
fun InputPasswordWidget(title: String, placeholders: String, onChange: (String) -> Unit) {
    var password by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    Column {
        Text(
            title,
            modifier = Modifier.padding(
                top = 24.dp,
                start = 16.dp,
                end = 16.dp
            )
        )
        OutlinedTextField(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 6.dp)
                .fillMaxWidth()
                .height(56.dp),
            value = password,
            placeholder = {
                Text(modifier = Modifier.align(Alignment.CenterHorizontally), text = placeholders)
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Gray,
                cursorColor = Color.Black,
                unfocusedBorderColor = Color.Gray
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }),
            onValueChange = {
                password = it
                onChange(password)
            },
        )
    }
}


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val application: Application
) : ViewModel() {

    var state = MutableStateFlow<LoginUiEvent>(LoginUiEvent.Loading(false))

    private fun emitState(state: LoginUiEvent) {
        viewModelScope.launch {
            this@LoginViewModel.state.emit(state)
        }
    }

    private fun setPreference(token: String) {
        val preferences: SharedPreferences = application.getSharedPreferences("AUTHENTICATION", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("token", token)
        editor.apply()
    }

    fun login(email: String, password: String) {
        emitState(LoginUiEvent.Loading(true))
        viewModelScope.launch {
            loginUseCase.execute(LoginRequest(email, password))
                .catch { exception ->
                    val message = if (exception.message.isNullOrBlank()) "Mohon Coba Lagi"
                    else exception.message
                    emitState(LoginUiEvent.Loading(false))
                    emitState(LoginUiEvent.FetchError(message))
                }.collect {
                    setPreference(it)
                    emitState(LoginUiEvent.SuccessLogin(it))
                }
        }
    }

    sealed class LoginUiEvent {
        data class Loading(val isShow: Boolean) : LoginUiEvent()
        data class FetchError(val message: String?) : LoginUiEvent()
        data class SuccessLogin(val token: String) : LoginUiEvent()
    }
}