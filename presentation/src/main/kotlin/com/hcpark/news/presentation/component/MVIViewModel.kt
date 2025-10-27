package com.hcpark.news.presentation.component

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

// references: https://velog.io/@victorywoo/Android-MVI-Architecture-with-Kotlin-Flows-and-Channels-%EB%B2%88%EC%97%AD

interface UiState
interface UiEvent
interface UiEffect

abstract class MVIViewModel<Event : UiEvent, State : UiState, Effect : UiEffect> : ViewModel() {
    private val initialState: State by lazy { createInitialState() }
    abstract fun createInitialState(): State
    private val _uiState: MutableStateFlow<State> = MutableStateFlow(initialState)
    val uiState = _uiState.asStateFlow()
    val currentState: State get() = _uiState.value

    private val _event: MutableSharedFlow<Event> = MutableSharedFlow()
    private val _effect: Channel<Effect> = Channel()
    val effect = _effect.receiveAsFlow()

    init {
        collectEvent()
    }

    protected fun setState(reduce: State.() -> State) {
        val newState = currentState.reduce()
        _uiState.value = newState
    }

    @OptIn(FlowPreview::class)
    private fun collectEvent() = viewModelScope.launch {
        _event.collect { handleEvent(it) }
    }

    protected abstract fun handleEvent(event: Event)

    fun setEvent(event: Event) = viewModelScope.launch {
        _event.emit(event)
    }

    protected fun setEffect(effect: Effect) = viewModelScope.launch {
        _effect.send(effect)
    }
}
