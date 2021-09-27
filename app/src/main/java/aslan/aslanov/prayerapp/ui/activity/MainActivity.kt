package aslan.aslanov.prayerapp.ui.activity

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.os.PersistableBundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import aslan.aslanov.prayerapp.R
import aslan.aslanov.prayerapp.databinding.ActivityMainBinding
import aslan.aslanov.prayerapp.local.manager.SharedPreferenceManager.isLatitude
import aslan.aslanov.prayerapp.local.manager.SharedPreferenceManager.isLongitude
import aslan.aslanov.prayerapp.local.manager.SharedPreferenceManager.locationCityName
import aslan.aslanov.prayerapp.local.manager.SharedPreferenceManager.locationCountryName
import aslan.aslanov.prayerapp.util.AppConstant
import aslan.aslanov.prayerapp.util.PendingRequests.CATCH_REQUEST_CODE_FROM_MAIN
import aslan.aslanov.prayerapp.util.PendingRequests.REQUEST_CODE_AYAHS
import aslan.aslanov.prayerapp.util.PendingRequests.REQUEST_CODE_PRAYER_TIME
import aslan.aslanov.prayerapp.util.makeToast
import com.google.android.gms.location.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import java.util.*

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var client: FusedLocationProviderClient
    private lateinit var geocoder: Geocoder
    private lateinit var navController: NavController

    private val permissionResult =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { requestPermissions ->
            if (requestPermissions.containsValue(false)) {
                permissionNeeded()
            } else {
                checkPermissionGetLocation()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.lifecycleOwner = this
        client = LocationServices.getFusedLocationProviderClient(this@MainActivity)

        val navView: BottomNavigationView = binding.bottomNavigationView
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_remaining_time,
                R.id.navigation_prayer_time,
                R.id.navigation_prayer_calendar,
                R.id.navigation_prayer_events,
                R.id.navigation_prayer_qibla
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        createNotificationChannel()
    }

    override fun onNewIntent(intent: Intent?) {
        intent?.let {
            it.getStringExtra(CATCH_REQUEST_CODE_FROM_MAIN)?.let { requestCode ->
                if (requestCode.toInt() == REQUEST_CODE_PRAYER_TIME) {
                    makeToast(requestCode)
                } else if (requestCode.toInt() == REQUEST_CODE_AYAHS) {
                    makeToast(requestCode)
                }
            }
        }
        super.onNewIntent(intent)
    }


    override fun onStart() {
        super.onStart()
        checkPermissionGetLocation()
        intent?.let {
            it.getStringExtra(CATCH_REQUEST_CODE_FROM_MAIN)?.let { requestCode ->
                if (requestCode.toInt() == REQUEST_CODE_PRAYER_TIME) {
                    makeToast(requestCode)
                } else if (requestCode.toInt() == REQUEST_CODE_AYAHS) {
                    makeToast(requestCode)
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        navController = navHostFragment.navController
        return navController.navigateUp()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
    }

    private fun checkPermissionGetLocation() {
        if (ActivityCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val locationManager =
                this@MainActivity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && locationManager.isProviderEnabled(
                    LocationManager.NETWORK_PROVIDER
                )
            ) {
                client.lastLocation.addOnCompleteListener { task ->
                    val location = task.result
                    if (location != null) {
                        try {
                            geocoder = Geocoder(this@MainActivity, Locale.getDefault())
                            val geocoderLocation: List<Address> =
                                geocoder.getFromLocation(location.latitude, location.longitude, 1)
                            if (geocoderLocation.isNotEmpty()) {
                                locationCityName = geocoderLocation[0].locality
                                locationCountryName = geocoderLocation[0].countryName
                            }
                            isLatitude = location.latitude.toString()
                            isLongitude = location.longitude.toString()

                        } catch (e: Exception) {
                            e.printStackTrace()
                            Log.d("TimingsFragment", "bindUI: ${e.message}")
                        }
                    } else {
                        val locationRequest =
                            LocationRequest.create().apply {
                                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                                interval = 10000
                                fastestInterval = 1000
                                numUpdates = 1
                            }
                        val locationCallback = object : LocationCallback() {
                            override fun onLocationResult(p0: LocationResult) {
                                val location1 = p0.lastLocation
                                isLatitude = location1.latitude.toString()
                                isLongitude = location1.longitude.toString()
                            }
                        }
                        client.requestLocationUpdates(
                            locationRequest,
                            locationCallback,
                            Looper.getMainLooper()
                        )
                    }
                }
            } else {
                Toast.makeText(this, "network or provider not enable", Toast.LENGTH_SHORT).show()
            }
        } else {
            permissionResult.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun permissionNeeded(): Unit = with(binding) {
        val intent =
            Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        Snackbar.make(
            binding.root,
            getString(R.string.permission),
            Snackbar.LENGTH_INDEFINITE
        ).setAction("Settings") {
            it?.let {
                startActivity(intent)
            }
        }.show()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "PrayerAppRemainderChannel"
            val desc = "Channel for Alarm Manager"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(AppConstant.NOTIFICATION_ID, name, importance)
            channel.description = desc
            val notificationManager =
                this.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }


}