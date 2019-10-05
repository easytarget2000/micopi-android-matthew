package eu.ezytarget.micopi.batch

import android.content.Intent
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

    private val viewModel: BatchViewModel by lazy {
        getViewModel(BatchViewModel::class)
    }
    private var contactsAdapter: BatchContactsAdapter = BatchContactsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModel()
        setupDataBinding()
        setupActionBar()
        setupRecyclerView()
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
        viewModel.contactWrappersLiveData.value = contactHashWrappers.toList()

        viewModel.contactWrappersLiveData.observe(this, Observer {
            it?.let(contactsAdapter::submitList)
        })
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

    companion object {
        const val ACTION_IDENTIFIER = "eu.ezytarget.micopi.BATCH"
        const val CONTACT_HASH_WRAPPERS_INTENT_EXTRA_NAME = "CONTACT_HASH_WRAPPERS"
    }

}