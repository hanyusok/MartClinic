package com.example.martclinic.data.remote.service

import com.example.martclinic.domain.model.Mtr
import retrofit2.Response
import retrofit2.http.*

interface MtrApiService {
    @GET("mtr")
    suspend fun getAll(): Response<List<Mtr>>

    @GET("mtr/date/{visidate}")
    suspend fun getByVisitDate(
        @Path("visidate") visidate: String
    ): Response<List<Mtr>>

    @GET("mtr/{pcode}")
    suspend fun getByPcode(
        @Path("pcode") pcode: Int
    ): Response<Mtr>

    @POST("mtr")
    suspend fun create(@Body mtr: Mtr): Response<Unit>

    @PUT("mtr/{pcode}")
    suspend fun update(
        @Path("pcode") pcode: Int,
        @Body mtr: Mtr
    ): Response<Unit>

    @DELETE("mtr/{pcode}")
    suspend fun delete(
        @Path("pcode") pcode: Int
    ): Response<Unit>
} 