package com.example.simplecalculator.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Calculation(
    val id: String = UUID.randomUUID().toString(),
    val firstArgument: Double,
    val secondArgument: Double,
    val result: Double,
    val action: Action,
    val lastChanged: Date = Date()
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Calculation

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}