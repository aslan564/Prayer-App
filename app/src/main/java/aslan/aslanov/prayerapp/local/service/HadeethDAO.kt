package aslan.aslanov.prayerapp.local.service

import androidx.lifecycle.LiveData
import androidx.room.*
import aslan.aslanov.prayerapp.model.hadeeths.HadeethsEntity
import aslan.aslanov.prayerapp.model.hadithCategory.CategoryEntity

@Dao
interface HadeethDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(vararg categoryEntity: CategoryEntity)

    @Query("SELECT * FROM table_category")
    fun getCategoryOfHadeeths(): LiveData<List<CategoryEntity>>

    @Query("DELETE FROM table_category")
    suspend fun deleteAllCategory()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHadeethsByCategory(vararg hadeethsEntity: HadeethsEntity)

    @Query("DELETE FROM table_hadeeths where categoryId=:idCategory")
    suspend fun deleteAllHadeeths(vararg idCategory: Int)

    @Query("SELECT * FROM table_hadeeths where categoryId=:idCategory")
    fun getHadeethsByCategory(idCategory: Int): LiveData<List<HadeethsEntity>>

    @Query("SELECT * FROM table_hadeeths")
    fun getRandomHadeethsFromQuran(): LiveData<List<HadeethsEntity>>

}