package aslan.aslanov.prayerapp.repository

import aslan.aslanov.prayerapp.local.PrayerDatabase
import aslan.aslanov.prayerapp.model.whereWereWe.WhereWereWe
import java.lang.Exception

class WhereWereRepository(private val database: PrayerDatabase) {

    suspend fun setPositionFromDatabase(whereWereWe: WhereWereWe) {
        try {
            database.getWhereWere().insertWhereWere(whereWereWe)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun getPositionFromDatabase(id:Int): WhereWereWe? {
        return try {
            database.getWhereWere().getWhereWere(id)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}