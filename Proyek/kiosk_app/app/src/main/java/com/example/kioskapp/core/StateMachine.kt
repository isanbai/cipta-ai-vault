package com.example.kioskapp.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

/**
 * Simple state machine that transitions between [State.IDLE] (showing the
 * configured image/GIF) and [State.ACTIVE] (showing the video).  It listens
 * to a boolean flow produced by the selected [TriggerSource] and debounces
 * transitions according to the user preference.  When a transition occurs
 * the machine enforces a minimum dwell time to avoid flickering between
 * states if the trigger bounces rapidly.  The minimal dwell times are set
 * to one second for both states.
 */
class StateMachine(
    private val scope: CoroutineScope,
    triggerFlow: kotlinx.coroutines.flow.Flow<Boolean>,
    debounceMsFlow: kotlinx.coroutines.flow.Flow<Int>
) {
    /** Available states. */
    enum class State { IDLE, ACTIVE }

    private val _state = MutableStateFlow(State.IDLE)
    val state: StateFlow<State> get() = _state

    // Timestamps of the last state entry used to enforce minimum dwell time
    private var lastStateChangeTime: Long = System.currentTimeMillis()

    init {
        scope.launch(Dispatchers.Default) {
            // Combine the trigger flow with the debounce time preference.  The
            // debounce operator delays emissions until the specified amount of
            // time has passed without a new value.  See the DataStore
            // documentation for rationale on using flows【83036416110107†L172-L176】.
            combine(triggerFlow.distinctUntilChanged(), debounceMsFlow) { triggered, debounceMs ->
                Pair(triggered, debounceMs)
            }.collect { (triggered, debounceMs) ->
                // Wait for the debounce period before acting
                kotlinx.coroutines.delay(debounceMs.toLong())
                handleTrigger(triggered)
            }
        }
    }

    private suspend fun handleTrigger(on: Boolean) {
        val desiredState = if (on) State.ACTIVE else State.IDLE
        val now = System.currentTimeMillis()
        val elapsed = now - lastStateChangeTime
        // If the current state is already the desired one, ignore
        if (_state.value == desiredState) return
        // Enforce a minimum dwell time of 1 second between transitions
        val minDwell = 1000L
        if (elapsed < minDwell) {
            kotlinx.coroutines.delay(minDwell - elapsed)
        }
        lastStateChangeTime = System.currentTimeMillis()
        _state.value = desiredState
    }
}