package com.example.mvvm_grp

import com.example.mvvm_grp.mvvm_svc.AnswerService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MvvmViewModel(
    private val answerService: AnswerService,
) {

    /* coroutine scope */
    private val coroutineScope = CoroutineScope(Dispatchers.Default)//MainScope()

    /* state-flow */
    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    /* state-flow */
    private val _textToDisplay: MutableStateFlow<String> = MutableStateFlow("")
    val textToDisplay = _textToDisplay.asStateFlow()

    /* channel flow */
    // See proandroiddev.com/android-singleliveevent-redux-with-kotlin-flow-b755c70bb055
    //  of 2021, for why channel > SharedFlow/StateFlow in this case
    // Update 2022: github.com/Kotlin/kotlinx.coroutines/issues/2886
    //  Due to the prompt cancellation guarantee changes that landed in Coroutines 1.4,
    //  Channels cannot be used as mechanism that guarantees delivery and acknowledges
    //  successful processing between producer and consumer in order to guarantee
    //  that an item was handled exactly once.
    private val _navigateToResults = Channel<Boolean>(Channel.BUFFERED)
    val navigateToResults = _navigateToResults.receiveAsFlow()

    fun confirmAnswer(answer: String) {
        // the launch failed with the MainScope():
        // Module with the Main dispatcher is missing. Add dependency providing
        // the Main dispatcher, e.g. 'kotlinx-coroutines-android' and ensure
        // it has the same version as 'kotlinx-coroutines-core'
        coroutineScope.launch {
            _isLoading.value = true
            withContext(Dispatchers.IO) { answerService.save(answer) }
            val is_ok: Boolean = answer == "Nacho cheese"
            val text = if (is_ok) {
                "You've heard too many cheese jokes"
            } else {
                "Nacho cheese"
            }
            _textToDisplay.emit(text)
            _navigateToResults.send(is_ok)
            _isLoading.value = false
        }

        coroutineScope.launch {
            val text = if (answer == "Nacho cheese") {
                "You've heard too many cheese jokes - preview"
            } else {
                "Nacho cheese - preview"
            }
            _textToDisplay.emit(text)
        }
    }
}

