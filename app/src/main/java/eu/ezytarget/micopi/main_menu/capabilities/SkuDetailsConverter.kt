package eu.ezytarget.micopi.main_menu.capabilities

import com.android.billingclient.api.SkuDetails

class SkuDetailsConverter {

    fun convertToInAppProduct(skuDetails: SkuDetails): InAppProduct {
        return InAppProduct(
            skuDetails.sku,
            skuDetails.title,
            skuDetails.description,
            skuDetails.price
        )
    }
}