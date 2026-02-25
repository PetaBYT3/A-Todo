package com.a.todo.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [EntityTodo::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DataConverters::class)
abstract class Database: RoomDatabase() {
    abstract fun dao(): Dao
}