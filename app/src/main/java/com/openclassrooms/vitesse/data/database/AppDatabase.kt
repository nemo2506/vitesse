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
                        lastName = "DUPONT",
                        isFavorite = true,
                        note = "Apprend vite, curieuse et autonome. Bonne capacité d’analyse. Travaille bien en équipe. Solide éthique professionnelle. Très bon potentiel évolutif.",
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
                        lastName = "KHALIL",
                        isFavorite = false,
                        note = "Très sociable et observateur. S’intègre bien. Réagit bien sous pression. Bon esprit, indépendant et fiable sur ses responsabilités.",
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
                        lastName = "MARTIN",
                        isFavorite = true,
                        note = "Toujours dynamique et souriante. Apporte de bonnes idées, contribue à la cohésion d’équipe. Excellente communication et sens des priorités.",
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
                        lastName = "ROCHE",
                        isFavorite = false,
                        note = "Analyse précise des problèmes. Organisé, structuré et rigoureux dans son travail. Peu de supervision nécessaire. Fait preuve d’initiative régulièrement.",
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
                        lastName = "EL AMRANI",
                        isFavorite = true,
                        note = "Professionnelle exemplaire. Répond toujours présente. Favorise une ambiance de travail sereine et efficace. Sait gérer plusieurs priorités sans stress.",
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
                        lastName = "BERTRAND",
                        isFavorite = false,
                        note = "Bon leadership technique. Hésite parfois à déléguer. Très impliqué mais doit encore améliorer sa gestion du temps et feedback.",
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
                        lastName = "LECLERC",
                        isFavorite = true,
                        note = "Force de proposition créative. Manque parfois de méthode. S’améliore rapidement. Besoin d’un cadre clair pour être pleinement performant.",
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
                        lastName = "DURAND",
                        isFavorite = false,
                        note = "Bonne maîtrise technique. Peu communicatif en réunion. Fiable sur les livrables. Devrait s’impliquer davantage dans les projets transversaux d’équipe.",
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
                        lastName = "MOREAU",
                        isFavorite = true,
                        note = "Charismatique, prend naturellement les devants. Sait motiver l’équipe. Gère le stress efficacement. Peut encore progresser en écoute active.",
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