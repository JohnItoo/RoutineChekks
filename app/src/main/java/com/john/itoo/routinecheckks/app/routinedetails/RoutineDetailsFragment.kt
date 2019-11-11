package com.john.itoo.routinecheckks.app.routinedetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.john.itoo.routinecheckks.App
import com.john.itoo.routinecheckks.R
import com.john.itoo.routinecheckks.app.ExampleRepository
import com.john.itoo.routinecheckks.base.BaseFragment
import com.john.itoo.routinecheckks.databinding.FragmentRoutineDetailBinding
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class RoutineDetailsFragment : BaseFragment(), CoroutineScope {

    lateinit var binding: FragmentRoutineDetailBinding
    @Inject
    lateinit var repo: ExampleRepository
    private lateinit var mJob: Job
    override val coroutineContext: CoroutineContext
        get() = mJob + Dispatchers.Main

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRoutineDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = RoutineDetailsFragmentArgs.fromBundle(arguments!!)

        (mainActivity.applicationContext as App).component.inject(this)

        mainActivity.setUpToolBar("Routine Details")
        binding.routine = args.routine
        binding.editDetailsButton.setOnClickListener {

            this.findNavController().navigate(
                RoutineDetailsFragmentDirections.actionRoutineDetailsFragmentToEditRoutineFragment(
                    args.routine
                )
            )
        }

        if (args.routine.canUpdate == 1) {
            binding.markDone.visibility = View.VISIBLE
        } else {
            binding.markDone.visibility = View.INVISIBLE
        }

        val diff = args.routine.date.time - Date().time
        val hours = (diff / (1000 * 60 * 60 * 24))
        val days = hours / 24
        val minutes = hours % 24

        binding.timeLeft.text = findDiff(Date(), args.routine.date)
        if (args.routine.total > 0 && args.routine.done > 0) {
            if (args.routine.done / args.routine.total >= 0.7) {
                binding.statusImage.setImageResource(com.john.itoo.routinecheckks.R.drawable.thumb)
                binding.statusText.text =
                    "‘Good job! You have over 70% check rate for this routine’"

            } else {
                binding.statusImage.setImageResource(R.drawable.fail)
                binding.statusText.text = "OOps you do not have up to 70 percent completion rate"
            }
        } else {
            binding.statusText.text = "Oops you do not have enough notifications to build a score."

        }

        mJob = Job()
        binding.markDone.setOnCheckedChangeListener { buttonView, isChecked ->
            launch(Dispatchers.Default) {
                async { repo.updateRoutineToMarkAsDone(routine = args.routine) }
            }
            binding.markDone.visibility = View.INVISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mJob.cancel()
    }

    fun findDiff(startDate: Date, endDate: Date): String {

        //milliseconds
        var different = endDate.time - startDate.time

        println("startDate : $startDate")
        println("endDate : $endDate")
        println("different : $different")

        val secondsInMilli: Long = 1000
        val minutesInMilli = secondsInMilli * 60
        val hoursInMilli = minutesInMilli * 60
        val daysInMilli = hoursInMilli * 24

        val elapsedHours = different / hoursInMilli
        different %= hoursInMilli

        val elapsedMinutes = different / minutesInMilli
        different %= minutesInMilli

        val elapsedSeconds = different / secondsInMilli

        return "$elapsedHours hours $elapsedMinutes minutes $elapsedSeconds seconds left"
    }
}