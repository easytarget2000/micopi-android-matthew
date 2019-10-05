package eu.ezytarget.micopi.batch

import eu.ezytarget.micopi.common.data.ContactHashWrapper

interface BatchViewModelServiceListener {
    fun onBatchServiceStartRequested(contactHashWrappers: Array<ContactHashWrapper>)
    fun onBatchServiceStopRequested()
}