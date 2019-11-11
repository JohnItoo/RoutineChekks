package com.john.itoo.routinecheckks.app.routinelist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.john.itoo.routinecheckks.R
import com.john.itoo.routinecheckks.app.models.Routine
import com.john.itoo.routinecheckks.databinding.ItemRoutineListBinding

class RoutineListAdapter(
    val routineItemClickListener: (Routine) -> Unit
) : ListAdapter<Routine, RoutineListAdapter.RoutinesViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutinesViewHolder {
        return RoutinesViewHolder(
            ItemRoutineListBinding.bind(
                LayoutInflater.from(parent.context).inflate(R.layout.item_routine_list, parent, false)
            )
        )
    }

    override fun onBindViewHolder(holder: RoutinesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Routine>() {

        override fun areItemsTheSame(oldItem: Routine, newItem: Routine): Boolean = oldItem === newItem

        override fun areContentsTheSame(oldItem: Routine, newItem: Routine): Boolean = oldItem.id == newItem.id
    }

    inner class RoutinesViewHolder(private var binding: ItemRoutineListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                routineItemClickListener(getItem(adapterPosition))
            }
        }

        fun bind(routine: Routine) {
            binding.routine = routine
            binding.executePendingBindings()
        }
    }
}
