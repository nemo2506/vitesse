package com.openclassrooms.vitesse.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.openclassrooms.vitesse.data.converter.Converters
import com.openclassrooms.vitesse.data.dao.CandidateDao
import com.openclassrooms.vitesse.data.dao.DetailDao
import com.openclassrooms.vitesse.data.entity.CandidateDto
import com.openclassrooms.vitesse.data.entity.DetailDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@Database(
    entities = [CandidateDto::class, DetailDto::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun candidateDao(): CandidateDao
    abstract fun detailDao(): DetailDao

    private class AppDatabaseCallback(
        private val scope: CoroutineScope
    ) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.candidateDao(), database.detailDao())
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

        suspend fun populateDatabase(candidateDao: CandidateDao, detailDao: DetailDao) {

            candidateDao.updateCandidate(
                CandidateDto(
                    firstName = "Emma",
                    lastName = "Durand",
                    phone = "0601020304",
                    email = "emma.durand@example.com",
                    isFavorite = true,
                    photoUri = "https://xsgames.co/randomusers/assets/avatars/female/3.jpg"
                )
            )

            candidateDao.updateCandidate(
                CandidateDto(
                    firstName = "Lucas",
                    lastName = "Martin",
                    phone = "0611223344",
                    email = "lucas.martin@example.com",
                    isFavorite = true,
                    photoUri = "https://xsgames.co/randomusers/assets/avatars/male/3.jpg"
                )
            )

            candidateDao.updateCandidate(
                CandidateDto(
                    firstName = "Chloé",
                    lastName = "Bernard",
                    phone = "0622334455",
                    email = "chloe.bernard@example.com",
                    isFavorite = false,
                    photoUri = "https://xsgames.co/randomusers/assets/avatars/female/4.jpg"
                )
            )

            candidateDao.updateCandidate(
                CandidateDto(
                    firstName = "Nathan",
                    lastName = "Robert",
                    phone = "0633445566",
                    email = "nathan.robert@example.com",
                    isFavorite = false,
                    photoUri = "https://xsgames.co/randomusers/assets/avatars/male/4.jpg"
                )
            )

            candidateDao.updateCandidate(
                CandidateDto(
                    firstName = "Léa",
                    lastName = "Petit",
                    phone = "0644556677",
                    email = "lea.petit@example.com",
                    isFavorite = false,
                    photoUri = "https://xsgames.co/randomusers/assets/avatars/female/5.jpg"
                )
            )

            candidateDao.updateCandidate(
                CandidateDto(
                    firstName = "Hugo",
                    lastName = "Richard",
                    phone = "0655667788",
                    email = "hugo.richard@example.com",
                    isFavorite = false,
                    photoUri = "https://xsgames.co/randomusers/assets/avatars/male/5.jpg"
                )
            )

            candidateDao.updateCandidate(
                CandidateDto(
                    firstName = "Camille",
                    lastName = "Garcia",
                    phone = "0666778899",
                    email = "camille.garcia@example.com",
                    isFavorite = false,
                    photoUri = "https://xsgames.co/randomusers/assets/avatars/female/6.jpg"
                )
            )
            candidateDao.updateCandidate(
                CandidateDto(
                    firstName = "Joe",
                    lastName = "Dalton",
                    phone = "9999999999",
                    email = "joe.dalton@example.com",
                    isFavorite = false,
                    photoUri = "https://xsgames.co/randomusers/assets/avatars/male/6.jpg"
                )
            )

            candidateDao.updateCandidate(
                CandidateDto(
                    firstName = "Julien",
                    lastName = "Moreau",
                    phone = "0655123498",
                    email = "julien.moreau@example.com",
                    isFavorite = true,
                    photoUri = "https://xsgames.co/randomusers/assets/avatars/male/8.jpg"
                )
            )

            candidateDao.updateCandidate(
                CandidateDto(
                    firstName = "Sophie",
                    lastName = "Dubois",
                    phone = "0644332211",
                    email = "sophie.dubois@example.com",
                    isFavorite = false,
                    photoUri = "https://xsgames.co/randomusers/assets/avatars/female/9.jpg"
                )
            )


            detailDao.updateDetail(
                DetailDto(
                    date = LocalDateTime.of(2001, 7, 15, 8, 30),
                    salaryClaim = 10000,
                    note = "A confondu l'entretien avec un one-man-show. Pas sûr du poste, mais on a bien ri.",
                    candidateId = 1
                )
            )
            detailDao.updateDetail(
                DetailDto(
                    date = LocalDateTime.of(2003, 5, 15, 15, 15),
                    salaryClaim = 1500,
                    note = "Sait tout faire, sauf rester modeste. On embauche peut-être un super-héros.",
                    candidateId = 2
                )
            )
            detailDao.updateDetail(
                DetailDto(
                    date = LocalDateTime.of(2005, 3, 15, 9, 30),
                    salaryClaim = 3000,
                    note = "Est arrivé avec un CV, un café... et sa mère. Esprit d’équipe très… familial.",
                    candidateId = 3
                )
            )
            detailDao.updateDetail(
                DetailDto(
                    date = LocalDateTime.of(2001, 1, 3, 8, 30),
                    salaryClaim = 5000,
                    note = "A demandé s’il y avait une prime de sieste. On admire l’honnêteté, mais bon…",
                    candidateId = 4
                )
            )
            detailDao.updateDetail(
                DetailDto(
                    date = LocalDateTime.of(2002, 6, 15, 8, 30),
                    salaryClaim = 1,
                    note = "Parle 5 langues, sauf celle du silence. Un vrai podcast ambulant.",
                    candidateId = 5
                )
            )
            detailDao.updateDetail(
                DetailDto(
                    date = LocalDateTime.now(),
                    salaryClaim = 8000,
                    note = "Répondait à nos questions avant qu’on les pose. Très proactif, ou un peu médium.",
                    candidateId = 6
                )
            )
            detailDao.updateDetail(
                DetailDto(
                    date = LocalDateTime.of(2025, 7, 15, 10, 0),
                    salaryClaim = 8000,
                    note = "Répondait à nos questions avant qu’on les pose. Très proactif, ou un peu médium.",
                    candidateId = 7
                )
            )

            detailDao.updateDetail(
                DetailDto(
                    date = LocalDateTime.of(2025, 7, 15, 11, 30),
                    salaryClaim = 12000,
                    note = "Maîtrise parfaite des frameworks. A mentionné « Clean Architecture » 12 fois.",
                    candidateId = 8
                )
            )

            detailDao.updateDetail(
                DetailDto(
                    date = LocalDateTime.of(2025, 7, 15, 14, 0),
                    salaryClaim = 10000,
                    note = "Est venu avec un PowerPoint intitulé ‘Pourquoi moi ?’. Convaincant.",
                    candidateId = 9
                )
            )

            detailDao.updateDetail(
                DetailDto(
                    date = LocalDateTime.of(2025, 7, 15, 15, 45),
                    salaryClaim = 9500,
                    note = "A demandé s’il y avait un code vestimentaire pour le télétravail.",
                    candidateId = 10
                )
            )

        }
    }
}