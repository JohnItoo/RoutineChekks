package com.john.itoo.routinecheckks.app.routinelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.john.itoo.routinecheckks.App
import com.john.itoo.routinecheckks.app.models.Routine
import com.john.itoo.routinecheckks.base.BaseFragment
import com.john.itoo.routinecheckks.databinding.FragmentRoutinesListBinding
import com.john.itoo.routinecheckks.networkutils.LoadingStatus
import timber.log.Timber
import javax.inject.Inject

class RoutineListFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var binding: FragmentRoutinesListBinding

    companion object {
        const val DEEP_LINK_EXTRAS = "android-support-nav:controller:deepLinkExtras"

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRoutinesListBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity.setUpToolBar("Routines List", true)
        (mainActivity.applicationContext as App).component.inject(this)
        val viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(RoutineListViewModel::class.java)
        binding.viewModel = viewModel
        val bundle = activity?.intent?.extras

        //Iterating over the bundle of we discover this DEEP_LINK_EXTRAS as key to a bundle that
        // contains (activity.intent.extras[Deep_link_extras])["Rputine"]  = Routine.
        val it = (bundle?.get(DEEP_LINK_EXTRAS)) as? Bundle
        val routine = it?.get("Routine") as? Routine

        if (routine != null) {
            viewModel.displaySelectedRoutineDetails(routine)
        }

        binding.newRoutine.setOnClickListener {
            try {
                requireActivity().runOnUiThread {
                    this.findNavController()
                        .navigate(
                            RoutineListFragmentDirections.actionRoutinesListFragmentToCreateRoutineFragment(
                                viewModel.fetchDefaultRoutine()
                            )
                        )
                }
            } catch (exception: java.lang.IllegalArgumentException) {
                Timber.e("User tried to tap more than twice.")
            }

        }

        binding.routinesRecyclerView.adapter = RoutineListAdapter(context!!) {
            viewModel.displaySelectedRoutineDetails(it)
        }

        showRoutineDetails(viewModel)

        viewModel.loadingStatus.observe(this, Observer {
            when (it) {
                LoadingStatus.Success -> mainActivity.dismissLoading()
                is LoadingStatus.Loading -> mainActivity.showLoading(it.message)
                is LoadingStatus.Error -> mainActivity.showError(it.errorMessage)
            }
        })


    }

    private fun showRoutineDetails(viewModel: RoutineListViewModel) {
        viewModel.navigateToSelectedRoutine.observe(this, Observer {
            if (it != null) {
                requireActivity().runOnUiThread {
                    try {

                        this.findNavController().navigate(
                            RoutineListFragmentDirections.actionRoutinesListFragmentToRoutineDetailsFragment(
                                it
                            )
                        )
                        viewModel.displaySelectedRoutineDetailsComplete()

                    } catch (exception: IllegalArgumentException) {
                        Timber.e("User tried to tap more than twice.")
                    }

                }
            }
        })
    }
}