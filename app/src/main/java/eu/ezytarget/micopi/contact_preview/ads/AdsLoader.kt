package eu.ezytarget.micopi.contact_preview.ads

import android.content.Context
import com.google.android.gms.ads.*
import com.google.android.gms.ads.RequestConfiguration.TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE
import eu.ezytarget.micopi.main_menu.capabilities.CapabilitiesManager


class AdsLoader(private val capabilitiesManager: CapabilitiesManager = CapabilitiesManager()) {

    private lateinit var interstitialAd: InterstitialAd

    fun setup(context: Context, adUnitID: String) {
        val adMobAppID = context.getString(eu.ezytarget.micopi.R.string.adMobAppId)
        setup(context, adMobAppID, adUnitID)
    }

    fun setup(context: Context, adMobAppID: String, adUnitID: String) {
        capabilitiesManager.getCapabilities(context)

        val adsRequestConfig = RequestConfiguration.Builder()
            .setTagForChildDirectedTreatment(TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE)
            .build()

        MobileAds.setRequestConfiguration(adsRequestConfig)
        MobileAds.initialize(context, adMobAppID)

        interstitialAd = InterstitialAd(context)
        interstitialAd.adUnitId = adUnitID
        preloadAd()

        interstitialAd.adListener = object : AdListener() {
            override fun onAdClosed() {
                preloadAd()
            }
        }
    }

    fun preloadAd() {
        interstitialAd.loadAd(AdRequest.Builder().build())
    }

    fun showAdIfAvailable() {
        if (capabilitiesManager.hasPlusProduct) {
            return
        }

        if (interstitialAd.isLoaded) {
            interstitialAd.show()
        }
    }

    companion object {
        const val TEST_AD_MOB_ADD_ID = "ca-app-pub-3940256099942544~3347511713"
        const val TEST_AD_MOB_INTERSTITIAL_AD_ID = "ca-app-pub-3940256099942544/1033173712"
    }
}