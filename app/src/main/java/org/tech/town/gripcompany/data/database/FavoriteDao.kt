package org.tech.town.gripcompany.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import org.tech.town.gripcompany.data.model.Search

@Dao
interface FavoriteDao {
    @Insert(onConflict = REPLACE)
    fun insert(search: Search)

    @Query("SELECT * FROM SEARCH")
    fun getAll(): List<Search>

    @Delete
    fun delete(search: Search)
}
