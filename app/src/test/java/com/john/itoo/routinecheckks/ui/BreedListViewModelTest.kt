//package com.john.itoo.androidbaseprojectkt.ui
//
//import androidx.arch.core.executor.testing.InstantTaskExecutorRule
//import androidx.lifecycle.Observer
//import com.john.itoo.androidbaseprojectkt.sample.ExampleRepository
//import com.john.itoo.androidbaseprojectkt.sample.catlist.RoutineListViewModel
//import com.john.itoo.androidbaseprojectkt.sample.models.Breed
//import junit.framework.Assert.assertEquals
//import junit.framework.Assert.assertNull
//import kotlinx.coroutines.*
//import kotlinx.coroutines.test.resetMain
//import kotlinx.coroutines.test.setMain
//
//import mock
//import org.junit.*
//import org.junit.runner.RunWith
//import org.junit.runners.JUnit4
//import org.mockito.ArgumentCaptor
//import org.mockito.Mockito.*
//
//@RunWith(JUnit4::class)
//class BreedListViewModelTest {
//
//    @Rule
//    @JvmField
//    val instantExecutorRule = InstantTaskExecutorRule()
//
//    private val repository = mock(ExampleRepository::class.java)
//    private var viewModel: RoutineListViewModel? = null
//    private val observer: Observer<Breed> = mock()
//    @UseExperimental(ObsoleteCoroutinesApi::class)
//    private val mainThreadSurrogate = newSingleThreadContext("UI thread")
//
//    @Before
//    fun before() {
//        Dispatchers.setMain(mainThreadSurrogate)
//        viewModel = RoutineListViewModel(repository)
//        viewModel!!.navigateToSelectedRoutine.observeForever(observer)
//    }
//
//    @After
//    fun tearDown() {
//        Dispatchers.resetMain()
//        mainThreadSurrogate.close()
//    }
//
//    @Test
//    fun testNull() {
//        Assert.assertNull(viewModel?.breeds)
//    }
//
//    @Test
//    fun testFetchBreedShouldReturnBreed() {
//        val newBreed = mock<Breed>()
//        runBlocking {
//            launch(Dispatchers.Main) {
//                viewModel!!.displaySelectedRoutineDetails(newBreed)
//            }
//        }
//        val captor = ArgumentCaptor.forClass(Breed::class.java)
//        captor.run {
//            verify(observer, times(1)).onChanged(capture())
//            assertEquals(newBreed, value)
//        }
//    }
//
//    @Test
//    fun testFetchBreedComplete() {
//        runBlocking {
//            launch(Dispatchers.Main) {
//                viewModel!!.displaySelectedRoutineDetailsComplete()
//            }
//        }
//        assertNull(viewModel!!.navigateToSelectedRoutine.value)
//    }
//}
