package eu.ezytarget.micopi.main_menu.capabilities.purchase

import android.os.Bundle
import eu.ezytarget.micopi.common.Tracker

class PurchaseTracker : Tracker() {

    fun handleBillingClientConnectionFailed(errorMessage: String?) {
        val bundle = Bundle()
        bundle.putString(ERROR_MESSAGE_BUNDLE_KEY, errorMessage ?: "")
        handleEventViaFirebase(BILLING_CLIENT_CONNECTION_FAILED_EVENT, bundle)
    }

    fun handlePlusBillingFlowLaunch() {
        handleEventViaFirebase(PLUS_BILLING_FLOW_LAUNCH_EVENT)
    }

    fun handlePlusBillingFlowUserCancel() {
        handleEventViaFirebase(PLUS_BILLING_FLOW_CANCEL_EVENT)
    }

    companion object {
        private const val BILLING_CLIENT_CONNECTION_FAILED_EVENT =
            "billing_client_connection_failed"
        private const val ERROR_MESSAGE_BUNDLE_KEY = "error_message"
        private const val PLUS_BILLING_FLOW_LAUNCH_EVENT = "plus_billing_flow_launch"
        private const val PLUS_BILLING_FLOW_CANCEL_EVENT = "plus_billing_flow_cancel"
    }
}