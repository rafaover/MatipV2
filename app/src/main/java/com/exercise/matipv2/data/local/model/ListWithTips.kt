package com.exercise.matipv2.data.local.model

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Collects an [List] with all their tips.
 * Uses the foreign Key on [Tip].
*/
data class ListWithTips(
    @Embedded val list: List,
    @Relation(
        parentColumn = "id",
        entityColumn = "list_id"
    )
    val tips: kotlin.collections.List<Tip>
)
