package com.example.martclinic.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Mtr(
    @SerialName("PCODE")
    val pcode: Int,
    @SerialName("VISIDATE")
    val visidate: String,
    @SerialName("VISITIME")
    val visitime: String,
    @SerialName("PNAME")
    val pname: String,
    @SerialName("SERIAL")
    val serial: Int? = null,  // Make serial nullable with default value null
    @SerialName("SEX")
    val sex: String = "1",  // Default value for sex
    @SerialName("PBIRTH")
    val pbirth: String,
    @SerialName("AGE")
    val age: String,
    @SerialName("PHONENUM")
    val phonenum: String? = null,  // Make phonenum nullable with default value null
    @SerialName("GUBUN")
    val gubun: String? = null,  // Insurance type
    @SerialName("N")
    val n: Int? = null,  // National health insurance number
    @SerialName("FIN")
    val fin: String? = null,  // Final insurance number
    @SerialName("RESERVED")
    val reserved: String? = null  // Reserved field
) 