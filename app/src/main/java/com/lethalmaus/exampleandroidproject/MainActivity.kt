package com.lethalmaus.exampleandroidproject

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.get
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.lethalmaus.exampleandroidproject.databinding.ActivityMainBinding
import com.lethalmaus.exampleandroidproject.utils.UpdateManager


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var remoteConfig: FirebaseRemoteConfig
    private val immediateUpdateResultLauncher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result: ActivityResult ->
        handleImmediateUpdateResult(result.resultCode)
    }
    private val flexibleUpdateResultLauncher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result: ActivityResult ->
        handleFlexibleUpdateResult(result.resultCode)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getRemoteConfig()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return if (id == R.id.action_licenses) {
            val intent = Intent(this, OssLicensesMenuActivity::class.java)
            startActivity(intent)
            true
        } else super.onOptionsItemSelected(item)
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
            appUpdateManager.startUpdateFlowForResult(appUpdateInfo, immediateUpdateResultLauncher, AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build())
        } else if (isFlexibleUpdateAllowed(appUpdateInfo)) {
            appUpdateManager.startUpdateFlowForResult(appUpdateInfo, flexibleUpdateResultLauncher, AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE).build())
        }
    }

    private fun isImmediateUpdateAllowed(appUpdateInfo: AppUpdateInfo): Boolean {
        return appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
                && (appUpdateInfo.updatePriority() in 4..5 || (remoteConfig[LATEST_VERSION].asDouble() >
                BuildConfig.VERSION_CODE && remoteConfig[FORCE_UPDATE].asBoolean()))
    }

    private fun isFlexibleUpdateAllowed(appUpdateInfo: AppUpdateInfo): Boolean {
        return appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
                && UpdateManager.getUpdateLastCancelled().plus(604800000) < System.currentTimeMillis()
                && (appUpdateInfo.updatePriority() in 2..3 || remoteConfig[LATEST_VERSION].asDouble() > BuildConfig.VERSION_CODE)
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