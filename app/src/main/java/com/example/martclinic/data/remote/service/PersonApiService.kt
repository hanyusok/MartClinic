package com.example.martclinic.data.remote.service

import com.example.martclinic.data.remote.model.ApiResponse
import com.example.martclinic.domain.model.Person
import retrofit2.Response
import retrofit2.http.*

interface PersonApiService {
    @GET("persons")
    suspend fun getPersons(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10,
        @Query("search") search: String? = null
    ): Response<ApiResponse>

    @GET("persons/search")
    suspend fun getPersonsByName(
        @Query("pname") pname: String
    ): Response<List<Person>>

    @GET("persons/search")
    suspend fun getPersonsBySearchId(
        @Query("searchId") searchId: String
    ): Response<List<Person>>

    @GET("persons/{pcode}")
    suspend fun getPersonByPcode(@Path("pcode") pcode: Int): Response<List<Person>>

    @POST("persons")
    suspend fun createPerson(@Body person: Person): Response<List<Person>>

    @PUT("persons/{pcode}")
    suspend fun updatePersonByPcode(
        @Path("pcode") pcode: Int,
        @Body person: Person
    ): Response<Person>

    @DELETE("persons/{pcode}")
    suspend fun deletePersonByPcode(@Path("pcode") pcode: Int): Response<Unit>
} 