package com.example.emailchecker.onboarding

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat.getColor
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.emailchecker.R
import com.example.emailchecker.adapter.AutoCompletedAdapter
import com.example.emailchecker.databinding.FragmentOnboardingBinding
import com.example.emailchecker.utils.Constants.EMPTY_STRING
import com.example.emailchecker.utils.EmailState
import com.example.emailchecker.utils.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingFragment : Fragment(R.layout.fragment_onboarding) {

    private lateinit var binding: FragmentOnboardingBinding
    private lateinit var autocompleteAdapter: AutoCompletedAdapter
    private val viewModel by viewModels<OnBoardingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentOnboardingBinding.bind(view)
        autocompleteAdapter = AutoCompletedAdapter { fullName ->
            binding.emailEditTextView.apply {
                setText(fullName)
                setSelection(this.length())
            }
        }

        binding.apply {
            mostPopularDomains.adapter = autocompleteAdapter
            emailEditTextView.apply {
                doOnTextChanged { text, _, _, _ ->
                    viewModel.updateEmail(text.toString())
                }
                setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        checkEmail()
                        return@setOnEditorActionListener true
                    }
                    false
                }
            }
            confirmButton.setOnClickListener { checkEmail() }
        }

        observeLiveData()
    }

    private fun checkEmail() {
        requireView().hideKeyboard()
        viewModel.checkEmail()
    }

    private fun observeLiveData() {
        viewModel.apply {
            emailState.observe(viewLifecycleOwner) {
                binding.errorTextView.apply {

                    setTextColor(
                        when (it) {
                            EmailState.SUCCESS -> getColor(requireContext(), R.color.green)
                            EmailState.RISKY -> getColor(requireContext(), R.color.orange)
                            else -> getColor(requireContext(), R.color.red)
                        }
                    )
                    visibility = if (it == EmailState.IN_CHANGED) GONE else VISIBLE

                    this.text = when (it) {
                        EmailState.SUCCESS ->
                            resources.getString(R.string.success_email)

                        EmailState.EMPTY -> resources.getString(R.string.error_empty)

                        EmailState.PATTERN_NOT_MATCH ->
                            resources.getString(R.string.error_pattern_not_match)

                        EmailState.REJECTED_EMAIL ->
                            resources.getString(R.string.error_rejected_email)

                        EmailState.INVALID_DOMAIN ->
                            resources.getString(R.string.error_invalid_domain)

                        EmailState.INVALID_SMTP ->
                            resources.getString(R.string.error_invalid_smtp)

                        EmailState.UNKNOWN_ERROR ->
                            resources.getString(R.string.error_unknown)

                        EmailState.RISKY -> resources.getString(R.string.warning_risky)

                        else -> EMPTY_STRING
                    }
                }
            }
            autoCompletedList.observe(viewLifecycleOwner) {
                autocompleteAdapter.setList(it)
            }
        }
    }
}