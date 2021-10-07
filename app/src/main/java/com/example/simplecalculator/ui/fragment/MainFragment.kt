package com.example.simplecalculator.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.simplecalculator.databinding.MainFragmentBinding
import com.example.simplecalculator.model.Action
import com.example.simplecalculator.model.Calculation
import com.example.simplecalculator.viewmodel.MainViewModel

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private var calculation: Calculation? = null
    lateinit var ui: MainFragmentBinding
    private lateinit var viewModel: MainViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        ui = MainFragmentBinding.inflate(layoutInflater)
        return ui.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        viewModel.currentValueLiveData.observe(viewLifecycleOwner, Observer {
            ui.currentValueTextField.editText?.setText(it)
        })

        viewModel.historyValueLiveData.observe(viewLifecycleOwner, Observer {
            ui.historyTextView.text = it
        })

        viewModel.errorValueLiveData.observe(viewLifecycleOwner, {
            if (it) {
                divisionByZero()
            }
        })
        
        ui.zeroButton.setOnClickListener { viewModel.numberPressed("0") }
        ui.oneButton.setOnClickListener { viewModel.numberPressed("1") }
        ui.twoButton.setOnClickListener { viewModel.numberPressed("2") }
        ui.threeButton.setOnClickListener { viewModel.numberPressed("3") }
        ui.fourButton.setOnClickListener { viewModel.numberPressed("4") }
        ui.fiveButton.setOnClickListener { viewModel.numberPressed("5") }
        ui.sixButton.setOnClickListener { viewModel.numberPressed("6") }
        ui.sevenButton.setOnClickListener { viewModel.numberPressed("7") }
        ui.eightButton.setOnClickListener { viewModel.numberPressed("8") }
        ui.nineButton.setOnClickListener { viewModel.numberPressed("9") }

        ui.dotButton.setOnClickListener { viewModel.dotPressed(".") }

        ui.plusButton.setOnClickListener { viewModel.actionPressed(Action.PLUS) }
        ui.minusButton.setOnClickListener { viewModel.actionPressed(Action.MINUS) }
        ui.multiplyButton.setOnClickListener { viewModel.actionPressed(Action.MULTIPLY) }
        ui.divideButton.setOnClickListener { viewModel.actionPressed(Action.DIVIDE) }
        ui.equallyButton.setOnClickListener { viewModel.actionPressed(Action.EQUALLY) }

        ui.inversionButton.setOnClickListener { viewModel.inversionPressed() }

        ui.eraseButton.setOnClickListener { viewModel.erasePressed() }
        ui.cancelButton.setOnClickListener { viewModel.cancelPressed() }
    }

    fun divisionByZero() {
        Toast.makeText(requireContext(), "Деление на ноль", Toast.LENGTH_LONG).show()
    }

}