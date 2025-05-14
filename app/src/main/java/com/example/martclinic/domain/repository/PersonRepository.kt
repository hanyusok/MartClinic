package com.example.martclinic.domain.repository

import com.example.martclinic.data.remote.model.ApiResponse
import com.example.martclinic.domain.model.Person
import kotlinx.coroutines.flow.Flow

interface PersonRepository {
    suspend fun getPersons(page: Int = 1, limit: Int = 10, search: String? = null): Flow<ApiResponse>
    suspend fun getPersonsByName(pname: String? = null): Flow<List<Person>>
    suspend fun getPersonsBySearchId(searchId: String): Flow<List<Person>>
    suspend fun getPerson(pcode: Int): Flow<Person>
    suspend fun createPerson(person: Person): Flow<Person>
    suspend fun updatePerson(person: Person): Flow<Person>
    suspend fun updatePersonByPcode(pcode: Int, person: Person): Flow<Person>
    suspend fun deletePersonByPcode(pcode: Int): Flow<Unit>
} 