package com.example.mvvm_demo.mvvm_svc

import kotlinx.coroutines.delay


class AnswerService {

    suspend fun save(answer: String) {
        //Log.v("Api call", "Make a call to an api")
        delay(1000)
    }
}

