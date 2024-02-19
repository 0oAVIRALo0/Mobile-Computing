package com.example.mobilecomputing_assignment_1

import android.animation.ObjectAnimator
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random


class DistanceManager {
    private val distanceMapInKm = mutableMapOf<Int, Float>()
    private val distanceMapInMiles = mutableMapOf<Int, Float>()

    fun initializeDistanceMaps() {
        distanceMapInKm[1] = 0.0f
        for (i in 2..10) {
            val randomDistance = randomNum()
            distanceMapInKm[i] = randomDistance
        }

        distanceMapInMiles[1] = 0.0f
        distanceMapInKm.forEach { (stop, distance) ->
            val distanceInMiles = convertKilometersToMiles(distance)
            distanceMapInMiles[stop] = distanceInMiles.toFloat()
        }
    }


    fun getTotalDistance(): Float {
        var totalDistance = 0.0f
        distanceMapInKm.forEach { (_, distance) ->
            totalDistance += distance
        }
        return totalDistance
    }

    fun getMapLength(): Int {
        return distanceMapInKm.size
    }

    fun getDistance(idx: Int, type: String) : Float {
        if (type == "miles") {
            return distanceMapInMiles[idx]!!
        } else {
            return distanceMapInKm[idx]!!
        }
    }

    private fun randomNum(): Float {
        return Random.nextFloat() * 100
    }

    private fun convertKilometersToMiles(kilometers: Float): Double {
        val milesConversionFactor = 0.621371
        return (kilometers * milesConversionFactor)
    }
}

class DistanceManagerLazy {
    private val lazyDistanceMapKm: Map<Int, Float> by lazy {
        generateLazyDistanceMapKm()
    }

    private val lazyDistanceMapMiles: Map<Int, Float> by lazy {
        generateLazyDistanceMapMiles()
    }

    private fun generateLazyDistanceMapKm(): Map<Int, Float> {
        return generateSequence(1) { it + 1 }
            .take(20)
            .associateWith {
                if (it == 1) {
                    0f
                } else {
                    generateRandomDistance()
                }
            }
    }

    private fun generateLazyDistanceMapMiles(): Map<Int, Float> {
        return generateSequence(1) { it + 1 }
            .take(20)
            .associateWith {
                if (it == 1) {
                    0f
                } else {
                    val distanceInKm = generateRandomDistance()
                    convertKilometersToMiles(distanceInKm).toFloat()
                }
            }
    }

    private fun generateRandomDistance(): Float {
        return Random.nextFloat() * 100
    }

    private fun convertKilometersToMiles(kilometers: Float): Float {
        val milesConversionFactor = 0.621371
        return (kilometers * milesConversionFactor).toFloat()
    }

    fun getDistance(idx: Int, type: String) : Float {
        return if (type == "miles") {
            lazyDistanceMapMiles[idx]!!
        } else {
            lazyDistanceMapKm[idx]!!
        }
    }

    fun getMapLength(): Int {
        return lazyDistanceMapKm.size
    }

    fun getTotalDistance(): Float {
        var totalDistance = 0.0f
        lazyDistanceMapKm.forEach { (_, distance) ->
            totalDistance += distance as Float
        }
        return totalDistance
    }
}

class ProgressManager {
    fun updateProgressBar(progressBar: ProgressBar, totalDistance: Float, currentDistance: Float, cumulativeDistance: Float) {
        progressBar.max = totalDistance.toInt()
        val startProgress = (currentDistance + cumulativeDistance).toInt()
        ObjectAnimator.ofInt(progressBar, "progress", startProgress).setDuration(2000).start()
    }

    fun buildProgressText(currentStopIndex: Int, totalDistance: Float, distanceCovered: Float, units: String): String {
        return "Current Stop: Stop $currentStopIndex\n" +
                "Total distance to cover: $totalDistance $units\n" +
                "Distance Covered till now: $distanceCovered $units\n" +
                "Total distance left: ${totalDistance - distanceCovered} $units"
    }
}

