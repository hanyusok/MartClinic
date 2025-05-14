package com.example.martclinic.data.repository

import android.util.Log
import com.example.martclinic.data.remote.model.ApiResponse
import com.example.martclinic.data.remote.model.Pagination
import com.example.martclinic.data.remote.service.PersonApiService
import com.example.martclinic.domain.model.Person
import com.example.martclinic.domain.repository.PersonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PersonRepositoryImpl @Inject constructor(
    private val apiService: PersonApiService
) : PersonRepository {

    companion object {
        private const val TAG = "PersonRepositoryImpl"
    }

    override suspend fun getPerson(pcode: Int): Flow<Person> = flow {
        try {
            val response = apiService.getPersonByPcode(pcode)
            if (response.isSuccessful) {
                response.body()?.firstOrNull()?.let { emit(it) } ?: throw Exception("Response body is null or empty")
            } else {
                throw Exception("API call failed: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getPersons(page: Int, limit: Int, search: String?): Flow<ApiResponse> = flow {
        try {
            val response = apiService.getPersons(page, limit, search)
            if (response.isSuccessful) {
                response.body()?.let { emit(it) } ?: throw Exception("Response body is null")
            } else {
                throw Exception("API call failed: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in getPersons", e)
            throw e
        }
    }

    override suspend fun getPersonsByName(pname: String?): Flow<List<Person>> = flow {
        try {
            if (pname.isNullOrBlank()) {
                Log.d(TAG, "getPersonsByName: pname is null or blank")
                emit(emptyList())
                return@flow
            }
            
            val response = apiService.getPersonsByName(pname)
            if (response.isSuccessful) {
                response.body()?.let { emit(it) } ?: throw Exception("Response body is null")
            } else {
                throw Exception("API call failed: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in getPersonsByName", e)
            throw e
        }
    }

    override suspend fun getPersonsBySearchId(searchId: String): Flow<List<Person>> = flow {
        try {
            if (searchId.isBlank()) {
                Log.d(TAG, "getPersonsBySearchid: searchId is blank")
                emit(emptyList())
                return@flow
            }
            
            val response = apiService.getPersonsBySearchId(searchId)
            if (response.isSuccessful) {
                response.body()?.let { emit(it) } ?: throw Exception("Response body is null")
            } else {
                throw Exception("API call failed: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in getPersonsBySearchid", e)
            throw e
        }
    }

    override suspend fun createPerson(person: Person): Flow<Person> = flow {
        try {
            val response = apiService.createPerson(person)
            if (response.isSuccessful) {
                response.body()?.firstOrNull()?.let { emit(it) } ?: throw Exception("Response body is null or empty")
            } else {
                throw Exception("API call failed: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun updatePerson(person: Person): Flow<Person> = flow {
        try {
            val pcode = person.pcode ?: throw Exception("Person code is required for update")
            val response = apiService.updatePersonByPcode(pcode, person)
            if (response.isSuccessful) {
                response.body()?.let { emit(it) } ?: throw Exception("Response body is null")
            } else {
                throw Exception("API call failed: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun updatePersonByPcode(pcode: Int, person: Person): Flow<Person> = flow {
        try {
            val response = apiService.updatePersonByPcode(pcode, person)
            if (response.isSuccessful) {
                response.body()?.let { emit(it) } ?: throw Exception("Response body is null")
            } else {
                throw Exception("API call failed: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun deletePersonByPcode(pcode: Int): Flow<Unit> = flow {
        try {
            val response = apiService.deletePersonByPcode(pcode)
            if (response.isSuccessful) {
                emit(Unit)
            } else {
                throw Exception("API call failed: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            throw e
        }
    }
}