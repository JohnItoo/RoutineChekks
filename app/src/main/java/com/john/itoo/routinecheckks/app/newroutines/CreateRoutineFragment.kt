package com.john.itoo.routinecheckks.app.newroutines

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.john.itoo.routinecheckks.App

import com.john.itoo.routinecheckks.base.BaseFragment
import com.john.itoo.routinecheckks.databinding.CreateRoutineFragmentBinding
import com.john.itoo.routinecheckks.app.models.Routine
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class CreateRoutineFragment : BaseFragment(), AdapterView.OnItemSelectedListener {

    private var frequency = "Hourly"
    private var date = Date()
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var binding: CreateRoutineFragmentBinding

    companion object {
        fun newInstance() = CreateRoutineFragment()
        val frequencySet = mutableListOf("Hourly", "Daily", "Weekly", "Monthly", "Yearly")
    }

    private lateinit var viewModel: CreateRoutineViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CreateRoutineFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity.setUpToolBar("Create New Routine", true)
        (mainActivity.applicationContext as App).component.inject(this)

        val viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(CreateRoutineViewModel::class.java)
        binding.viewModel = viewModel
        binding.time.setOnClickListener {

            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                date = cal.time
                binding.time.text = SimpleDateFormat("HH:mm").format(date)
            }
            TimePickerDialog(
                this.context,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()

        }
        binding.frequency.setOnItemSelectedListener(this)
        val adapter =
            ArrayAdapter(this.context!!, android.R.layout.simple_spinner_item, frequencySet)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.frequency.adapter = adapter

        binding.viewDetailsButton.setOnClickListener {
            if (binding.description.text.isEmpty() || binding.title.text.isEmpty()) {
                Snackbar.make(this.view!!, "Kindly fill in all fields", Snackbar.LENGTH_LONG).show()
            } else {
                viewModel.insert(
                    Routine(
                        0,
                        binding.description.text.toString(),
                        binding.title.text.toString(),
                        binding.title.text.toString(),
                        frequency,
                        0,
                        0,
                        Date(),
                        date,
                        0,
                        0,
                        0,
                        "To-do"
                    ),
                    this.mainActivity.applicationContext
                )

            }
        }

    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        frequency = frequencySet[p2]
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}