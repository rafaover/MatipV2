package com.exercise.matipv2.ui.navigation

import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

/** Every destination in the app. Routes are saved with the back stack, so each must be serializable. */
@Serializable
sealed interface MatipRoute : NavKey {
    @Serializable
    data object TipCalculator : MatipRoute

    @Serializable
    data object Lists : MatipRoute

    @Serializable
    data object Settings : MatipRoute

    /** Shown as a full-screen dialog over the Lists tab. */
    @Serializable
    data class ListTipList(val listId: Int) : MatipRoute
}

/** Registers every route so the back stack can be saved and restored on all platforms. */
val navigationSavedStateConfiguration = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(MatipRoute.TipCalculator::class)
            subclass(MatipRoute.Lists::class)
            subclass(MatipRoute.Settings::class)
            subclass(MatipRoute.ListTipList::class)
        }
    }
}
