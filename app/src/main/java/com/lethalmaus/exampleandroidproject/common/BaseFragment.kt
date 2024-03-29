package com.lethalmaus.exampleandroidproject.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.lethalmaus.exampleandroidproject.MainActivity
import com.lethalmaus.exampleandroidproject.databinding.ActivityMainBinding

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

open class BaseFragment<T : ViewBinding>(private val inflate: Inflate<T>) : Fragment() {

    private var _binding: T? = null
    val binding: T get() = _binding!!
    private var _activityBinding: ActivityMainBinding? = null
    val activityBinding: ActivityMainBinding get() = _activityBinding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = inflate.invoke(inflater, container, false)
        _activityBinding = (requireActivity() as MainActivity).binding
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _activityBinding = null
    }

    fun minimizeKeyboard() {
        requireActivity().currentFocus?.let {
            val imm: InputMethodManager? = ContextCompat.getSystemService(requireContext(), InputMethodManager::class.java)
            imm?.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }
}