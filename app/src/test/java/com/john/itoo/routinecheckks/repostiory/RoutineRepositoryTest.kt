package com.john.itoo.androidbaseprojectkt.repostiory

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.john.itoo.routinecheckks.app.RoutineRepository
import com.john.itoo.routinecheckks.app.models.*
import com.john.itoo.routinecheckks.utils.TimeUtils

import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.util.*

@RunWith(JUnit4::class)
class RoutineRepositoryTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()
    private lateinit var repository: RoutineRepository
    private val routineDao = mock(RoutineDao::class.java)
    @UseExperimental(ObsoleteCoroutinesApi::class)
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")
    private val database = mock(RoutineDatabase::class.java)
    val data =
        Routine(0, "viola", "viola", 1, Date(), Date(), Date(), 1)
    private var dbRoutine: DatabaseRoutine? = null


    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(mainThreadSurrogate)
        `when`(database.routineDao).thenReturn(routineDao)
        `when`(database.runInTransaction(ArgumentMatchers.any())).thenCallRealMethod()
        repository = RoutineRepository(database, TimeUtils())
        dbRoutine = data.asDbRoutine()
//        `when`(routineDao.insert(dbRoutine as DatabaseRoutine)).thenReturn(dbRoutine as DatabaseRoutine)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun testGetSingleRoutine() {
        runBlocking {
            launch(Dispatchers.Main) {
                repository.getSingleRoutine(data)
            }
        }
        verify(routineDao, times(1)).getRoutine(data.date)
    }

    private fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    private fun <T> uninitialized(): T = null as T

    @Test
    fun testInsert() {
        runBlocking {
            launch(Dispatchers.Main) {
                repository.insert(data.asDbRoutine())
            }
        }
        verify(routineDao, times(1)).insert(isA(DatabaseRoutine::class.java))

    }
}