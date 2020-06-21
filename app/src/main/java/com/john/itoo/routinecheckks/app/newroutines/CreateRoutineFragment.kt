package com.john.itoo.routinecheckks.app.newroutines

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.john.itoo.routinecheckks.App

import com.john.itoo.routinecheckks.base.BaseFragment
import com.john.itoo.routinecheckks.databinding.CreateRoutineFragmentBinding
import com.john.itoo.routinecheckks.app.models.Routine
import com.john.itoo.routinecheckks.extensions.readableString
import com.john.itoo.routinecheckks.utils.TimeHandler
import com.john.itoo.routinecheckks.utils.TimeUtils
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap

class CreateRoutineFragment : BaseFragment(), TimeHandler {

    private var date = Date()
    private lateinit var viewFrequencyMap: HashMap<Int, View>
    private var currentlySelectedFrequency = 1
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var binding: CreateRoutineFragmentBinding

    @Inject
    lateinit var timeUtils: TimeUtils


    companion object {
        fun newInstance() = CreateRoutineFragment()
    }

    private lateinit var viewModel: CreateRoutineViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CreateRoutineFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        (mainActivity.applicationContext as App).component.inject(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity.setUpToolBar("Create New Routine")
        (mainActivity.applicationContext as App).component.inject(this)
        binding.time.text = date.readableString()
        val args = CreateRoutineFragmentArgs.fromBundle(arguments!!)
        val routine = args.routine

        if (routine.id != -1) {
            binding.routine = args.routine
            binding.time.text = routine.date.readableString()
            currentlySelectedFrequency = routine.frequency
            date = routine.date
        }

        val viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(CreateRoutineViewModel::class.java)
//        binding.viewModel = viewModel

        timeUtils.setDateTimeListeners(this.context!!, binding.timeLayout, binding.time, this, date)
        viewFrequencyMap = hashMapOf(
            1 to binding.hourly,
            2 to binding.daily,
            3 to binding.weekly,
            4 to binding.monthly,
            5 to binding.yearly
        )

        showUserFrequencySelection()
        setFrequencyClickListeners()

        binding.viewDetailsButton.setOnClickListener {
            if (binding.description.text?.isEmpty()!! || binding.title.text?.isEmpty()!!) {
                if (binding.description.text?.isEmpty()!!) {
                    binding.description.setError("Please add a description")
                }
                if (binding.title.text?.isEmpty()!!) {
                    binding.title.setError("Please add a title")
                }
            } else {
                Timber.d(date.toString())
                val updateTime = Date()
                updateTime.time = date.time
                viewModel.insert(
                    Routine(
                        0,
                        binding.description.text.toString(),
                        binding.title.text.toString(),
                        currentlySelectedFrequency,
                        Date(),
                        date,
                        timeUtils.getTimeToUpdate(updateTime, currentlySelectedFrequency),
                        0
                    ),
                    this.mainActivity.applicationContext,
                    routine.id != -1,
                    routine
                )
                Snackbar.make(this.view!!, "This is it", Snackbar.LENGTH_SHORT)
                this.fragmentManager!!.popBackStack()
            }
        }
    }

    private fun setFrequencyClickListeners() {
        for (entry in viewFrequencyMap) {
            entry.value.setOnClickListener {
                handle(entry.key)
            }
        }
    }

    private fun handle(viewIndex: Int) {
        if (currentlySelectedFrequency != viewIndex) {
            currentlySelectedFrequency = viewIndex
            showUserFrequencySelection()
        }
    }

    private fun showUserFrequencySelection() {
        for (frequencyEntry in viewFrequencyMap) {
            var isOpaque = false
            if (frequencyEntry.key == currentlySelectedFrequency) {
                isOpaque = true
            }
            toggleViewAlpha(currentView = frequencyEntry.value, opaque = isOpaque)
        }
    }

    private fun toggleViewAlpha(currentView: View, opaque: Boolean) {
        if (opaque) {
            currentView.alpha = 1f
        } else {
            currentView.alpha = 0.4f
        }
    }

    override fun setTime(date: Date) {
        this.date = date
    }
}