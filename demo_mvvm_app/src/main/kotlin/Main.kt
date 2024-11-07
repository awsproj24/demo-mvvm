//package com.example.demo_mvvm.main

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

@Composable
fun MainWindow(mvvmViewModel: MvvmViewModel, changeCount: Int, onChange: ()->Unit) {
    var text by remember { mutableStateOf("Hello, World!") }
    Button(onClick = {
        text = "Hello, Desktop! " + changeCount.toString()
        onChange()
    }) {
        Text(text)
    }
}

@Composable
@Preview
fun App(answerService: AnswerService, mvvmViewModel: MvvmViewModel) {
    MaterialTheme {
        Column {
            var app_reset_count by remember { mutableStateOf(0) }
            MainWindow(mvvmViewModel = mvvmViewModel, changeCount = app_reset_count,
                onChange = { app_reset_count++ }
            )
            MvvmApp(mvvmViewModel = mvvmViewModel, appResetCount = app_reset_count)
        }
    }
}

fun main() = application {
    val answerService = AnswerService()
    val mvvmViewModel = MvvmViewModel(answerService)
    Window(onCloseRequest = ::exitApplication) {
        App(answerService = answerService, mvvmViewModel = mvvmViewModel)
    }
}
