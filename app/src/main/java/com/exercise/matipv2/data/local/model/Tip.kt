package com.exercise.matipv2.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tips")
data class Tip(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    /**
     * The amount of the [Tip] given after calculations. In code
     * is known as FinalTip.
     */
    @ColumnInfo(name = "tip_amount")
    val tipAmount: String,
    @ColumnInfo(name = "tip_percent")
    val tipPercent: String,

    /**
     * The [List] id that the [Tip] is associated with.
     */
    @ColumnInfo(name = "list_id")
    var listId: Int,

    /**
     * The date of the [Tip] creation.
     */
    @ColumnInfo(name = "date_created")
    val dateCreated: String
)