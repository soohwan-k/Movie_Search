package org.tech.town.gripcompany.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.tech.town.gripcompany.data.model.Search

@Database(entities = [Search::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao

    companion object{
        private var instance: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabase?{
            if(instance == null){
                synchronized(AppDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "favorite-database"
                    ).allowMainThreadQueries()
                        .build()
                }
            }
            return instance
        }
    }
}