package eu.ezytarget.micopi.common.ui

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import eu.ezytarget.micopi.BR

class DataBindingViewHolder<T>(private val binding: ViewDataBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(viewModel: T) {
        binding.setVariable(BR.viewModel, viewModel)
        binding.executePendingBindings()
    }
}