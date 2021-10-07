package com.example.simplecalculator.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.*

object Repository {
    private val calculationsLiveData = MutableLiveData<List<Calculation>>()

    private val calculations: MutableList<Calculation> = mutableListOf(
        Calculation(
            id = UUID.randomUUID().toString(),
            firstArgument = 10.0,
            secondArgument = 20.0,
            result = 30.0,
            action = Action.PLUS
        ),
        Calculation(
            id = UUID.randomUUID().toString(),
            firstArgument = 1.0,
            secondArgument = 2.0,
            result = 2.0,
            action = Action.MULTIPLY
        )
    )

    init {
        calculationsLiveData.value = calculations
    }

    fun getCalculations(): LiveData<List<Calculation>> = calculationsLiveData

    fun saveCalculation(calculation: Calculation) {
        addOrReplace(calculation)
        calculationsLiveData.value = calculations
    }

    private fun addOrReplace(calculation: Calculation) {
        for (i in 0 until calculations.size) {
            if (calculations[i] == calculation) {
                calculations[i] = calculation
                return
            }
        }
        calculations.add(calculation)
    }

}