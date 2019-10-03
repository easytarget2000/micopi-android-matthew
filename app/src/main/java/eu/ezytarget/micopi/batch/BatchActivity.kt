package eu.ezytarget.micopi.batch

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import eu.ezytarget.micopi.R
import eu.ezytarget.micopi.common.data.ContactHashWrapper
import eu.ezytarget.micopi.common.ui.Activity
import eu.ezytarget.micopi.databinding.BatchActivityBinding

class BatchActivity : Activity() {

    private val viewModel: BatchViewModel by lazy {
        getViewModel(BatchViewModel::class)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModel()
        setupDataBinding()
    }

    private fun setupViewModel() {
        val contactHashWrappers =
            intent.extras!![CONTACT_HASH_WRAPPERS_INTENT_EXTRA_NAME]
                    as Array<ContactHashWrapper>

        viewModel.resources = resources
//        startService(contactHashWrappers)
    }

    private fun setupDataBinding() {
        val binding: BatchActivityBinding = DataBindingUtil.setContentView(
            this,
            R.layout.batch_activity
        )

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    private fun startService(contactHashWrappers: Array<ContactHashWrapper>) {
        val batchService = Intent(this, BatchService::class.java)
        batchService.putExtra(BatchService.CONTACT_WRAPPERS_EXTRA_KEY, contactHashWrappers)
        startService(batchService)
    }

    companion object {
        const val ACTION_IDENTIFIER = "eu.ezytarget.micopi.BATCH"
        const val CONTACT_HASH_WRAPPERS_INTENT_EXTRA_NAME = "CONTACT_HASH_WRAPPERS"
    }

}