class MainActivity : AppCompatActivity() {

    private lateinit var progressTextView: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var switchUnitsButton: Button
    private lateinit var reachedNextStopButton: Button
    private val lazyTextView: TextView by lazy {
        findViewById<TextView>(R.id.lazyTextView)
    }
    private lateinit var showLazyTextButton: Button
    private lateinit var reachedNextStopButtonLazy: Button
    private lateinit var showNormalTextButton: Button
    private lateinit var showNormalListButton: Button
    private lateinit var showLazyListButton: Button
    private lateinit var showLists: TextView

    private var isKm = true
    private var currentStopIndex = 1
    private var indexForNextDistance = 2
    private var currentStopIndexLazy = 1
    private var indexForNextDistanceLazy = 2

    private val distanceManager = DistanceManager()
    private val progressManager = ProgressManager()
    private val distanceManagerLazy = DistanceManagerLazy()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressTextView = findViewById(R.id.progressTextView)
        progressBar = findViewById(R.id.progressBar)
        switchUnitsButton = findViewById(R.id.switchUnitsButton)
        reachedNextStopButton = findViewById(R.id.reachedNextStopButton)
        showLazyTextButton = findViewById(R.id.showLazyTextButton)
        reachedNextStopButtonLazy = findViewById(R.id.reachedNextStopButtonLazy)
        showNormalTextButton = findViewById(R.id.showNormalTextButton)
        showNormalListButton = findViewById(R.id.showNormalListButton)
        showLazyListButton = findViewById(R.id.showLazyListButton)
        showLists = findViewById(R.id.showLists)

        lazyTextView.visibility = TextView.INVISIBLE
        progressTextView.visibility = TextView.INVISIBLE
        showLists.visibility = TextView.INVISIBLE
        switchUnitsButton.text = "Switch to Miles"

        distanceManager.initializeDistanceMaps()

        var totalDistance = distanceManager.getTotalDistance().toFloat()
        var totalDistancelazy = distanceManagerLazy.getTotalDistance().toFloat()

        var distanceCovered = 0f
        var cumulativeDistance = 0f
        showNormalTextButton.setOnClickListener {
            if (progressTextView.visibility == TextView.INVISIBLE) {
                progressTextView.visibility = TextView.VISIBLE
                showNormalTextButton.text = "Hide Normal Text"
                reachedNextStopButton.setOnClickListener {
                    if (currentStopIndex <= 10) {
                        val currentDistance = distanceManager.getDistance(currentStopIndex, "km")
                        progressManager.updateProgressBar(progressBar, totalDistance, currentDistance, cumulativeDistance)
                        distanceCovered += currentDistance
                        val progressText = progressManager.buildProgressText(currentStopIndex, totalDistance, distanceCovered,"km")
                        progressTextView.text = progressText

                        setupSwitchUnitsClickListener(currentStopIndex, totalDistance, distanceCovered)

                        cumulativeDistance += distanceManager.getDistance(currentStopIndex, "km")
                        currentStopIndex++
                        indexForNextDistance++
                    }
                }
            } else {
                progressTextView.visibility = TextView.INVISIBLE
                showNormalTextButton.text = "Show Normal Text"
            }
        }


        var distanceCoveredLazy = 0f
        var cumulativeDistanceLazy = 0f
        showLazyTextButton.setOnClickListener {
            if (lazyTextView.visibility == TextView.INVISIBLE) {
                lazyTextView.visibility = TextView.VISIBLE
                showLazyTextButton.text = "Hide Lazy Text"
                reachedNextStopButtonLazy.setOnClickListener {
                    if (currentStopIndexLazy <= distanceManagerLazy.getMapLength()) {
                        val currentDistance = distanceManagerLazy.getDistance(currentStopIndexLazy, "km")
                        progressManager.updateProgressBar(progressBar, totalDistancelazy, currentDistance, cumulativeDistanceLazy)
                        distanceCoveredLazy += currentDistance.toFloat()
                        val progressText = progressManager.buildProgressText(currentStopIndexLazy, totalDistancelazy, distanceCoveredLazy, "km")
                        lazyTextView.text = progressText

                        setupSwitchUnitsClickListenerLazy(currentStopIndexLazy, totalDistancelazy, distanceCoveredLazy)

                        cumulativeDistanceLazy += currentDistance.toFloat()
                        currentStopIndexLazy++
                        indexForNextDistanceLazy++
                    }
                }
            } else {
                lazyTextView.visibility = TextView.INVISIBLE
                showLazyTextButton.text = "Show Lazy Text"
            }
        }

