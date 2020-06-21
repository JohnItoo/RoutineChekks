package com.john.itoo.routinecheckks.app.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Routine(
    var id: Int,
    val description: String,
    val title: String,
    val frequency: Int,
    val createdAt: Date,
    var date: Date,
    var nextTime: Date,
    var total: Int
) : Parcelable