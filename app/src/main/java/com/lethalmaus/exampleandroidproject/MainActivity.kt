package com.lethalmaus.exampleandroidproject

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.get
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.lethalmaus.exampleandroidproject.databinding.ActivityMainBinding
import com.lethalmaus.exampleandroidproject.imdb.favourite.FavouritesFragment
import com.lethalmaus.exampleandroidproject.utils.UpdateManager

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var remoteConfig: FirebaseRemoteConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getRemoteConfig()
    }

    private fun getRemoteConfig() {
        remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(this) {
                checkForUpdate()
            }
    }

    private fun checkForUpdate() {
        val appUpdateManager = AppUpdateManagerFactory.create(applicationContext)
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                handleUpdateAvailable(appUpdateManager, appUpdateInfo)
            }
        }
    }

    private fun handleUpdateAvailable(appUpdateManager: AppUpdateManager, appUpdateInfo: AppUpdateInfo) {
        if (isImmediateUpdateAllowed(appUpdateInfo)) {
            appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, this, 1201)
        } else if (isFlexibleUpdateAllowed(appUpdateInfo)) {
            appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.FLEXIBLE, this, 1202)
        }
    }

    private fun isImmediateUpdateAllowed(appUpdateInfo: AppUpdateInfo): Boolean {
        return appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
                && (appUpdateInfo.updatePriority() in 4..5 || (remoteConfig[LATEST_VERSION].asDouble() > BuildConfig.VERSION_CODE && remoteConfig[FORCE_UPDATE].asBoolean()))
    }

    private fun isFlexibleUpdateAllowed(appUpdateInfo: AppUpdateInfo): Boolean {
        return appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
                && UpdateManager.getUpdateLastCancelled().plus(604800000) < System.currentTimeMillis()
                && (appUpdateInfo.updatePriority() in 2..3 || remoteConfig[LATEST_VERSION].asDouble() > BuildConfig.VERSION_CODE)
    }

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1201 -> handleImmediateUpdateResult(resultCode)
            1202 -> handleFlexibleUpdateResult(resultCode)
        }
    }

    private fun handleImmediateUpdateResult(resultCode: Int) {
        if (resultCode != Activity.RESULT_OK) {
            checkForUpdate()
        }
    }

    private fun handleFlexibleUpdateResult(resultCode: Int) {
        if (resultCode == Activity.RESULT_CANCELED) {
            UpdateManager.setUpdateCancelled(System.currentTimeMillis())
        }
    }

    override fun onResume() {
        super.onResume()
        val appUpdateManager = AppUpdateManagerFactory.create(applicationContext)
        appUpdateManager
            .appUpdateInfo
            .addOnSuccessListener { appUpdateInfo ->
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    checkForUpdate()
                } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                    appUpdateManager.completeUpdate()
                }
            }
    }

    companion object {
        private const val LATEST_VERSION = "latest_version"
        private const val FORCE_UPDATE = "force_update"
    }
}