package com.openclassrooms.vitesse

import com.openclassrooms.vitesse.data.dao.CandidateDao
import com.openclassrooms.vitesse.data.database.AppDatabase
import com.openclassrooms.vitesse.data.network.ManageClient
import com.openclassrooms.vitesse.data.repository.CandidateRepository
import com.openclassrooms.vitesse.data.repository.CurrencyRepository
import com.openclassrooms.vitesse.data.repository.DetailRepository
import com.openclassrooms.vitesse.di.AppModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class AppModuleTest {

    private lateinit var coroutineScope: CoroutineScope
    private lateinit var appDatabase: AppDatabase
    private lateinit var candidateDao: CandidateDao
    private lateinit var manageClient: ManageClient
    private lateinit var appModule: AppModule

    @Before
    fun setup() {
        coroutineScope = CoroutineScope(SupervisorJob())
        appDatabase = mock(AppDatabase::class.java)
        candidateDao = mock(CandidateDao::class.java)
        manageClient = mock(ManageClient::class.java)
        appModule = AppModule()
    }

    @Test
    fun provideCoroutineScope_returns_scope() {
        val scope = appModule.provideCoroutineScope()
        assertNotNull(scope)
    }

    @Test
    fun provideCandidateDao_returns_dao() {
        whenever(appDatabase.candidateDao()).thenReturn(candidateDao)
        val dao = appModule.provideCandidateDao(appDatabase)
        assertEquals(candidateDao, dao)
    }

    @Test
    fun provideCandidateRepository_returns_repository() {
        val repo = appModule.provideCandidateRepository(candidateDao)
        assertNotNull(repo)
    }

    @Test
    fun provideDetailRepository_returns_repository() {
        val repo = appModule.provideDetailRepository(candidateDao)
        assertNotNull(repo)
    }

    @Test
    fun provideCurrencyRepository_returns_repository() {
        val repo = appModule.provideCurrencyRepository(manageClient)
        assertNotNull(repo)
    }

    @Test
    fun provideDetailUseCase_returns_usecase() {
        val detailRepository = mock(DetailRepository::class.java)
        val currencyRepository = mock(CurrencyRepository::class.java)
        val useCase = appModule.provideDetailUseCase(detailRepository, currencyRepository)
        assertNotNull(useCase)
    }

    @Test
    fun provideCandidateUseCase_returns_usecase() {
        val candidateRepository = mock(CandidateRepository::class.java)
        val useCase = appModule.provideCandidateUseCase(candidateRepository)
        assertNotNull(useCase)
    }
}

