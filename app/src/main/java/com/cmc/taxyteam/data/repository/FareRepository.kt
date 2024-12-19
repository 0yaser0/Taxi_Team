package com.cmc.taxyteam.data.repository

class FareRepository {
    private var baseFare: Double = 2.5
    private var perKmRate: Double = 1.5
    private var perMinuteRate: Double = 0.5

    fun calculateFare(distance: Double, timeElapsed: Long): Double {
        return baseFare + (distance * perKmRate) + (timeElapsed * perMinuteRate)
    }
}