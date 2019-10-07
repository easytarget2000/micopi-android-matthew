package eu.ezytarget.micopi.batch.service

import eu.ezytarget.micopi.common.data.ContactHashWrapper

interface BatchViewModelServiceListener {
    fun onBatchServiceStartRequested(contactHashWrappers: Array<ContactHashWrapper>)
    fun onBatchServiceStopRequested()
}