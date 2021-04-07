package com.udacity.project4.locationreminders

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.udacity.project4.R
import com.udacity.project4.databinding.ActivityReminderDescriptionBinding
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem
import com.udacity.project4.locationreminders.savereminder.SaveReminderFragment

/**
 * Activity that displays the reminder details after the user clicks on the notification
 */
class ReminderDescriptionActivity : AppCompatActivity(), OnMapReadyCallback {

	private val GEOFENCE_STROKE_WIDTH = 4f

	companion object {
		private const val EXTRA_ReminderDataItem = "EXTRA_ReminderDataItem"
		const val TAG = "ReminderDescription"
		fun newIntent(context: Context, reminderDataItem: ReminderDataItem): Intent {
			val intent = Intent(context, ReminderDescriptionActivity::class.java)
			intent.putExtra(EXTRA_ReminderDataItem, reminderDataItem)
			return intent
		}
	}

	private lateinit var binding: ActivityReminderDescriptionBinding
	private lateinit var map: GoogleMap
	private lateinit var reminder: ReminderDataItem

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = DataBindingUtil.setContentView(this, R.layout.activity_reminder_description)

		reminder = intent.getSerializableExtra(EXTRA_ReminderDataItem) as ReminderDataItem
		binding.reminderDataItem = reminder

		val mapFragment = supportFragmentManager.findFragmentById(R.id.selected_location_map) as SupportMapFragment
		mapFragment.getMapAsync(this)
	}

	override fun onMapReady(googleMap: GoogleMap) {
		map = googleMap
		map.mapType = GoogleMap.MAP_TYPE_NORMAL

		val latLng = LatLng(reminder.latitude!!, reminder.longitude!!)
		map.addCircle(CircleOptions()
        .center(latLng)
        .radius(SaveReminderFragment.GEOFENCE_RADIUS_IN_METERS.toDouble())
        .fillColor(R.color.colorAccent)
        .strokeColor(R.color.colorAccent)
        .strokeWidth(GEOFENCE_STROKE_WIDTH)
    )
		val locationMarker = map.addMarker(MarkerOptions()
        .position(latLng)
        .title(reminder.location)
        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
    )
		locationMarker.showInfoWindow()
		setMapStyle(map)
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
	}

	private fun setMapStyle(map: GoogleMap) {
		try {
			val success = map.setMapStyle(
          MapStyleOptions.loadRawResourceStyle(
              this,
              R.raw.map_style
          )
      )
			if (!success) {
				Log.i(TAG, "Style parsing failed")
			}
		} catch (e: Resources.NotFoundException) {
			Log.i(TAG, "Can't find style. Error: ", e)
		}
	}
}
