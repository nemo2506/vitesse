package com.openclassrooms.vitesse.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.openclassrooms.vitesse.data.dao.CandidateDao
import com.openclassrooms.vitesse.data.entity.CandidateDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(
    entities = [CandidateDto::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun candidateDao(): CandidateDao

    private class AppDatabaseCallback(
        private val scope: CoroutineScope
    ) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.candidateDao())
                }
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, coroutineScope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "VitesseDB"
                )
                    .addCallback(AppDatabaseCallback(coroutineScope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        suspend fun populateDatabase(candidateDao: CandidateDao) {
            candidateDao.updateCandidate(
                CandidateDto(
                    firstName = "Emma",
                    lastName = "Durand",
                    phone = "0601020304",
                    email = "emma.durand@example.com",
                    isFavorite = false,
                    photoUri = ""
                )
            )

            candidateDao.updateCandidate(
                CandidateDto(
                    firstName = "Lucas",
                    lastName = "Martin",
                    phone = "0611223344",
                    email = "lucas.martin@example.com",
                    isFavorite = false,
                    photoUri = ""
                )
            )

            candidateDao.updateCandidate(
                CandidateDto(
                    firstName = "Chloé",
                    lastName = "Bernard",
                    phone = "0622334455",
                    email = "chloe.bernard@example.com",
                    isFavorite = false,
                    photoUri = ""
                )
            )

            candidateDao.updateCandidate(
                CandidateDto(
                    firstName = "Nathan",
                    lastName = "Robert",
                    phone = "0633445566",
                    email = "nathan.robert@example.com",
                    isFavorite = false,
                    photoUri = ""
                )
            )

            candidateDao.updateCandidate(
                CandidateDto(
                    firstName = "Léa",
                    lastName = "Petit",
                    phone = "0644556677",
                    email = "lea.petit@example.com",
                    isFavorite = false,
                    photoUri = ""
                )
            )

            candidateDao.updateCandidate(
                CandidateDto(
                    firstName = "Hugo",
                    lastName = "Richard",
                    phone = "0655667788",
                    email = "hugo.richard@example.com",
                    isFavorite = false,
                    photoUri = ""
                )
            )

            candidateDao.updateCandidate(
                CandidateDto(
                    firstName = "Camille",
                    lastName = "Garcia",
                    phone = "0666778899",
                    email = "camille.garcia@example.com",
                    isFavorite = false,
                    photoUri = ""
                )
            )

        }
    }
}