package com.example.swapcampus_isaaclee.ui.i18n

import androidx.compose.runtime.Composable
import kotlinx.datetime.LocalDate
import platform.Foundation.*
import kotlin.text.toLong
import kotlin.toString

@Composable
actual fun currentLanguageTag(): String {
    return NSLocale.currentLocale.localeIdentifier
}

actual fun formatPrice(price: Double, languageTag: String): String {
    val formatter = NSNumberFormatter().apply {
        numberStyle = NSNumberFormatterCurrencyStyle
        locale = NSLocale(languageTag)
    }
    return formatter.stringFromNumber(NSNumber(price)) ?: "$price €"
}

actual fun formatDate(date: LocalDate, languageTag: String): String {
    val components = NSDateComponents().apply {
        year = date.year.toLong()
        month = date.monthNumber.toLong()
        day = date.dayOfMonth.toLong()
    }
    val calendar = NSCalendar.currentCalendar
    val nsDate = calendar.dateFromComponents(components) ?: return date.toString()

    val formatter = NSDateFormatter().apply {
        dateStyle = NSDateFormatterMediumStyle
        locale = NSLocale(languageTag)
    }
    return formatter.stringFromDate(nsDate)
}

