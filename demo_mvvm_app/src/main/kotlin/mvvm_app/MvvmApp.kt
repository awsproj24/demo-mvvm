package com.example.mvvm_grp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import kotlinx.coroutines.flow.onEach

/* example app, view-model, answer-service from
 * medium.com/mobile-at-octopus-energy/architecture-in-jetpack-compose-mvp-mvvm-mvi-17d8170a13fd
 *  written on Mar 1, 2021
 */


@Composable
fun MvvmApp(
    mvvmViewModel: MvvmViewModel,
    appResetCount: Int,
) {
//    val navController = rememberNavController()
//    NavHost(navController, startDestination = "question") {
//        composable("question") {
//            MvvmQuestionDestination(
//                mvvmViewModel = mvvmViewModel,
//                // You could pass the nav controller to further composables,
//                // but I like keeping nav logic in a single spot by using the hoisting pattern
//                // hoisting probably won't work as well in deep hierarchies,
//                // in which case CompositionLocal might be more appropriate
//                onConfirm = { navController.navigate("result") },
//            )
//        }
//        composable("result") {
//            MvvmResultDestination(
//                mvvmViewModel,
//            )
//        }
//    }

    var nav_sel by remember { mutableStateOf("question") }
    var nav_reset_count by remember { mutableStateOf(appResetCount) }
    if (nav_reset_count != appResetCount) {
        nav_sel = "question"
        nav_reset_count = appResetCount
    }
    if ( nav_sel == "question") {
            MvvmQuestionDestination(
                mvvmViewModel = mvvmViewModel,
                // You could pass the nav controller to further composables,
                // but I like keeping nav logic in a single spot by using the hoisting pattern
                // hoisting probably won't work as well in deep hierarchies,
                // in which case CompositionLocal might be more appropriate
                onConfirm = { it ->
                    val arg1 = it
                    if (arg1 != null && arg1 == true) {
                        nav_sel = "result"
                    }
                            },
            )
    } else {
            MvvmResultDestination(
                mvvmViewModel = mvvmViewModel,
            )
    }
}

@Composable
fun MvvmQuestionDestination(
    mvvmViewModel: MvvmViewModel,
    onConfirm: (Boolean?) -> Unit
) {
    val textFieldState = remember { mutableStateOf(TextFieldValue()) }

    // We only want the event stream to be attached once
    // even if there are multiple re-compositions
    LaunchedEffect("key") { // probably is a better way to set the key than hardcoding key...
        mvvmViewModel.navigateToResults
            .onEach {
                onConfirm(it)
            }
            .collect() {}
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "What do you call a mexican cheese?")
        TextField(
            value = textFieldState.value,
            onValueChange = { textFieldState.value = it }
        )

        // preview:
        val resultText = mvvmViewModel.textToDisplay.collectAsState().value
        if ( resultText != "" ) {
            Text(text = resultText)
        }

        if (mvvmViewModel.isLoading.collectAsState().value) {
            CircularProgressIndicator()
        } else {
            Button(onClick = { mvvmViewModel.confirmAnswer(textFieldState.value.text) }) {
                Text(text = "Confirm")
            }
        }
    }
}

@Composable
fun MvvmResultDestination(
    mvvmViewModel: MvvmViewModel
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = mvvmViewModel.textToDisplay.collectAsState().value)
    }
}

