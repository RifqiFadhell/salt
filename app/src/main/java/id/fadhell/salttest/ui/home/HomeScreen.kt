package id.fadhell.salttest.ui.home

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import id.fadhell.salttest.ui.destinations.LoginScreenDestination
import javax.inject.Inject


@Destination
@RootNavGraph(true)
@Composable
fun HomeScreen(navigator: DestinationsNavigator, viewModel: HomeViewModel = hiltViewModel()) {
    val activity = (LocalContext.current as? Activity)
    if (viewModel.getToken().isEmpty()) {
        navigator.navigate(LoginScreenDestination)
    } else {
        RenderView(navigator, viewModel)
    }
    BackHandler {
        activity?.finish()
    }
}

@Composable
fun RenderView(navigator: DestinationsNavigator, viewModel: HomeViewModel) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = viewModel.getToken())
        Button(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 64.dp), onClick = {
            viewModel.removeToken()
            navigator.navigate(LoginScreenDestination)
        }) {
            Text(text = "Logout")
        }
    }
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val application: Application
): ViewModel() {

    fun getToken(): String {
        val prefs: SharedPreferences = application.getSharedPreferences("AUTHENTICATION", Context.MODE_PRIVATE)
        return prefs.getString("token", "").orEmpty()
    }

    fun removeToken() {
        val prefs: SharedPreferences = application.getSharedPreferences("AUTHENTICATION", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.remove("token")
        editor.apply()
    }
}