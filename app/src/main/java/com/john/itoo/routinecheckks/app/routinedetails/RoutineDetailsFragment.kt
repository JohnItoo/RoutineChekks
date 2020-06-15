package com.john.itoo.routinecheckks.app.routinedetails

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.john.itoo.routinecheckks.App
import com.john.itoo.routinecheckks.R
import com.john.itoo.routinecheckks.Utils
import com.john.itoo.routinecheckks.app.ExampleRepository
import com.john.itoo.routinecheckks.app.newroutines.CreateRoutineViewModel
import com.john.itoo.routinecheckks.databinding.FragmentRoutineDetailBinding
import com.john.itoo.routinecheckks.extensions.readableString
import kotlinx.coroutines.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class RoutineDetailsFragment : BottomSheetDialogFragment(), CoroutineScope {

    lateinit var binding: FragmentRoutineDetailBinding
    @Inject
    lateinit var repo: ExampleRepository
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var mJob: Job
    override val coroutineContext: CoroutineContext
        get() = mJob + Dispatchers.Main
    private val frequencySet = mutableListOf("Hourly", "Daily", "Weekly", "Monthly", "Yearly")

    companion object {
        val TAG = "RoutineDetailsFragment"

        fun newInstance() = RoutineDetailsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRoutineDetailBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = RoutineDetailsFragmentArgs.fromBundle(arguments!!)

        (activity?.applicationContext as App).component.inject(this)

//        mainActivity.setUpToolBar("Routine Details")

        val viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(CreateRoutineViewModel::class.java)
        val alertDialog: AlertDialog? = activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setMessage("Do you really want to delete this?")
                setPositiveButton(
                    R.string.ok
                ) { _, _ ->
                    // User clicked OK button
                    CoroutineScope(Dispatchers.IO).launch {
                        viewModel.delete(
                            binding.routine!!,
                            context
                        )
                    }
                    dismiss()
                }
                setNegativeButton("Cancel",
                    DialogInterface.OnClickListener { dialog, id ->
                        // User cancelled the dialog
                        dialog.cancel()
                    })
            }
            // Set other dialog properties

            // Create the AlertDialog
            builder.create()
        }
        binding.routine = args.routine
        binding.deleteRoutine.setOnClickListener {
            alertDialog?.show()
        }
        val abbreviation = Utils.abbreviations[args.routine.frequency - 1]
        binding.editDetailsButton.setBackgroundTintList(context!!.resources.getColorStateList(Utils.frequencyColorMap[abbreviation]!!))
        binding.editDetailsButton.setOnClickListener {

            try {
                requireActivity().runOnUiThread {
                    this.findNavController().navigate(
                        RoutineDetailsFragmentDirections.actionRoutineDetailsFragmentToCreateRoutineFragment(
                            args.routine
                        )
                    )
                }
            } catch (e: IllegalArgumentException) {
                Timber.e("User tried to break Nav stack")
            }
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

        val spannableString =
            SpannableString("This is routine occurs ${frequencySet[args.routine.frequency - 1]} and will happen next ${args.routine.date.readableString()}")
//        spannableString.setSpan(
//            ForegroundColorSpan(getTextColor(args.routine.frequency)),
//            22,
//            spannableString.length,
//            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//        )
        binding.statusText.text = spannableString

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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.setOnShowListener {
            val dialog = it as BottomSheetDialog
            val bottomSheet =
                dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            BottomSheetBehavior.from(bottomSheet!!).state = BottomSheetBehavior.STATE_EXPANDED
        }
        return bottomSheetDialog
    }
}