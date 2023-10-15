package com.example.emailchecker.remote.models

data class Response(
    val accept_all: Boolean,
    val did_you_mean: Any,
    val disposable: Boolean,
    val domain: String,
    val email: String,
    val free: Boolean,
    val message: String,
    val reason: String,
    val result: String,
    val role: Boolean,
    val sendex: Float,
    val success: Boolean,
    val user: String
)