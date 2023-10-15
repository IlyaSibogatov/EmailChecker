package com.example.emailchecker.utils

import android.content.Context.INPUT_METHOD_SERVICE
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS

fun String.validate() = Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun View.hideKeyboard() = (context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
    .hideSoftInputFromWindow(windowToken, HIDE_NOT_ALWAYS)

val mostPopularDomain = listOf(
    "gmail.com", "yahoo.com", "gmail.co.uk", "mail.ru", "inbox.ru", "yandex.ru"
)