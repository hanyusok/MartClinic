package com.example.martclinic.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Person(
    @SerialName("PCODE")
    val pcode: Int? = null,
    @SerialName("FCODE")
    val fcode: Int? = null,
    @SerialName("PNAME")
    val pname: String? = null,
    @SerialName("PIDNUM")
    val pidnum: String? = null,
    @SerialName("PIDNUM2")
    val pidnum2: String? = null,
    @SerialName("OLDIDNUM")
    val oldidnum: String? = null,
    @SerialName("SEX")
    val sex: String? = null,
    @SerialName("RELATION")
    val relation: String? = null,
    @SerialName("SERIAL")
    val serial: Int? = null,
    @SerialName("RELATION2")
    val relation2: String? = null,
    @SerialName("CRIPPLED")
    val crippled: String? = null,
    @SerialName("VINFORM")
    val vinform: String? = null,
    @SerialName("PBIRTH")
    val pbirth: String? = null,
    @SerialName("MEMO1")
    val memo1: String? = null,
    @SerialName("AGREE")
    val agree: String? = null,
    @SerialName("PERINFO")
    val perinfo: String? = null,
    @SerialName("JAEHAN")
    val jaehan: String? = null,
    @SerialName("SEARCHID")
    val searchId: String? = null,
    @SerialName("PCCHECK")
    val pccheck: String? = null,
    @SerialName("PSNIDT")
    val psnidt: String? = null,
    @SerialName("PSNID")
    val psnid: String? = null,
    @SerialName("LASTCHECK")
    val lastcheck: String? = null,
    @SerialName("CARDCHECK")
    val cardcheck: String? = null
)