package com.exercise.matipv2.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/*
 * Firestore document shapes for backups.
 *
 * Field names are the Kotlin property names that older builds already wrote,
 * so existing backups keep restoring. Never rename a property here.
 * Default values cover documents that are missing a field.
 */

/** Name of the user document field that holds the date of the last backup. */
const val LAST_BACKUP_FIELD = "last_backup"

@Serializable
data class ListBackupDto(
    val id: Int = 0,
    val name: String = "",
    val userId: String? = null,
    val cloudId: String? = null,
)

@Serializable
data class TipBackupDto(
    val id: Int = 0,
    val tipAmount: String = "",
    val tipPercent: String = "",
    val listId: Int = 0,
    val dateCreated: String = "",
    val userId: String? = null,
    val cloudId: String? = null,
)

/** Stored on the user document, next to the lists and tips collections. */
@Serializable
data class BackupMetadataDto(
    @SerialName(LAST_BACKUP_FIELD) val lastBackup: String,
)
