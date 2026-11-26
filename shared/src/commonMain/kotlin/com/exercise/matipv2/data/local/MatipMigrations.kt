package com.exercise.matipv2.data.local

import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL

/*
 * One migration per schema version, rebuilt from the exported schemas in `shared/schemas/`.
 * Never drop the database instead of migrating: users must not lose their tips.
 * Tables that gain or lose a NOT NULL column are rebuilt (create, copy, drop, rename)
 * so the result matches the exported schema exactly.
 */

/** Events became lists, and each tip points to a list instead of an event. */
val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(connection: SQLiteConnection) {
        connection.execSQL(
            "CREATE TABLE IF NOT EXISTS lists (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, list_name TEXT NOT NULL)"
        )
        connection.execSQL("INSERT INTO lists (id, list_name) SELECT id, event_name FROM events")
        connection.execSQL("DROP TABLE events")

        connection.execSQL(
            "CREATE TABLE tips_new (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "tip_amount TEXT NOT NULL, tip_percent TEXT NOT NULL, list_id INTEGER NOT NULL)"
        )
        connection.execSQL(
            "INSERT INTO tips_new (id, tip_amount, tip_percent, list_id) " +
                "SELECT id, tip_amount, tip_percent, event_id FROM tips"
        )
        connection.execSQL("DROP TABLE tips")
        connection.execSQL("ALTER TABLE tips_new RENAME TO tips")
    }
}

/** Tips got a creation date. Tips saved before this have no date, so they get an empty one. */
val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(connection: SQLiteConnection) {
        connection.execSQL(
            "CREATE TABLE tips_new (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "tip_amount TEXT NOT NULL, tip_percent TEXT NOT NULL, list_id INTEGER NOT NULL, " +
                "date_created TEXT NOT NULL)"
        )
        connection.execSQL(
            "INSERT INTO tips_new (id, tip_amount, tip_percent, list_id, date_created) " +
                "SELECT id, tip_amount, tip_percent, list_id, '' FROM tips"
        )
        connection.execSQL("DROP TABLE tips")
        connection.execSQL("ALTER TABLE tips_new RENAME TO tips")
    }
}

/** The schema did not change (same identity hash), only the version number moved. */
val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(connection: SQLiteConnection) = Unit
}

/** Lists and tips can belong to a signed-in user. Existing rows stay as guest data (no user). */
val MIGRATION_5_6 = object : Migration(5, 6) {
    override fun migrate(connection: SQLiteConnection) {
        connection.execSQL("ALTER TABLE lists ADD COLUMN user_id TEXT")
        connection.execSQL("ALTER TABLE tips ADD COLUMN user_id TEXT")
    }
}

/** Lists and tips can point to their cloud backup copy. */
val MIGRATION_6_7 = object : Migration(6, 7) {
    override fun migrate(connection: SQLiteConnection) {
        connection.execSQL("ALTER TABLE lists ADD COLUMN cloud_id TEXT")
        connection.execSQL("ALTER TABLE tips ADD COLUMN cloud_id TEXT")
    }
}

/** Every step from the first exported schema (2) to the current one (7). */
val MATIP_MIGRATIONS = arrayOf(
    MIGRATION_2_3,
    MIGRATION_3_4,
    MIGRATION_4_5,
    MIGRATION_5_6,
    MIGRATION_6_7,
)
