package eu.ezytarget.micopi.main_menu.capabilities.purchase

data class InAppProduct(
    val sku: String,
    val title: String,
    val description: String,
    val formattedPrice: String
)