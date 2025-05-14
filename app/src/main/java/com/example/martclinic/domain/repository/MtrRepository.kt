package com.example.martclinic.domain.repository

import com.example.martclinic.domain.model.Mtr
import kotlinx.coroutines.flow.Flow

interface MtrRepository {
    suspend fun getAll(): Flow<List<Mtr>>
    suspend fun getByVisitDate(visidate: String): Flow<List<Mtr>>
    suspend fun getByPcode(pcode: Int): Flow<Mtr>
    suspend fun create(mtr: Mtr): Flow<Unit>
    suspend fun update(pcode: Int, mtr: Mtr): Flow<Unit>
    suspend fun delete(pcode: Int): Flow<Unit>
} 