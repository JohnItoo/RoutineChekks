package com.john.itoo.routinecheckks.app.newroutines

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.john.itoo.routinecheckks.App

import com.john.itoo.routinecheckks.base.BaseFragment
import com.john.itoo.routinecheckks.databinding.CreateRoutineFragmentBinding
import com.john.itoo.routinecheckks.app.models.Routine
import com.john.itoo.routinecheckks.app.routinedetails.RoutineDetailsFragmentArgs
import com.john.itoo.routinecheckks.databinding.EditRoutineFragmentBinding
import com.john.itoo.routinecheckks.utils.TimeUtils
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class EditRoutineFragment : BaseFragment(), AdapterView.OnItemSelectedListener {

    private var frequency = "Hourly"
    private var date = Date()
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var timeUtils: TimeUtils

    lateinit var binding: EditRoutineFragmentBinding

    companion object {
        fun newInstance() = EditRoutineFragment()
        val frequencySet = mutableListOf("Hourly", "Daily", "Weekly", "Monthly", "Yearly")
    }

    private lateinit var viewModel: EditRoutineViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = EditRoutineFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity.setUpToolBar("Edit Routine")
        (mainActivity.applicationContext as App).component.inject(this)

        val viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(EditRoutineViewModel::class.java)

        val args = EditRoutineFragmentArgs.fromBundle(arguments!!)

        binding.viewModel = viewModel
        binding.routine = args.routine
        date = args.routine.date
        Timber.d(args.routine.toString())
//        timeUtils.setDateTimeListeners(this.context!!, binding.time)
        binding.frequency.onItemSelectedListener = this
        val adapter =
            ArrayAdapter(this.context!!, android.R.layout.simple_spinner_item, frequencySet)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.frequency.adapter = adapter

        binding.viewDetailsButton.setOnClickListener {

            if (binding.description.text.isEmpty() || binding.title.text.isEmpty()) {
                Snackbar.make(this.view!!, "Kindly fill in all fields", Snackbar.LENGTH_LONG).show()
            } else {
//                viewModel.insert(
//                    Routine(
//                        args.routine.id,
//                        binding.description.text.toString(),
//                        binding.title.text.toString(),
//                        binding.title.text.toString(),
//                        frequency,
//                        args.routine.canUpdate,
//                        args.routine.done,
//                        args.routine.createdAt,
//                        date,
//                        args.routine.total,
//                        args.routine.expired,
//                        args.routine.withinMinute,
//                        args.routine.tagProgress
//                    ),
//                    this.mainActivity.applicationContext
//                )
            }
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        frequency = frequencySet[p2]
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {}
}