package com.example.martclinic.data.remote.service

import com.example.martclinic.domain.model.MtsWait
import retrofit2.Response
import retrofit2.http.*

interface MtsWaitApiService {
    @GET("mtswait")
    suspend fun getAll(): Response<List<MtsWait>>

    @GET("mtswait/date/{visidate}")
    suspend fun getByVisitDate(
        @Path("visidate") visidate: String
    ): Response<List<MtsWait>>

    @GET("mtswait/{pcode}/{visidate}")
    suspend fun getById(
        @Path("pcode") pcode: Int,
        @Path("visidate") visidate: String
    ): Response<MtsWait>

    @POST("mtswait")
    suspend fun create(@Body mtsWait: MtsWait): Response<Unit>

    @PUT("mtswait/{pcode}/{visidate}")
    suspend fun update(
        @Path("pcode") pcode: Int,
        @Path("visidate") visidate: String,
        @Body mtsWait: MtsWait
    ): Response<Unit>

    @DELETE("mtswait/{pcode}/{visidate}")
    suspend fun delete(
        @Path("pcode") pcode: Int,
        @Path("visidate") visidate: String
    ): Response<Unit>
} 