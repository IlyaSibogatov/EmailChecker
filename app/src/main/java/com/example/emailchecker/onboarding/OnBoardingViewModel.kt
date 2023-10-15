package com.example.emailchecker.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emailchecker.remote.services.VerificationService
import com.example.emailchecker.utils.Constants.API_KEY
import com.example.emailchecker.utils.Constants.EMPTY_STRING
import com.example.emailchecker.utils.Constants.SIGN_AT
import com.example.emailchecker.utils.DeliverableState
import com.example.emailchecker.utils.EmailState
import com.example.emailchecker.utils.mostPopularDomain
import com.example.emailchecker.utils.validate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val verificationService: VerificationService
) : ViewModel() {
    private val _email = MutableLiveData(EMPTY_STRING)

    private var _autoCompetedList = mutableListOf<String>()
    private val _filteredList = MutableLiveData<List<String>>()
    val autoCompletedList: LiveData<List<String>> = _filteredList

    val emailState = MutableLiveData(EmailState.IN_CHANGED)

    fun updateEmail(value: String) {
        _email.value = value
        emailState.value = EmailState.IN_CHANGED
        _email.value?.let { email ->
            if (email.contains(SIGN_AT) && email.first().toString() != SIGN_AT) {

                val strings = email.split(SIGN_AT)
                _autoCompetedList = mostPopularDomain.map { strings[0] + SIGN_AT + it }.toMutableList()
                _filteredList.value = _autoCompetedList

                val items = _autoCompetedList
                _filteredList.value = items.filter {
                    it.contains(email) && it != email
                }
            } else {
                _autoCompetedList.clear()
                _filteredList.value = listOf()
            }
        }
    }

    fun checkEmail() {
        _email.value?.let {
            emailState.value = when {
                it.isEmpty() -> EmailState.EMPTY
                !it.validate() -> EmailState.PATTERN_NOT_MATCH
                else -> EmailState.CHECK_ON_DELIVERABLE
            }
        }
        if (emailState.value == EmailState.CHECK_ON_DELIVERABLE) emailIsExist()
    }

    private fun emailIsExist() {
        viewModelScope.launch {
            _email.value?.let {
                verificationService.verifyEmail(email = it, apiKey = API_KEY).let { response ->
                    when (response.result) {
                        DeliverableState.DELIVERABLE.value -> {
                            emailState.value = EmailState.SUCCESS
                        }

                        DeliverableState.UNDELIVERABLE.value -> {
                            emailState.value = when (response.reason) {
                                EmailState.REJECTED_EMAIL.value -> EmailState.REJECTED_EMAIL
                                EmailState.INVALID_DOMAIN.value -> EmailState.INVALID_DOMAIN
                                EmailState.INVALID_SMTP.value -> EmailState.INVALID_SMTP
                                else -> EmailState.UNKNOWN_ERROR
                            }
                        }

                        DeliverableState.RISKY.value -> {
                            emailState.value = EmailState.RISKY
                        }

                        else -> EmailState.UNKNOWN_ERROR
                    }
                }
            }
        }
    }
}