package com.example.martclinic.data.repository

import android.util.Log
import com.example.martclinic.data.remote.service.MtrApiService
import com.example.martclinic.domain.model.Mtr
import com.example.martclinic.domain.repository.MtrRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MtrRepositoryImpl @Inject constructor(
    private val apiService: MtrApiService
) : MtrRepository {

    companion object {
        private const val TAG = "MtrRepositoryImpl"
    }

    override suspend fun getAll(): Flow<List<Mtr>> = flow {
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

    override suspend fun getByVisitDate(visidate: String): Flow<List<Mtr>> = flow {
        try {
            val response = apiService.getByVisitDate(visidate)
            if (response.isSuccessful) {
                response.body()?.let { emit(it) } ?: throw Exception("Response body is null")
            } else {
                throw Exception("API call failed: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in getByVisitDate", e)
            throw e
        }
    }

    override suspend fun getByPcode(pcode: Int): Flow<Mtr> = flow {
        try {
            val response = apiService.getByPcode(pcode)
            if (response.isSuccessful) {
                response.body()?.let { emit(it) } ?: throw Exception("Response body is null")
            } else {
                throw Exception("API call failed: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in getByPcode", e)
            throw e
        }
    }

    override suspend fun create(mtr: Mtr): Flow<Unit> = flow {
        try {
            val response = apiService.create(mtr)
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

    override suspend fun update(pcode: Int, mtr: Mtr): Flow<Unit> = flow {
        try {
            val response = apiService.update(pcode, mtr)
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

    override suspend fun delete(pcode: Int): Flow<Unit> = flow {
        try {
            val response = apiService.delete(pcode)
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