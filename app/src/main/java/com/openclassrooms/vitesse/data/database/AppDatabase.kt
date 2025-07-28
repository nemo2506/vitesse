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
                        firstName = "Lucie",
                        lastName = "Dupont",
                        isFavorite = true,
                        note = "Très proactive, propose souvent des améliorations pertinentes.",
                        photoUri = "https://miseservice.ovh/media/lucie.jpg"
                    ).toDto(),
                    detailDto = Detail(
                        date = LocalDateTime.of(1991, 3, 22, 9, 30),
                        salaryClaim = 32000,
                        phone = "0601020304",
                        email = "lucie.dupont@email.com",
                        candidateId = 0L
                    ).toDto()
                )
            )
            candidateDao.upsertCandidateAll(
                CandidateWithDetailDto(
                    candidateDto = Candidate(
                        firstName = "Ahmed",
                        lastName = "Khalil",
                        isFavorite = false,
                        note = "Esprit analytique. Excellente rigueur en gestion de projet.",
                        photoUri = "https://miseservice.ovh/media/ahmed.jpg"
                    ).toDto(),
                    detailDto = Detail(
                        date = LocalDateTime.of(1986, 11, 2, 16, 15),
                        salaryClaim = 45000,
                        phone = "0611223344",
                        email = "ahmed.khalil@pro.com",
                        candidateId = 0L
                    ).toDto()
                )
            )
            candidateDao.upsertCandidateAll(
                CandidateWithDetailDto(
                    candidateDto = Candidate(
                        firstName = "Claire",
                        lastName = "Martin",
                        isFavorite = true,
                        note = "Toujours ponctuelle et fiable. Très bonne ambiance avec l'équipe.",
                        photoUri = "https://miseservice.ovh/media/claire.jpg"
                    ).toDto(),
                    detailDto = Detail(
                        date = LocalDateTime.of(1994, 5, 10, 12, 0),
                        salaryClaim = 37000,
                        phone = "0612345678",
                        email = "claire.martin@email.fr",
                        candidateId = 0L
                    ).toDto()
                )
            )
            candidateDao.upsertCandidateAll(
                CandidateWithDetailDto(
                    candidateDto = Candidate(
                        firstName = "Julien",
                        lastName = "Roche",
                        isFavorite = false,
                        note = "Bonne capacité d’adaptation. A tendance à trop vouloir tout contrôler.",
                        photoUri = "https://miseservice.ovh/media/julien.jpg"
                    ).toDto(),
                    detailDto = Detail(
                        date = LocalDateTime.of(1989, 9, 5, 10, 0),
                        salaryClaim = 39000,
                        phone = "0677889900",
                        email = "julien.roche@openmail.fr",
                        candidateId = 0L
                    ).toDto()
                )
            )
            candidateDao.upsertCandidateAll(
                CandidateWithDetailDto(
                    candidateDto = Candidate(
                        firstName = "Fatima",
                        lastName = "El Amrani",
                        isFavorite = true,
                        note = "Excellente communicante. Appréciée par les clients et les collègues.",
                        photoUri = "https://miseservice.ovh/media/fatima.jpg"
                    ).toDto(),
                    detailDto = Detail(
                        date = LocalDateTime.of(1990, 12, 25, 8, 0),
                        salaryClaim = 48000,
                        phone = "0655443322",
                        email = "fatima.elamrani@mail.fr",
                        candidateId = 0L
                    ).toDto()
                )
            )
            candidateDao.upsertCandidateAll(
                CandidateWithDetailDto(
                    candidateDto = Candidate(
                        firstName = "Thomas",
                        lastName = "Bertrand",
                        isFavorite = false,
                        note = "Profil technique solide. Peu d’initiatives en dehors de ses tâches.",
                        photoUri = "https://miseservice.ovh/media/thomas.jpg"
                    ).toDto(),
                    detailDto = Detail(
                        date = LocalDateTime.of(1985, 2, 8, 15, 45),
                        salaryClaim = 42000,
                        phone = "0688997766",
                        email = "thomas.bertrand@workplace.fr",
                        candidateId = 0L
                    ).toDto()
                )
            )
            candidateDao.upsertCandidateAll(
                CandidateWithDetailDto(
                    candidateDto = Candidate(
                        firstName = "Sophie",
                        lastName = "Leclerc",
                        isFavorite = true,
                        note = "Leader naturel. Motive les autres. Excellente gestion du stress.",
                        photoUri = "https://miseservice.ovh/media/sophie.jpg"
                    ).toDto(),
                    detailDto = Detail(
                        date = LocalDateTime.of(1993, 6, 17, 11, 0),
                        salaryClaim = 51000,
                        phone = "0622334455",
                        email = "sophie.leclerc@jobmail.com",
                        candidateId = 0L
                    ).toDto()
                )
            )
            candidateDao.upsertCandidateAll(
                CandidateWithDetailDto(
                    candidateDto = Candidate(
                        firstName = "Nathan",
                        lastName = "Durand",
                        isFavorite = false,
                        note = "Créatif mais manque parfois de rigueur dans les délais.",
                        photoUri = "https://miseservice.ovh/media/nathan.jpg"
                    ).toDto(),
                    detailDto = Detail(
                        date = LocalDateTime.of(
                            1996,
                            8,
                            30,
                            13,
                            15
                        ),
                        salaryClaim = 35000,
                        phone = "0699887766",
                        email = "nathan.durand@email.org",
                        candidateId = 0L
                    ).toDto()
                )
            )
            candidateDao.upsertCandidateAll(
                CandidateWithDetailDto(
                    candidateDto = Candidate(
                        firstName = "Léa",
                        lastName = "Moreau",
                        isFavorite = true,
                        note = "Très bon relationnel. Curieuse et toujours prête à apprendre.",
                        photoUri = "https://miseservice.ovh/media/lea.jpg"
                    ).toDto(),
                    detailDto = Detail(
                        date = LocalDateTime.of(
                            1992,
                            1,
                            4,
                            10,
                            30
                        ),
                        salaryClaim = 40000,
                        phone = "0633221100",
                        email = "lea.moreau@live.fr",
                        candidateId = 0L
                    ).toDto()
                )
            )
        }
    }
}