package com.exercise.matipv2.data.remote

import com.exercise.matipv2.data.local.model.Tip
import com.exercise.matipv2.data.local.model.TipList

/**
 * Explicit Firestore document shape for lists and tips.
 *
 * Field names are the Kotlin property names, the same ones the old reflection
 * mapper wrote, so existing backups keep restoring. Never rename them.
 */
private const val FIELD_ID = "id"
private const val FIELD_NAME = "name"
private const val FIELD_USER_ID = "userId"
private const val FIELD_CLOUD_ID = "cloudId"
private const val FIELD_TIP_AMOUNT = "tipAmount"
private const val FIELD_TIP_PERCENT = "tipPercent"
private const val FIELD_LIST_ID = "listId"
private const val FIELD_DATE_CREATED = "dateCreated"

fun TipList.toBackupMap(): Map<String, Any?> = mapOf(
    FIELD_ID to id,
    FIELD_NAME to name,
    FIELD_USER_ID to userId,
    FIELD_CLOUD_ID to cloudId
)

fun Tip.toBackupMap(): Map<String, Any?> = mapOf(
    FIELD_ID to id,
    FIELD_TIP_AMOUNT to tipAmount,
    FIELD_TIP_PERCENT to tipPercent,
    FIELD_LIST_ID to listId,
    FIELD_DATE_CREATED to dateCreated,
    FIELD_USER_ID to userId,
    FIELD_CLOUD_ID to cloudId
)

/** Missing fields fall back to the entity defaults, like the old mapper did. */
fun Map<String, Any?>.toTipList(): TipList {
    val defaults = TipList()
    return TipList(
        id = intField(FIELD_ID) ?: defaults.id,
        name = this[FIELD_NAME] as? String ?: defaults.name,
        userId = this[FIELD_USER_ID] as? String,
        cloudId = this[FIELD_CLOUD_ID] as? String
    )
}

fun Map<String, Any?>.toTip(): Tip {
    val defaults = Tip()
    return Tip(
        id = intField(FIELD_ID) ?: defaults.id,
        tipAmount = this[FIELD_TIP_AMOUNT] as? String ?: defaults.tipAmount,
        tipPercent = this[FIELD_TIP_PERCENT] as? String ?: defaults.tipPercent,
        listId = intField(FIELD_LIST_ID) ?: defaults.listId,
        dateCreated = this[FIELD_DATE_CREATED] as? String ?: defaults.dateCreated,
        userId = this[FIELD_USER_ID] as? String,
        cloudId = this[FIELD_CLOUD_ID] as? String
    )
}

/** Firestore returns every stored integer as a Long. */
private fun Map<String, Any?>.intField(field: String): Int? = (this[field] as? Number)?.toInt()
