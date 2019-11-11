package com.john.itoo.routinecheckks.app

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.john.itoo.routinecheckks.app.routinelist.RoutineListAdapter
import com.john.itoo.routinecheckks.app.models.Routine
import timber.log.Timber

@BindingAdapter("routinesList")
fun bindRoutinesRecyclerView(recyclerView: RecyclerView, data: List<Routine>?) {
    data?.let {
        Timber.d("Update recycler view")
        val adapter = recyclerView.adapter as RoutineListAdapter
        adapter.submitList(data)
    }
}