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
fun MainWindow(mvvmViewModel: MvvmViewModel) {
    var text by remember { mutableStateOf("Hello, World!") }
    Button(onClick = {
        text = "Hello, Desktop!"
    }) {
        Text(text)
    }
}

@Composable
@Preview
fun App() {
    val answerService = remember { AnswerService() }
    val mvvmViewModel = remember { MvvmViewModel(answerService) }

    MaterialTheme {
        Column {
            MainWindow(mvvmViewModel = mvvmViewModel)
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
