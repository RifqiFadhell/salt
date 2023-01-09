package id.fadhell.salttest

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.plusAssign
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import com.ramcosta.composedestinations.spec.Route
import dagger.hilt.android.AndroidEntryPoint
import id.fadhell.salttest.ui.NavGraphs
import id.fadhell.salttest.ui.appCurrentDestinationAsState
import id.fadhell.salttest.ui.destinations.HomeScreenDestination
import id.fadhell.salttest.ui.startAppDestination

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScaffoldNavHost()
        }
    }
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialNavigationApi::class)
@Composable
private fun ScaffoldNavHost() {
    val engine = rememberAnimatedNavHostEngine()
    val navController = engine.rememberNavController()
    val startRoute = HomeScreenDestination
    Scaffold {
        DestinationsNavHost(
            engine = engine,
            navController = navController,
            navGraph = NavGraphs.root,
            modifier = Modifier.padding(it),
            startRoute = startRoute,
            dependenciesContainerBuilder = {
            }
        )
    }
}