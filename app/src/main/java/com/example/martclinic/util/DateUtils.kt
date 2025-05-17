package com.example.martclinic.util

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object DateUtils {
    private val ISO_FORMATTER = DateTimeFormatter.ISO_DATE_TIME
    private val DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    private val DATE_ONLY_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val TIME_ONLY_FORMATTER = DateTimeFormatter.ofPattern("HH:mm")
    private val KOREAN_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")

    /**
     * Parse ISO 8601 date string to LocalDateTime
     */
    fun parseIsoDate(isoDate: String): LocalDateTime? {
        return try {
            LocalDateTime.parse(isoDate, ISO_FORMATTER)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Format LocalDateTime to ISO 8601 string
     */
    fun formatToIso(dateTime: LocalDateTime): String {
        return dateTime.format(ISO_FORMATTER)
    }

    /**
     * Format ISO 8601 string to display format (yyyy-MM-dd HH:mm)
     */
    fun formatForDisplay(isoDate: String): String {
        return try {
            val dateTime = parseIsoDate(isoDate)
            dateTime?.format(DISPLAY_FORMATTER) ?: isoDate
        } catch (e: Exception) {
            isoDate
        }
    }

    /**
     * Format ISO 8601 string to date only (yyyy-MM-dd)
     */
    fun formatDateOnly(isoDate: String): String {
        return try {
            val dateTime = parseIsoDate(isoDate)
            dateTime?.format(DATE_ONLY_FORMATTER) ?: isoDate
        } catch (e: Exception) {
            isoDate
        }
    }

    /**
     * Format ISO 8601 string to Korean date format (yyyy년 MM월 dd일)
     */
    fun formatKoreanDate(isoDate: String): String {
        return try {
            val dateTime = parseIsoDate(isoDate)
            dateTime?.format(KOREAN_DATE_FORMATTER) ?: isoDate
        } catch (e: Exception) {
            isoDate
        }
    }

    /**
     * Format ISO 8601 string to time only (HH:mm)
     */
    fun formatTimeOnly(isoDate: String): String {
        return try {
            val dateTime = parseIsoDate(isoDate)
            dateTime?.format(TIME_ONLY_FORMATTER) ?: isoDate
        } catch (e: Exception) {
            isoDate
        }
    }

    /**
     * Get current date and time in ISO 8601 format
     */
    fun getCurrentIsoDateTime(): String {
        return LocalDateTime.now().format(ISO_FORMATTER)
    }

    /**
     * Convert Instant to LocalDateTime
     */
    fun instantToLocalDateTime(instant: Instant): LocalDateTime {
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
    }

    /**
     * Convert LocalDateTime to Instant
     */
    fun localDateTimeToInstant(dateTime: LocalDateTime): Instant {
        return dateTime.atZone(ZoneId.systemDefault()).toInstant()
    }

    /**
     * Calculate age from birth date string (ISO 8601 or yyyy-MM-dd).
     * Returns age as a string like "51y 3m" or "Unknown" if invalid.
     */
    fun calculateAge(birthDate: String): String {
        return try {
            val dateStr = if (birthDate.length > 10) birthDate.substring(0, 10) else birthDate
            val localDate = try {
                LocalDateTime.parse("${dateStr}T00:00:00").toLocalDate()
            } catch (e: Exception) {
                java.time.LocalDate.parse(dateStr)
            }
            val now = java.time.LocalDate.now()
            val years = now.year - localDate.year
            val months = now.monthValue - localDate.monthValue
            val adjustedMonths = if (months < 0) months + 12 else months
            "${years}y ${adjustedMonths}m"
        } catch (e: Exception) {
            "Unknown"
        }
    }
} 