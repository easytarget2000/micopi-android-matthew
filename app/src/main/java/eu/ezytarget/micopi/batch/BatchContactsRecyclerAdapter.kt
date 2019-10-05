package eu.ezytarget.micopi.batch

import androidx.recyclerview.widget.DiffUtil
import eu.ezytarget.micopi.R
import eu.ezytarget.micopi.common.data.ContactHashWrapper
import eu.ezytarget.micopi.common.ui.DataBindingAdapter

class BatchContactsAdapter: DataBindingAdapter<ContactHashWrapper>(DiffCallback()) {

    class DiffCallback : DiffUtil.ItemCallback<ContactHashWrapper>() {
        override fun areItemsTheSame(oldItem: ContactHashWrapper, newItem: ContactHashWrapper): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun areContentsTheSame(oldItem: ContactHashWrapper, newItem: ContactHashWrapper): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
        // your DiffCallback implementation
    }

    override fun getItemViewType(position: Int) = R.layout.batch_contact_item

}