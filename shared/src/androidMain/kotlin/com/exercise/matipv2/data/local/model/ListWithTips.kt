package com.exercise.matipv2.data.local.model

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Collects an [TipList] with all their tips.
 * Uses the foreign Key on [Tip].
*/
data class ListWithTips(
    @Embedded val list: TipList,
    @Relation(
        parentColumn = "id",
        entityColumn = "list_id"
    )
    val tips: List<Tip>
)
