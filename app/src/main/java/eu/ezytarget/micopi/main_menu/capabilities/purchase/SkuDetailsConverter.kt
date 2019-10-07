package eu.ezytarget.micopi.main_menu.capabilities.purchase

import com.android.billingclient.api.SkuDetails
import eu.ezytarget.micopi.main_menu.capabilities.purchase.InAppProduct

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