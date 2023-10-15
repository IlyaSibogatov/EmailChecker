package com.example.emailchecker.utils

enum class EmailState(val value: String) {
    IN_CHANGED("in_changed"),
    SUCCESS("success"),
    CHECK_ON_DELIVERABLE("check_on_deliverable"),

    EMPTY("empty"),
    PATTERN_NOT_MATCH("pattern_not_match"),
    REJECTED_EMAIL("rejected_email"),
    INVALID_DOMAIN("invalid_domain"),
    INVALID_SMTP("invalid_smtp"),
    UNKNOWN_ERROR("unknown_error"),

    RISKY("risky_email")
}

enum class DeliverableState(val value: String) {
    DELIVERABLE("deliverable"),
    UNDELIVERABLE("undeliverable"),
    RISKY("risky")
}