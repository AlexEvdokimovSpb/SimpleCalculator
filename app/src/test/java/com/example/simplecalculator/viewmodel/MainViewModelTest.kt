package com.example.simplecalculator.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.simplecalculator.model.Action
import com.example.simplecalculator.model.Calculation
import com.example.simplecalculator.model.Repository
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.util.*

class MainViewModelTest {

    private lateinit var viewModel: MainViewModel

    private var id: String = UUID.randomUUID().toString()
    private var firstArgument = 0.0
    private var secondArgument = 0.0
    private var result = 0.0
    private var action = Action.NO_ACTIONS
    private var pendingCalculation = Calculation(id, firstArgument, secondArgument, result, action)

    @Mock
    private lateinit var repository: Repository

    @Mock
    private lateinit var calculationsLiveData: MutableLiveData<List<Calculation>>

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this).close()
        viewModel = MainViewModel(repository = Repository)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `verify repository has getCalculations()`() {
        repository.getCalculations()
        verify(repository, times(1)).getCalculations()
    }

    @Test
    fun `verify getCalculations() return calculationsLiveData`() {
        `when`(repository.getCalculations()).thenReturn(calculationsLiveData)
        assertEquals(calculationsLiveData, repository.getCalculations())
    }

    @Test
    fun `verify repository has saveCalculation()`() {
        repository.saveCalculation(pendingCalculation)
        verify(repository, times(1)).saveCalculation(pendingCalculation)
    }

    @Test
    fun `should return the entered number`() {
        val expectedData = "1"
        val testData = "1"
        viewModel.numberPressed(testData)
        val actualData: String? = viewModel.currentValueLiveData.value
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `should return the entered number but not`() {
        val expectedData = "1"
        val testData = "2"
        viewModel.numberPressed(testData)
        val actualData: String? = viewModel.currentValueLiveData.value
        assertNotEquals(expectedData, actualData)
    }

    @Test
    fun `should return number with dot`() {
        val expectedData = "1."
        val testData = "1"
        viewModel.numberPressed(testData)
        viewModel.dotPressed(".")
        val actualData: String? = viewModel.currentValueLiveData.value
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `should return erase testData`() {
        val expectedData = "12"
        val testData = "123"
        viewModel.numberPressed(testData)
        viewModel.erasePressed()
        val actualData: String? = viewModel.currentValueLiveData.value
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `should return cancel testData`() {
        val expectedData = ""
        val testData = "1234567"
        viewModel.numberPressed(testData)
        viewModel.cancelPressed()
        val actualData: String? = viewModel.currentValueLiveData.value
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `should return the sum of the numbers`() {
        val expectedData = "4.0"
        val testData1 = "1"
        val testData2 = "3"
        viewModel.cancelPressed()
        viewModel.numberPressed(testData1)
        viewModel.actionPressed(Action.PLUS)
        viewModel.numberPressed(testData2)
        viewModel.actionPressed(Action.EQUALLY)
        val actualData: String? = viewModel.currentValueLiveData.value
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `division by zero is not possible`() {
        val expectedData = "0.0"
        val testData1 = "42"
        val testData2 = "0"
        viewModel.cancelPressed()
        viewModel.numberPressed(testData1)
        viewModel.actionPressed(Action.DIVIDE)
        viewModel.numberPressed(testData2)
        viewModel.actionPressed(Action.EQUALLY)
        val actualData: String? = viewModel.currentValueLiveData.value
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `should return the minus of the numbers`() {
        val expectedData = "1.0"
        val testData1 = "2"
        val testData2 = "1"
        viewModel.cancelPressed()
        viewModel.numberPressed(testData1)
        viewModel.actionPressed(Action.MINUS)
        viewModel.numberPressed(testData2)
        viewModel.actionPressed(Action.EQUALLY)
        val actualData: String? = viewModel.currentValueLiveData.value
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `should return the divide of the numbers`() {
        val expectedData = "21.0"
        val testData1 = "42"
        val testData2 = "2"
        viewModel.cancelPressed()
        viewModel.numberPressed(testData1)
        viewModel.actionPressed(Action.DIVIDE)
        viewModel.numberPressed(testData2)
        viewModel.actionPressed(Action.EQUALLY)
        val actualData: String? = viewModel.currentValueLiveData.value
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `should return the multiply of the numbers`() {
        val expectedData = "84.0"
        val testData1 = "42"
        val testData2 = "2"
        viewModel.cancelPressed()
        viewModel.numberPressed(testData1)
        viewModel.actionPressed(Action.MULTIPLY)
        viewModel.numberPressed(testData2)
        viewModel.actionPressed(Action.EQUALLY)
        val actualData: String? = viewModel.currentValueLiveData.value
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `should change the sign`() {
        val expectedData = "-1.0"
        val testData = "1"
        viewModel.numberPressed(testData)
        viewModel.inversionPressed()
        val actualData: String? = viewModel.currentValueLiveData.value
        assertEquals(expectedData, actualData)
    }
}