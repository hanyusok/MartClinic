package com.example.martclinic.data.repository

import android.util.Log
import com.example.martclinic.data.remote.service.MtsWaitApiService
import com.example.martclinic.domain.model.MtsWait
import com.example.martclinic.domain.repository.MtsWaitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MtsWaitRepositoryImpl @Inject constructor(
    private val apiService: MtsWaitApiService
) : MtsWaitRepository {

    companion object {
        private const val TAG = "MtsWaitRepositoryImpl"

        /**
         * Extracts SEARCHID from RRN input.
         * Example: "721023-1260917" -> "721023-1"
         * @param rrnInput The full RRN string
         * @return The extracted SEARCHID or null if input is invalid
         */
        fun extractSearchId(rrnInput: String): String? {
            return try {
                val parts = rrnInput.split("-")
                if (parts.size != 2) return null
                
                val firstPart = parts[0]
                val secondPart = parts[1]
                
                if (firstPart.length != 6 || secondPart.length != 7) return null
                
                "$firstPart-${secondPart[0]}"
            } catch (e: Exception) {
                Log.e(TAG, "Error extracting SEARCHID from RRN: $rrnInput", e)
                null
            }
        }
    }

    override suspend fun getAll(): Flow<List<MtsWait>> = flow {
        try {
            val response = apiService.getAll()
            if (response.isSuccessful) {
                response.body()?.let { emit(it) } ?: throw Exception("Response body is null")
            } else {
                throw Exception("API call failed: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in getAll", e)
            throw e
        }
    }

    override suspend fun getByVisitDate(visidate: String): Flow<List<MtsWait>> = flow {
        try {
            val response = apiService.getByVisitDate(visidate)
            if (response.isSuccessful) {
                response.body()?.let { emit(it) } ?: emit(emptyList())
            } else if (response.code() == 404) {
                // Return empty list for 404 (no records found)
                emit(emptyList())
            } else {
                throw Exception("API call failed: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in getByVisitDate", e)
            throw e
        }
    }

    override suspend fun getById(pcode: Int, visidate: String): Flow<MtsWait> = flow {
        try {
            val response = apiService.getById(pcode, visidate)
            if (response.isSuccessful) {
                response.body()?.let { emit(it) } ?: throw Exception("Response body is null")
            } else {
                throw Exception("API call failed: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in getById", e)
            throw e
        }
    }

    override suspend fun create(mtsWait: MtsWait): Flow<Unit> = flow {
        try {
            val response = apiService.create(mtsWait)
            if (response.isSuccessful) {
                emit(Unit)
            } else {
                throw Exception("API call failed: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in create", e)
            throw e
        }
    }

    override suspend fun update(pcode: Int, visidate: String, mtsWait: MtsWait): Flow<Unit> = flow {
        try {
            val response = apiService.update(pcode, visidate, mtsWait)
            if (response.isSuccessful) {
                emit(Unit)
            } else {
                throw Exception("API call failed: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in update", e)
            throw e
        }
    }

    override suspend fun delete(pcode: Int, visidate: String): Flow<Unit> = flow {
        try {
            val response = apiService.delete(pcode, visidate)
            if (response.isSuccessful) {
                emit(Unit)
            } else {
                throw Exception("API call failed: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in delete", e)
            throw e
        }
    }
} 