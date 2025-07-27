package com.openclassrooms.vitesse.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.openclassrooms.vitesse.data.converter.Converters
import com.openclassrooms.vitesse.data.dao.CandidateDao
import com.openclassrooms.vitesse.data.entity.CandidateDto
import com.openclassrooms.vitesse.data.entity.CandidateWithDetailDto
import com.openclassrooms.vitesse.data.entity.DetailDto
import com.openclassrooms.vitesse.domain.model.Candidate
import com.openclassrooms.vitesse.domain.model.Detail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@Database(
    entities = [DetailDto::class, CandidateDto::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
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

        suspend fun populateDatabase(
            candidateDao: CandidateDao
        ) {
            candidateDao.upsertCandidateAll(
                CandidateWithDetailDto(
                    candidateDto = Candidate(
                        firstName = "Jonathan",
                        lastName = "DESCAMPS NAVARRO",
                        isFavorite = true,
                        note = "Un mentor dans l'art de la menuiserie.",
                        photoUri = "https://miseservice.ovh/media/jonathan.jpg"
                    ).toDto(),
                    detailDto = Detail(
                        date = LocalDateTime.of(2006, 7, 15, 14, 0),
                        salaryClaim = 10000,
                        phone = "0782089191",
                        email = "jodesnava@gmail.com",
                        candidateId = 0L
                    ).toDto()
                )
            )
            candidateDao.upsertCandidateAll(
                CandidateWithDetailDto(
                    candidateDto = Candidate(
                        firstName = "Océanne",
                        lastName = "CHAPELLE GUICHARD",
                        isFavorite = true,
                        note = "Infirmière spécialisée. On l'appelle le saint-bernard",
                        photoUri = "https://miseservice.ovh/media/oceanne3.jpeg"
                    ).toDto(),
                    detailDto = Detail(
                        date = LocalDateTime.of(2006, 7, 15, 14, 0),
                        salaryClaim = 10000,
                        phone = "0766662089",
                        email = "oceanne@gmail.com",
                        candidateId = 0L
                    ).toDto()
                )
            )
            candidateDao.upsertCandidateAll(
                CandidateWithDetailDto(
                    candidateDto = Candidate(
                        firstName = "Shercan",
                        lastName = "CHIEN",
                        isFavorite = false,
                        note = "Grande ame. Protecteur, calin, raleur",
                        photoUri = "https://miseservice.ovh/media/shercan.jpg"
                    ).toDto(),
                    detailDto = Detail(
                        date = LocalDateTime.of(2006, 7, 15, 14, 0),
                        salaryClaim = 10000,
                        phone = "012121212",
                        email = "shercan@gmail.com",
                        candidateId = 0L
                    ).toDto()
                )
            )
            candidateDao.upsertCandidateAll(
                CandidateWithDetailDto(
                    candidateDto = Candidate(
                        firstName = "Enojado",
                        lastName = "CHAT",
                        isFavorite = false,
                        note = "Aime la compagnie. Independant, Chase le moustique et les fantomes",
                        photoUri = "https://miseservice.ovh/media/enojado.jpeg"
                    ).toDto(),
                    detailDto = Detail(
                        date = LocalDateTime.of(2006, 7, 15, 14, 0),
                        salaryClaim = 10000,
                        phone = "0202020202",
                        email = "enojado@gmail.com",
                        candidateId = 0L
                    ).toDto()
                )
            )
            candidateDao.upsertCandidateAll(
                CandidateWithDetailDto(
                    candidateDto = Candidate(
                        firstName = "Marc",
                        lastName = "NAVARRO",
                        isFavorite = true,
                        note = "Mange des algorythmes. Adore les bonbons salés, court derriere les limaces",
                        photoUri = "https://miseservice.ovh/media/marc2.jpg"
                    ).toDto(),
                    detailDto = Detail(
                        date = LocalDateTime.of(2006, 7, 15, 14, 0),
                        salaryClaim = 10000,
                        phone = "0634058195",
                        email = "navamarc@gmail.com",
                        candidateId = 0L
                    ).toDto()
                )
            )
            candidateDao.upsertCandidateAll(
                CandidateWithDetailDto(
                    candidateDto = Candidate(
                        firstName = "Marie Odile",
                        lastName = "DESCAMPS",
                        isFavorite = true,
                        note = "Cynéphile. Amies des Divas et des VIP",
                        photoUri = "https://miseservice.ovh/media/marie-odile.jpg"
                    ).toDto(),
                    detailDto = Detail(
                        date = LocalDateTime.of(2006, 7, 15, 14, 0),
                        salaryClaim = 10000,
                        phone = "0695389986",
                        email = "descampsmo974@gmail.com",
                        candidateId = 0L
                    ).toDto()
                )
            )
            candidateDao.upsertCandidateAll(
                CandidateWithDetailDto(
                    candidateDto = Candidate(
                        firstName = "Intru",
                        lastName = "FAMILLE" +
                                "",
                        isFavorite = false,
                        note = "Troll. Apparait les jours de froid et très haute température.",
                        photoUri = "https://miseservice.ovh/media/marc3.jpeg"
                    ).toDto(),
                    detailDto = Detail(
                        date = LocalDateTime.of(2006, 7, 15, 14, 0),
                        salaryClaim = 10000,
                        phone = "066666666",
                        email = "troll@gmail.com",
                        candidateId = 0L
                    ).toDto()
                )
            )
        }
    }
}