package com.example.martclinic.data.remote.model

import com.example.martclinic.domain.model.Person
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse(
    @SerialName("data")
    val data: List<Person>,
    @SerialName("pagination")
    val pagination: Pagination
)

@Serializable
data class Pagination(
    @SerialName("total")
    val total: Int,
    @SerialName("currentPage")
    val currentPage: Int,
    @SerialName("itemsPerPage")
    val itemsPerPage: Int,
    @SerialName("totalPages")
    val totalPages: Int,
    @SerialName("hasNextPage")
    val hasNextPage: Boolean,
    @SerialName("hasPreviousPage")
    val hasPreviousPage: Boolean
) 