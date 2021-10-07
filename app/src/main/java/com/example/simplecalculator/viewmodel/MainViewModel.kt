package com.example.simplecalculator.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.simplecalculator.model.Action
import com.example.simplecalculator.model.Calculation
import com.example.simplecalculator.model.Repository

class MainViewModel(private val repository: Repository = Repository) : ViewModel() {

    private val viewStateLiveData: MutableLiveData<CalculationViewState> = MutableLiveData()

    private val _currentValueLiveData: MutableLiveData<String> = MutableLiveData("")
    private val _historyValueLiveData: MutableLiveData<String> = MutableLiveData("")
    private val _errorValueLiveData: MutableLiveData<Boolean> = MutableLiveData(false)

    private var firstArgument = 0.0
    private var secondArgument = 0.0
    private var result = 0.0
    private var temp = 0.0
    private var action = Action.NO_ACTIONS
    private var isFirstAction = true

    init {
        Repository.getCalculations().observeForever { calculations ->
            viewStateLiveData.value =
                viewStateLiveData.value?.copy(calculations = calculations) ?: CalculationViewState(
                    calculations
                )
        }
    }

    fun viewState(): LiveData<CalculationViewState> = viewStateLiveData
    val currentValueLiveData: LiveData<String> = _currentValueLiveData
    val historyValueLiveData: LiveData<String> = _historyValueLiveData
    val errorValueLiveData: LiveData<Boolean> = _errorValueLiveData

    private var pendingCalculation: Calculation? = null

    fun numberPressed(number: String) {
        _currentValueLiveData.value = _currentValueLiveData.value + number
    }

    fun dotPressed(dot: String) {
        if (_currentValueLiveData.value?.contains(".") != true)
            _currentValueLiveData.value = _currentValueLiveData.value + dot
    }

    private fun plusPressed() {
        result = firstArgument + secondArgument
        action = Action.PLUS
    }

    private fun minusPressed() {
        result = firstArgument - secondArgument
        action = Action.MINUS
    }

    private fun multiplyPressed() {
        result = firstArgument * secondArgument
        action = Action.MULTIPLY
    }

    private fun dividePressed() {
        if (secondArgument != 0.0) {
            result = firstArgument / secondArgument
            action = Action.DIVIDE
        } else {
            _errorValueLiveData.value = true
        }
        _errorValueLiveData.value = false
    }

    fun erasePressed() {
        _currentValueLiveData.value = _currentValueLiveData.value?.dropLast(1)
    }

    fun cancelPressed() {
        _currentValueLiveData.value = ""
        firstArgument = 0.0
        secondArgument = 0.0
        result = 0.0
        temp = 0.0
        action = Action.NO_ACTIONS
        isFirstAction = true
    }

    private fun equallyPressed() {
        when (action) {
            Action.PLUS -> plusPressed()
            Action.MINUS -> minusPressed()
            Action.DIVIDE -> dividePressed()
            Action.MULTIPLY -> multiplyPressed()
            Action.EQUALLY -> {
                action = Action.EQUALLY
                return
            }
            Action.NO_ACTIONS -> return
        }
    }

    fun actionPressed(action: Action) {
        if (isFirstAction) {
            if (action == Action.EQUALLY) {
                return
            }
            if (_currentValueLiveData.value == "") {
                return
            }
            this.action = action
            firstArgument = _currentValueLiveData.value?.toDouble() ?: 0.0
            _currentValueLiveData.value = ""
            isFirstAction = false
        } else {
            if (_currentValueLiveData.value == "") {
                return
            }
            //this.action = action
            secondArgument = _currentValueLiveData.value?.toDouble() ?: 0.0
            _currentValueLiveData.value = ""
            // isFirstAction = true
            when (action) {
                Action.PLUS -> plusPressed()
                Action.MINUS -> minusPressed()
                Action.DIVIDE -> dividePressed()
                Action.MULTIPLY -> multiplyPressed()
                Action.EQUALLY -> equallyPressed()
                Action.NO_ACTIONS -> return
            }
            showResult()
        }
    }

    private fun showResult() {
        _currentValueLiveData.value = result.toString()
        _historyValueLiveData.value =
            firstArgument.toString() +
                    actionToString(action) +
                    secondArgument.toString() +
                    "=" +
                    result.toString()
        firstArgument = result
        secondArgument = 0.0
        result = 0.0
        isFirstAction = true
    }

    fun inversionPressed() {
        if (_currentValueLiveData.value != "" && _currentValueLiveData.value != ".") {
            temp = _currentValueLiveData.value?.toDouble() ?: 0.0
            temp = -temp
            _currentValueLiveData.value = temp.toString()
        }
    }


    fun saveChanges(calculation: Calculation) {
        pendingCalculation = calculation
    }

    override fun onCleared() {
        if (pendingCalculation != null) {
            repository.saveCalculation(pendingCalculation!!)
        }
        super.onCleared()
    }

    private fun actionToString(action: Action): String {
        when (action) {
            Action.PLUS -> return "+"
            Action.MINUS -> return "-"
            Action.DIVIDE -> return "/"
            Action.MULTIPLY -> return "*"
            Action.EQUALLY -> return "="
            Action.NO_ACTIONS -> return ""
        }
    }
}