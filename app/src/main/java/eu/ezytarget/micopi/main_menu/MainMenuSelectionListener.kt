package eu.ezytarget.micopi.main_menu

import eu.ezytarget.micopi.common.data.ContactHashWrapper

interface MainMenuSelectionListener {
    fun onContactPickerSelected(allowMultipleSelection: Boolean = false)
    fun onContactSelected(contactHashWrapper: ContactHashWrapper)
    fun onContactsSelected(contactHashWrappers: Array<ContactHashWrapper>)
    fun sendPromoMailSelected()
}