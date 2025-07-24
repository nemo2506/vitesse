package com.openclassrooms.vitesse.data.database

import android.content.Context
import android.util.Log
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
            candidateDao.upsertCandidateWithDetail(
                CandidateWithDetailDto(
                    candidateDto = Candidate(
                        firstName = "Lucas",
                        lastName = "Martin",
                        isFavorite = true,
                        note = "A demandé s’il y avait un code vestimentaire pour le télétravail.",
                        photoUri = "https://xsgames.co/randomusers/assets/avatars/male/3.jpg"
                    ).toDto(),
                    detailDto = Detail(
                        date = LocalDateTime.of(2006, 7, 15, 14, 0),
                        salaryClaim = 10000,
                        phone = "0655123498",
                        email = "julien.moreau@example.com",
                        candidateId = 0L
                    ).toDto()
                )
            )

            candidateDao.upsertCandidateWithDetail(
                CandidateWithDetailDto(
                    candidateDto = Candidate(
                        firstName = "Chloé",
                        lastName = "Bernard",
                        isFavorite = false,
                        note = "A confondu l'entretien avec un one-man-show. Pas sûr du poste, mais on a bien ri.",
                        photoUri = "https://xsgames.co/randomusers/assets/avatars/female/4.jpg"
                    ).toDto(),
                    detailDto = Detail(
                        date = LocalDateTime.of(2005, 7, 15, 11, 30),
                        salaryClaim = 12000,
                        phone = "9999999999",
                        email = "joe.dalton@example.com",
                        candidateId = 0L
                    ).toDto()
                )
            )

            candidateDao.upsertCandidateWithDetail(
                CandidateWithDetailDto(
                    candidateDto = Candidate(
                        firstName = "Nathan",
                        lastName = "Robert",
                        isFavorite = false,
                        note = "Sait tout faire, sauf rester modeste. On embauche peut-être un super-héros.",
                        photoUri = "https://xsgames.co/randomusers/assets/avatars/male/4.jpg"
                    ).toDto(),
                    detailDto = Detail(
                        date = LocalDateTime.of(2004, 7, 15, 10, 0),
                        salaryClaim = 8000,
                        phone = "0666778899",
                        email = "camille.garcia@example.com",
                        candidateId = 0L
                    ).toDto()
                )
            )

            candidateDao.upsertCandidateWithDetail(
                CandidateWithDetailDto(
                    candidateDto = Candidate(
                        firstName = "Léa",
                        lastName = "Petit",
                        isFavorite = false,
                        note = "Est arrivé avec un CV, un café... et sa mère. Esprit d’équipe très… familial.",
                        photoUri = "https://xsgames.co/randomusers/assets/avatars/female/5.jpg"
                    ).toDto(),
                    detailDto = Detail(
                        date = LocalDateTime.now(),
                        salaryClaim = 8000,
                        phone = "0655667788",
                        email = "hugo.richard@example.com",
                        candidateId = 0L
                    ).toDto()
                )
            )

            candidateDao.upsertCandidateWithDetail(
                CandidateWithDetailDto(
                    candidateDto = Candidate(
                        firstName = "Hugo",
                        lastName = "Richard",
                        isFavorite = false,
                        note = "A demandé s’il y avait une prime de sieste. On admire l’honnêteté, mais bon…",
                        photoUri = "https://xsgames.co/randomusers/assets/avatars/male/5.jpg"
                    ).toDto(),
                    detailDto = Detail(
                        date = LocalDateTime.of(2002, 6, 15, 8, 30),
                        salaryClaim = 1,
                        phone = "0644556677",
                        email = "lea.petit@example.com",
                        candidateId = 0L
                    ).toDto()
                )
            )

            candidateDao.upsertCandidateWithDetail(
                CandidateWithDetailDto(
                    candidateDto = Candidate(
                        firstName = "Camille",
                        lastName = "Garcia",
                        isFavorite = false,
                        note = "Maîtrise parfaite des frameworks. A mentionné « Clean Architecture » 12 fois.",
                        photoUri = "https://xsgames.co/randomusers/assets/avatars/female/6.jpg"
                    ).toDto(),
                    detailDto = Detail(
                        date = LocalDateTime.of(2001, 1, 3, 8, 30),
                        salaryClaim = 5000,
                        phone = "0633445566",
                        email = "nathan.robert@example.com",
                        candidateId = 0L
                    ).toDto()
                )
            )

            candidateDao.upsertCandidateWithDetail(
                CandidateWithDetailDto(
                    candidateDto = Candidate(
                        firstName = "Joe",
                        lastName = "Dalton",
                        isFavorite = false,
                        note = "Répondait à nos questions avant qu’on les pose. Très proactif, ou un peu médium.",
                        photoUri = "https://xsgames.co/randomusers/assets/avatars/male/6.jpg"
                    ).toDto(),
                    detailDto = Detail(
                        date = LocalDateTime.of(2005, 3, 15, 9, 30),
                        salaryClaim = 3000,
                        phone = "0622334455",
                        email = "chloe.bernard@example.com",
                        candidateId = 0L
                    ).toDto()
                )
            )

            candidateDao.upsertCandidateWithDetail(
                CandidateWithDetailDto(
                    candidateDto = Candidate(
                        firstName = "Julien",
                        lastName = "Moreau",
                        isFavorite = true,
                        note = "Répondait à nos questions avant qu’on les pose. Très proactif, ou un peu médium.",
                        photoUri = "https://xsgames.co/randomusers/assets/avatars/male/8.jpg"
                    ).toDto(),
                    detailDto = Detail(
                        date = LocalDateTime.of(2003, 5, 15, 15, 15),
                        salaryClaim = 1500,
                        phone = "0611223344",
                        email = "lucas.martin@example.com",
                        candidateId = 0L
                    ).toDto()
                )
            )

            candidateDao.upsertCandidateWithDetail(
                CandidateWithDetailDto(
                    candidateDto = Candidate(
                        firstName = "Sophie",
                        lastName = "Dubois",
                        isFavorite = false,
                        note = "Parle 5 langues, sauf celle du silence. Un vrai podcast ambulant.",
                        photoUri = "https://xsgames.co/randomusers/assets/avatars/female/9.jpg"
                    ).toDto(),
                    detailDto = Detail(
                        date = LocalDateTime.of(2000, 7, 15, 8, 30),
                        salaryClaim = 10000,
                        phone = "0601020304",
                        email = "emma.durand@example.com",
                        candidateId = 0L
                    ).toDto()
                )
            )

        }
    }
}