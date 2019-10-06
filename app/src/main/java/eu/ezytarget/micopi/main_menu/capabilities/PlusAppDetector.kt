package eu.ezytarget.micopi.main_menu.capabilities

import android.content.pm.PackageManager



class PlusAppDetector {

    fun search(packageManager: PackageManager): Boolean {
        return isPackageInstalled(PLUS_APP_PACKAGE_NAME, packageManager)
    }

    private fun isPackageInstalled(packageName: String, packageManager: PackageManager): Boolean {
        return try {
            packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    companion object {
        private const val PLUS_APP_PACKAGE_NAME = "org.eztarget.micopifull"
    }
}