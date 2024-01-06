//package com.example.pet.views.adapters
//
//import android.view.LayoutInflater
//import android.view.View
//import android.widget.Button
//import android.widget.TextView
//import com.example.pet.R
//import com.google.android.gms.maps.GoogleMap
//import com.google.android.gms.maps.model.Marker
//
//class CustomInfoWindowAdapter(private val layoutInflater: LayoutInflater) : GoogleMap.InfoWindowAdapter {
//
//    override fun getInfoWindow(marker: Marker): View? {
//        return null // Use default window frame
//    }
//
//    override fun getInfoContents(marker: Marker): View {
//        val view = layoutInflater.inflate(R.layout.info_window, null)
//        val tvTitle = view.findViewById<TextView>(R.id.tvInfoWindowTitle) // Corrected ID
//        tvTitle.text = marker.title
//        // If you want to handle the button click here, you should find the button and set a click listener.
//        val btnInfoWindow = view.findViewById<Button>(R.id.btnInfoWindow) // Find the button by ID
//        btnInfoWindow.setOnClickListener {
//            // Handle button click event
//            // For example, you could call a method to open a new activity or perform another action
//        }
//        return view
//    }
//}
