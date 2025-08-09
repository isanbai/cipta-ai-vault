package com.example.kioskapp.trigger

import kotlinx.coroutines.flow.Flow

/**
 * Common interface for all trigger sources.  Implementations should emit
 * booleans indicating whether the external trigger is ON (`true`) or OFF
 * (`false`).  Each implementation must handle any internal resources (e.g.
 * listeners, servers) in [start] and release them in [stop].  Consumers
 * should call [start] once during the lifecycle when the trigger should
 * become active, typically in `onStart()` of an activity or service, and
 * [stop] in `onStop()`.
 */
interface TriggerSource {
    /** Flow emitting the current trigger state.  True indicates active, false idle. */
    val events: Flow<Boolean>

    /** Start listening for trigger events. */
    fun start()

    /** Stop listening and release resources. */
    fun stop()
}