        showNormalListButton.setOnClickListener {
            if (showLists.visibility == TextView.INVISIBLE) {
                showLists.visibility = TextView.VISIBLE
                showNormalListButton.text = "Hide Normal List"
                for (i in 1..distanceManager.getMapLength()) {
                    val distanceKm = distanceManager.getDistance(i, "km")
                    val distanceMiles = distanceManager.getDistance(i, "miles")
                    showLists.append("Stop $i: $distanceKm km/$distanceMiles miles\n")
                }
            } else {
                showLists.visibility = TextView.INVISIBLE
                showNormalListButton.text = "Show Normal List"
                showLists.text = ""
            }
        }

        showLazyListButton.setOnClickListener {
            if (showLists.visibility == TextView.INVISIBLE) {
                showLists.visibility = TextView.VISIBLE
                showLazyListButton.text = "Hide Lazy List"
                for (i in 1..distanceManagerLazy.getMapLength()) {
                    val distanceKm = distanceManagerLazy.getDistance(i, "km")
                    val distanceMiles = distanceManagerLazy.getDistance(i, "miles")
                    showLists.append("Stop $i: $distanceKm km/$distanceMiles miles\n")
                }
            } else {
                showLists.visibility = TextView.INVISIBLE
                showLazyListButton.text = "Show Lazy List"
                showLists.text = ""
            }
        }
    }

    private fun convertKilometersToMiles(kilometers: Float): Float {
        val milesConversionFactor = 0.621371
        return (kilometers * milesConversionFactor).toFloat()
    }

    private fun setupSwitchUnitsClickListener(currentStopIndex: Int, initialTotalDistance: Float, initialDistanceCovered: Float) {
        switchUnitsButton.setOnClickListener {
            if (isKm) {
                val totalDistanceInMiles = convertKilometersToMiles(initialTotalDistance)
                val distanceCoveredInMiles = convertKilometersToMiles(initialDistanceCovered)
                val progressText = progressManager.buildProgressText(currentStopIndex, totalDistanceInMiles, distanceCoveredInMiles, "miles")
                progressTextView.text = progressText
                isKm = false
                switchUnitsButton.text = "Switch to Kilometers"
            } else {
                val progressText = progressManager.buildProgressText(currentStopIndex, initialTotalDistance, initialDistanceCovered, "km")
                progressTextView.text = progressText
                isKm = true
                switchUnitsButton.text = "Switch to Miles"
            }
        }
    }

    private fun setupSwitchUnitsClickListenerLazy(currentStopIndex: Int, initialTotalDistance: Float, initialDistanceCovered: Float) {
        switchUnitsButton.setOnClickListener {
            if (isKm) {
                val totalDistanceInMiles = convertKilometersToMiles(initialTotalDistance)
                val distanceCoveredInMiles = convertKilometersToMiles(initialDistanceCovered)
                val progressText = progressManager.buildProgressText(currentStopIndex, totalDistanceInMiles, distanceCoveredInMiles, "miles")
                lazyTextView.text = progressText
                isKm = false
                switchUnitsButton.text = "Switch to Kilometers"
            } else {
                val progressText = progressManager.buildProgressText(currentStopIndex, initialTotalDistance, initialDistanceCovered, "km")
                lazyTextView.text = progressText
                isKm = true
                switchUnitsButton.text = "Switch to Miles"
            }
        }
    }
}