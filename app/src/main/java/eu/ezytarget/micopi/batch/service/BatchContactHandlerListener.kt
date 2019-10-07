package eu.ezytarget.micopi.batch.service

import eu.ezytarget.micopi.common.data.ContactHashWrapper

interface BatchContactHandlerListener {

    fun onBatchContactProcessingStarted(
        currentContactWrapper: ContactHashWrapper,
        contactWrappers: Array<ContactHashWrapper>
    )

    fun onBatchContactSuccess(
        finishedContactWrapper: ContactHashWrapper,
        finishedContactWrappers: Array<ContactHashWrapper>,
        contactWrappers: Array<ContactHashWrapper>
    )

    fun onBatchContactError(
        failedContactWrapper: ContactHashWrapper,
        failedContactWrappers: Array<ContactHashWrapper>,
        contactWrappers: Array<ContactHashWrapper>
    )


    fun onBatchFinish(
        finishedContactWrappers: Array<ContactHashWrapper>,
        failedContactWrappers: Array<ContactHashWrapper>,
        contactWrappers: Array<ContactHashWrapper>
    )
}