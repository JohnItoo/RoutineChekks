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
import com.john.itoo.routinecheckks.base.BaseFragment
import com.john.itoo.routinecheckks.databinding.FragmentRoutinesListBinding
import com.john.itoo.routinecheckks.databinding.FragmentRoutinesListUpNextBinding
import com.john.itoo.routinecheckks.networkutils.LoadingStatus
import kotlinx.android.synthetic.main.fragment_routines_list.*
import okhttp3.internal.notify
import javax.inject.Inject

class UpNextRoutineListFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var binding: FragmentRoutinesListUpNextBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRoutinesListUpNextBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity.setUpToolBar("Up Next", true)
        (mainActivity.applicationContext as App).component.inject(this)
        val viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(UpNextRoutineListViewModel::class.java)
        binding.viewModel = viewModel


        binding.routinesRecyclerView.adapter = RoutineListAdapter {
            viewModel.displaySelectedRoutineDetails(it)
        }


        viewModel.navigateToSelectedRoutine.observe(this, Observer {
            if (it != null) {
                this.findNavController().navigate(
                    RoutineListFragmentDirections.actionRoutinesListFragmentToRoutineDetailsFragment(it)
                )
                viewModel.displaySelectedRoutineDetailsComplete()
            }
        })

        viewModel.loadingStatus.observe(this, Observer {
            when (it) {
                LoadingStatus.Success -> mainActivity.dismissLoading()
                is LoadingStatus.Loading -> mainActivity.showLoading(it.message)
                is LoadingStatus.Error -> mainActivity.showError(it.errorMessage)
            }
        })
    }
}