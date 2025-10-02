package edu.swin.rentacar

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Car(
    val id: String,
    val name: String,
    val model: String,
    val year: Int,
    val rating: Float,
    val kilometres: Int,
    val dailyCost: Int,
    val imageRes: Int
) : Parcelable
