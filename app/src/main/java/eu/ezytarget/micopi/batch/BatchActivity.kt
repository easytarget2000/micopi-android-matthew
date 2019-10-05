package eu.ezytarget.micopi.batch

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import eu.ezytarget.micopi.R
import eu.ezytarget.micopi.common.data.ContactHashWrapper
import eu.ezytarget.micopi.common.ui.Activity
import eu.ezytarget.micopi.databinding.BatchActivityBinding


class BatchActivity : Activity() {

    var contactsAdapter: BatchContactsAdapter = BatchContactsAdapter()
    var broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent == null) {
                return
            }

            when (intent.action) {
                BatchService.CONTACT_SUCCESS_ACTION -> handleSuccessBroadcast(intent)
                BatchService.CONTACT_ERROR_ACTION -> handleErrorBroadcast(intent)
            }
        }
    }

    private val viewModel: BatchViewModel by lazy {
        getViewModel(BatchViewModel::class)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModel()
        setupDataBinding()
        setupActionBar()
        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        registerBroadcastReceiver()
    }

    override fun onPause() {
        super.onPause()
        unregisterBroadcastReceiver()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.batch_contacts_recycler)
        recyclerView.adapter = contactsAdapter
    }

    private fun setupViewModel() {
        viewModel.resources = resources

        val contactHashWrappers =
            intent.extras!![CONTACT_HASH_WRAPPERS_INTENT_EXTRA_NAME]
                    as Array<ContactHashWrapper>
        viewModel.setup(
            contactHashWrappers,
            viewModelsOwner = this,
            viewModelsObserver = Observer {
                it?.let(contactsAdapter::submitList)
            }
        )
    }

    private fun setupDataBinding() {
        val binding: BatchActivityBinding = DataBindingUtil.setContentView(
            this,
            R.layout.batch_activity
        )

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    private fun setupActionBar() {
        val toolbar: Toolbar = findViewById(R.id.batch_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun startService(contactHashWrappers: Array<ContactHashWrapper>) {
        val batchService = Intent(this, BatchService::class.java)
        batchService.putExtra(BatchService.CONTACT_WRAPPERS_EXTRA_KEY, contactHashWrappers)
        startService(batchService)
    }

    private fun handleSuccessBroadcast(intent: Intent) {

        val finishedContactWrapper = intent.getSerializableExtra(
            BatchService.CURRENT_CONTACT_WRAPPER_EXTRA_KEY
        )
//        successBroadcast.putExtra(BatchService.CURRENT_CONTACT_WRAPPER_EXTRA_KEY, finishedContact)
//        successBroadcast.putExtra(BatchService.FINISHED_CONTACT_WRAPPERS_EXTRA_KEY, finishedContacts)
//        successBroadcast.putExtra(BatchService.CONTACT_WRAPPERS_EXTRA_KEY, contacts)
//        sendBroadcast(successBroadcast)
    }

    private fun handleErrorBroadcast(intent: Intent) {

    }

    private fun registerBroadcastReceiver() {
        val filter = IntentFilter(
            BatchService.CONTACT_SUCCESS_ACTION,
            BatchService.CONTACT_ERROR_ACTION
        )
        registerReceiver(broadcastReceiver, filter)
    }

    private fun unregisterBroadcastReceiver() {
        unregisterReceiver(broadcastReceiver)
    }

    companion object {
        const val ACTION_IDENTIFIER = "eu.ezytarget.micopi.BATCH"
        const val CONTACT_HASH_WRAPPERS_INTENT_EXTRA_NAME = "CONTACT_HASH_WRAPPERS"
    }

}