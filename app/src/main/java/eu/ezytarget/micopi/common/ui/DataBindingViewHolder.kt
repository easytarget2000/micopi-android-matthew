package eu.ezytarget.micopi.common.ui

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import eu.ezytarget.micopi.BR

class DataBindingViewHolder<T>(private val binding: ViewDataBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: T) {
        binding.setVariable(BR.item, item)
        binding.executePendingBindings()
    }
}