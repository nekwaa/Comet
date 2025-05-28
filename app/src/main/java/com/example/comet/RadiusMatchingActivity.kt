package com.example.comet

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import android.content.Intent

class RadiusMatchingActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var seekBarRadius: SeekBar
    private lateinit var tvRadius: TextView
    private lateinit var tvCategoryName: TextView
    private lateinit var rvCometeers: RecyclerView
    private lateinit var ivBackButton: ImageView

    private var currentRadius = 5 // Default radius in km
    private var currentLocation: LatLng? = null
    private var categoryName: String = ""

    private val cometeers = mutableListOf<CometeerModel>()
    private lateinit var adapter: CometeerAdapter

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val MAX_RADIUS = 50 // Maximum radius in km
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_radius_matching)

        // Get category name from intent
        categoryName = intent.getStringExtra("CATEGORY_NAME") ?: "All Services"

        // Initialize views
        tvCategoryName = findViewById(R.id.tvCategoryName)
        seekBarRadius = findViewById(R.id.seekBarRadius)
        tvRadius = findViewById(R.id.tvRadius)
        rvCometeers = findViewById(R.id.rvCometeers)
        ivBackButton = findViewById(R.id.ivBackButton)

        // Set category name
        tvCategoryName.text = categoryName

        // Set up back button
        ivBackButton.setOnClickListener {
            finish()
        }

        // Set up map
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Initialize location client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Set up radius seek bar
        setupRadiusSeekBar()

        // Set up recycler view
        setupRecyclerView()

        // Load mock data for testing
        loadMockData()
    }

    private fun setupRadiusSeekBar() {
        seekBarRadius.max = MAX_RADIUS
        seekBarRadius.progress = currentRadius
        tvRadius.text = "$currentRadius km"

        seekBarRadius.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (progress < 1) seekBarRadius.progress = 1
                currentRadius = seekBarRadius.progress
                tvRadius.text = "$currentRadius km"
                updateMapCircle()
                filterCometeers()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun setupRecyclerView() {
        adapter = CometeerAdapter(cometeers) { cometeer ->
            showCometeerDetails(cometeer)
        }
        rvCometeers.layoutManager = LinearLayoutManager(this)
        rvCometeers.adapter = adapter
    }

    private fun loadMockData() {
        // Mock data for testing
        val mockCometeers = listOf(
            CometeerModel(
                id = "1",
                name = "John Smith",
                category = "Plumbing",
                rating = 4.5f,
                numReviews = 120,
                distance = 2.3,
                imageUrl = "",
                description = "Professional plumber with 10 years of experience",
                hourlyRate = "$50/hr"
            ),
            CometeerModel(
                id = "2",
                name = "Sarah Johnson",
                category = "Electrical",
                rating = 4.8f,
                numReviews = 85,
                distance = 3.7,
                imageUrl = "",
                description = "Licensed electrician specializing in residential wiring",
                hourlyRate = "$65/hr"
            ),
            CometeerModel(
                id = "3",
                name = "Mike Davis",
                category = "Plumbing",
                rating = 4.2f,
                numReviews = 56,
                distance = 6.1,
                imageUrl = "",
                description = "Plumbing repairs and installations",
                hourlyRate = "$45/hr"
            ),
            CometeerModel(
                id = "4",
                name = "Emily Wilson",
                category = "Cleaning",
                rating = 4.9f,
                numReviews = 210,
                distance = 1.8,
                imageUrl = "",
                description = "Thorough home cleaning services",
                hourlyRate = "$35/hr"
            ),
            CometeerModel(
                id = "5",
                name = "David Brown",
                category = "Electrical",
                rating = 4.6f,
                numReviews = 92,
                distance = 8.5,
                imageUrl = "",
                description = "Commercial and residential electrical services",
                hourlyRate = "$70/hr"
            )
        )

        cometeers.clear()
        cometeers.addAll(mockCometeers)
        filterCometeers()
    }

    private fun filterCometeers() {
        val filteredList = cometeers.filter {
            (categoryName == "All Services" || it.category == categoryName) &&
                    it.distance <= currentRadius
        }

        adapter.updateData(filteredList)

        // Show empty state if no cometeers found
        if (filteredList.isEmpty()) {
            findViewById<TextView>(R.id.tvEmptyState).visibility = View.VISIBLE
        } else {
            findViewById<TextView>(R.id.tvEmptyState).visibility = View.GONE
        }
    }

    private fun showCometeerDetails(cometeer: CometeerModel) {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_cometeer_details, null)
        dialog.setContentView(view)  // This line is missing in your code

        // Set cometeer details
        view.findViewById<TextView>(R.id.tvName).text = cometeer.name
        view.findViewById<TextView>(R.id.tvCategory).text = cometeer.category
        view.findViewById<TextView>(R.id.tvRating).text = "${cometeer.rating} (${cometeer.numReviews})"
        view.findViewById<TextView>(R.id.tvDistance).text = "${cometeer.distance} km away"
        view.findViewById<TextView>(R.id.tvDescription).text = cometeer.description
        view.findViewById<TextView>(R.id.tvRate).text = cometeer.hourlyRate

        // Set contact button click listener
        view.findViewById<Button>(R.id.btnContact).setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java).apply {
                putExtra("COMETEER_ID", cometeer.id)
                putExtra("COMETEER_NAME", cometeer.name)
                // If you have an image resource ID in your Cometeer model, include it
                if (cometeer.imageUrl.isNotEmpty()) {
                    putExtra("COMETEER_IMAGE", cometeer.imageUrl)
                }
            }
            startActivity(intent)
            dialog.dismiss()
        }

        dialog.setOnShowListener {
            // Optional: You can add animations or additional setup here
        }

        dialog.show()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Check for location permission
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
            getCurrentLocation()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                currentLocation = LatLng(it.latitude, it.longitude)

                // Add marker for current location
                mMap.addMarker(MarkerOptions().position(currentLocation!!).title("Your Location"))

                // Move camera to current location
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation!!, 12f))

                // Draw radius circle
                updateMapCircle()
            }
        }
    }

    private fun updateMapCircle() {
        if (currentLocation == null) return

        // Clear previous circles
        mMap.clear()

        // Add marker for current location
        mMap.addMarker(MarkerOptions().position(currentLocation!!).title("Your Location"))

        // Draw radius circle
        mMap.addCircle(
            CircleOptions()
                .center(currentLocation!!)
                .radius((currentRadius * 1000).toDouble()) // Convert km to meters
                .strokeColor(ContextCompat.getColor(this, R.color.circle_stroke))
                .fillColor(ContextCompat.getColor(this, R.color.circle_fill))
                .strokeWidth(2f)
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    mMap.isMyLocationEnabled = true
                    getCurrentLocation()
                }
            } else {
                Toast.makeText(
                    this,
                    "Location permission denied. Cannot show nearby cometeers.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}