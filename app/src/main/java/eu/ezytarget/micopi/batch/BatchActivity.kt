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
import eu.ezytarget.micopi.batch.service.BatchService
import eu.ezytarget.micopi.batch.service.BatchViewModelServiceListener
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
                BatchService.CONTACT_START_ACTION -> handleStartBroadcast(intent)
                BatchService.CONTACT_SUCCESS_ACTION -> handleSuccessBroadcast(intent)
                BatchService.CONTACT_ERROR_ACTION -> handleErrorBroadcast(intent)
                BatchService.FINISH_ALL_ACTION -> handleFinishBroadcast(intent)
            }
        }
    }
    private var batchServiceIntent: Intent? = null

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

    override fun onBackPressed() {
        super.onBackPressed()
        stopService()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService()
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
        viewModel.contactWrappers = contactHashWrappers
        viewModel.setupContactViewModels(
            viewModelsOwner = this,
            viewModelsObserver = Observer {
                it?.let(contactsAdapter::submitList)
            }
        )
        viewModel.serviceListener = object :
            BatchViewModelServiceListener {
            override fun onBatchServiceStartRequested(
                contactHashWrappers: Array<ContactHashWrapper>
            ) {
                startService(contactHashWrappers)
            }

            override fun onBatchServiceStopRequested() {
                stopService()
            }
        }
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
        if (batchServiceIntent != null) {
            stopService()
        }

        val batchServiceIntent = Intent(this, BatchService::class.java)
        batchServiceIntent.putExtra(BatchService.CONTACT_WRAPPERS_EXTRA_KEY, contactHashWrappers)
        startService(batchServiceIntent)

        this.batchServiceIntent = batchServiceIntent
        viewModel.handleServiceStarted()
    }

    private fun stopService() {
        if (batchServiceIntent == null) {
            return
        }
        stopService(batchServiceIntent)
        batchServiceIntent = null
        viewModel.handleServiceStopped()
    }

    private fun handleStartBroadcast(intent: Intent) {
        val currentContactWrapper = intent.getSerializableExtra(
            BatchService.CURRENT_CONTACT_WRAPPER_EXTRA_KEY
        ) as ContactHashWrapper
        viewModel.currentContactWrapper = currentContactWrapper
        viewModel.handleServiceStarted()
    }

    private fun handleSuccessBroadcast(intent: Intent) {
        val finishedContactWrappers = intent.getSerializableExtra(
            BatchService.FINISHED_CONTACT_WRAPPERS_EXTRA_KEY
        ) as Array<ContactHashWrapper>

        viewModel.finishedContactWrappers = finishedContactWrappers
    }

    private fun handleErrorBroadcast(intent: Intent) {
        val failedContactWrappers = intent.getSerializableExtra(
            BatchService.FAILED_CONTACT_WRAPPERS_EXTRA_KEY
        ) as Array<ContactHashWrapper>

        viewModel.failedContactWrappers = failedContactWrappers
    }

    private fun handleFinishBroadcast(intent: Intent) {
        viewModel.handleServiceStopped()
    }

    private fun registerBroadcastReceiver() {
        val filter = IntentFilter()
        filter.addAction(BatchService.CONTACT_START_ACTION)
        filter.addAction(BatchService.CONTACT_SUCCESS_ACTION)
        filter.addAction(BatchService.CONTACT_ERROR_ACTION)
        filter.addAction(BatchService.FINISH_ALL_ACTION)
        registerReceiver(broadcastReceiver, filter)
    }

    private fun unregisterBroadcastReceiver() {
        unregisterReceiver(broadcastReceiver)
    }

    companion object {
        const val ACTION_IDENTIFIER = "eu.ezytarget.micopi.BATCH"
        const val CONTACT_HASH_WRAPPERS_INTENT_EXTRA_NAME = "CONTACT_HASH_WRAPPERS"
        val tag = BatchActivity::class.java.name
    }

}