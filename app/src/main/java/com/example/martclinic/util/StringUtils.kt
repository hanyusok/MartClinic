package com.example.martclinic.util

import android.util.Log
import java.time.LocalDateTime

object StringUtils {
    private const val TAG = "StringUtils"

    /**
     * Extracts SEARCHID from RRN input.
     * Format: YYMMDD-F where:
     * - YYMMDD is the first 6 digits (birth date)
     * - F is the first digit after birth date
     * Example: "7210231260917" -> "721023-1"
     * @param rrnInput The full RRN string without hyphen (13 digits)
     * @return The formatted SEARCHID or null if input is invalid
     */
    fun extractSearchId(rrnInput: String): String? {
        return try {
            // Validate input length
            if (rrnInput.length != 13) return null
            
            // Validate all characters are digits
            if (!rrnInput.all { it.isDigit() }) return null
            
            // Extract birth date part (first 6 digits) and first digit after birth date
            val birthDatePart = rrnInput.substring(0, 6)
            val firstDigit = rrnInput[6]
            
            // Format as YYMMDD-F
            "$birthDatePart-$firstDigit"
        } catch (e: Exception) {
            Log.e(TAG, "Error extracting SEARCHID from RRN: $rrnInput", e)
            null
        }
    }

    /**
     * Extracts birth date from Korean Resident Registration Number (RRN).
     * Example: "7210231260917" -> "1972-10-23"
     * @param rrn The full RRN string without hyphen (13 digits)
     * @return The birth date in ISO format (YYYY-MM-DD) or null if invalid
     */
    fun extractBirthDateFromRRN(rrn: String): String? {
        // 입력 유효성 검사
        if (rrn.length != 13) {
            return null // 길이가 13자리가 아닌 경우
        }

        // 숫자만 있는지 확인
        if (!rrn.all { it.isDigit() }) {
            return null // 숫자가 아닌 문자가 포함된 경우
        }

        // 성별 코드 및 생년월일 추출
        val genderCode = rrn[6]
        val yearPrefix = when (genderCode) {
            '1', '2' -> "19" // 1900년대
            '3', '4' -> "20" // 2000년대
            else -> return null // 유효하지 않은 성별 코드
        }

        // YYMMDD 추출
        val year = rrn.substring(0, 2)
        val month = rrn.substring(2, 4)
        val day = rrn.substring(4, 6)

        // 월, 일 유효성 검사
        val monthInt = month.toIntOrNull()
        val dayInt = day.toIntOrNull()
        if (monthInt !in 1..12 || dayInt !in 1..31) {
            return null // 유효하지 않은 월 또는 일
        }

        // 생일 형식(YYYY-MM-DD)으로 반환
        return try {
            val dateStr = String.format("%s%s-%s-%s", yearPrefix, year, month, day)
            // Convert to ISO format for consistency
            val localDateTime = LocalDateTime.parse("${dateStr}T00:00:00")
            DateUtils.formatToIso(localDateTime)
        } catch (e: Exception) {
            Log.e(TAG, "Error extracting birth date from RRN: $rrn", e)
            null // 예외 발생 시 null 반환
        }
    }

    /**
     * Formats a phone number from digits-only to formatted string.
     * Example: "01082591548" -> "010-8259-1548"
     * @param phoneNumber The phone number string without hyphens
     * @return The formatted phone number or null if invalid
     */
    fun formatPhoneNumber(phoneNumber: String): String? {
        return try {
            // Validate input
            if (phoneNumber.length != 11 || !phoneNumber.all { it.isDigit() }) {
                return null
            }
            
            // Format as XXX-XXXX-XXXX
            "${phoneNumber.substring(0, 3)}-${phoneNumber.substring(3, 7)}-${phoneNumber.substring(7)}"
        } catch (e: Exception) {
            Log.e(TAG, "Error formatting phone number: $phoneNumber", e)
            null
        }
    }

    // formatKoreanDate function has been moved to DateUtils.kt
} 