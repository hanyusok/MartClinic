package com.example.martclinic.domain.repository

import com.example.martclinic.domain.model.MtsWait
import kotlinx.coroutines.flow.Flow

interface MtsWaitRepository {
    suspend fun getAll(): Flow<List<MtsWait>>
    suspend fun getByVisitDate(visidate: String): Flow<List<MtsWait>>
    suspend fun getById(pcode: Int, visidate: String): Flow<MtsWait>
    suspend fun create(mtsWait: MtsWait): Flow<Unit>
    suspend fun update(pcode: Int, visidate: String, mtsWait: MtsWait): Flow<Unit>
    suspend fun delete(pcode: Int, visidate: String): Flow<Unit>
} 