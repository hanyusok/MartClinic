package com.example.martclinic.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MtsWait(
    @SerialName("PCODE")
    val pcode: Int,
    @SerialName("VISIDATE")
    val visidate: String,
    @SerialName("RESID1")
    val resid1: String,
    @SerialName("RESID2")
    val resid2: String,
    @SerialName("DISPLAYNAME")
    val displayname: String? = null
) 