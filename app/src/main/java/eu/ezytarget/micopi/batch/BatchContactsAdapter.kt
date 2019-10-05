package eu.ezytarget.micopi.batch

import androidx.recyclerview.widget.DiffUtil
import eu.ezytarget.micopi.R
import eu.ezytarget.micopi.common.ui.DataBindingAdapter

class BatchContactsAdapter: DataBindingAdapter<BatchContactViewModel>(DiffCallback()) {

    class DiffCallback : DiffUtil.ItemCallback<BatchContactViewModel>() {
        override fun areItemsTheSame(
            oldItem: BatchContactViewModel,
            newItem: BatchContactViewModel
        ): Boolean {
            return oldItem.contact == newItem.contact
        }

        override fun areContentsTheSame(
            oldItem: BatchContactViewModel,
            newItem: BatchContactViewModel
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun getItemViewType(position: Int) = R.layout.batch_contact_item

}