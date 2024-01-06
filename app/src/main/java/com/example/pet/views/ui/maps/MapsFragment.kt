package com.example.pet.views.ui.maps

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.pet.R
import com.example.pet.databinding.FragmentMapsBinding
import com.example.pet.model.ChatGroup
import com.example.pet.viewmodel.MyViewModel
import com.example.pet.views.ChatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment(), OnMapReadyCallback, GoogleMap.InfoWindowAdapter {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val permissionCode = 101

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    private lateinit var myViewModel: MyViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        myViewModel = ViewModelProvider(this)[MyViewModel::class.java]

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            onMapReady(googleMap)
            // The following code is moved inside onMapReady to ensure mMap has been initialized
            binding.buttonZoomIn.setOnClickListener {
                googleMap.animateCamera(CameraUpdateFactory.zoomIn())
            }

            binding.buttonZoomOut.setOnClickListener {
                googleMap.animateCamera(CameraUpdateFactory.zoomOut())
            }

            googleMap.setOnInfoWindowClickListener { marker ->
                marker.title?.let { navigateToGroupChat(it) }
            }
        }

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), permissionCode)
        }
    }

    private fun addGroupMarkers(googleMap: GoogleMap, groups: List<ChatGroup>) {
        groups.forEach { group ->
            val location = LatLng(group.latitude, group.longitude)
            googleMap.addMarker(MarkerOptions().position(location).title(group.name))
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setInfoWindowAdapter(this)

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                permissionCode
            )
            return
        }
        mMap.isMyLocationEnabled = true

        // Get current location
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                val currentLatLng = LatLng(it.latitude, it.longitude)
                // Add a title for the current location marker
                mMap.addMarker(MarkerOptions().position(currentLatLng).title("Your Location"))
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
            }
        }

        myViewModel.getGroupList().observe(viewLifecycleOwner) { groups ->
            addGroupMarkers(mMap, groups)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mMap.isMyLocationEnabled = true
                }
            }
        }
    }

    override fun onDestroyView() {
        if (::mMap.isInitialized) {
            // Clean up mMap if necessary
        }
        super.onDestroyView()
        _binding = null
    }

    override fun getInfoWindow(marker: Marker): View? {
        // Check if the marker is for the current location
        if (marker.title == "Your Location") {
            // Inflate the layout specifically for the current location
            val infoWindow = layoutInflater.inflate(R.layout.info_window_current_location, null)
            renderInfoWindowContents(marker, infoWindow)
            return infoWindow
        } else {
            // For other markers, inflate the general info window layout
            val infoWindow = layoutInflater.inflate(R.layout.info_window, null)
            renderInfoWindowContents(marker, infoWindow)
            return infoWindow
        }
    }
    private fun renderInfoWindowContents(marker: Marker, view: View) {
        val title = view.findViewById<TextView>(R.id.tvInfoWindowTitle)
        title.text = if (marker.title == "Your Location") {
            "You are here"
        } else {
            marker.title
        }

        // Check if the button exists in the layout before setting an OnClickListener
        view.findViewById<Button>(R.id.btnInfoWindow)?.setOnClickListener {
            if (::mMap.isInitialized) {
                marker.title?.let { it1 -> navigateToGroupChat(it1) }
            }
        }
    }

    private fun navigateToGroupChat(groupName: String) {
        val intent = Intent(context, ChatActivity::class.java).apply {
            putExtra("GROUP_NAME", groupName)
        }
        startActivity(intent)
    }

    override fun getInfoContents(p0: Marker): View? {
        return null
    }
}
