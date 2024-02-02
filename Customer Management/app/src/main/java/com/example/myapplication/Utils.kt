package com.example.myapplication

import android.view.View
import android.view.Window
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar

fun hideKeyboard(window: Window, view: View) = WindowCompat.getInsetsController(window, view)?.hide(WindowInsetsCompat.Type.ime())

fun popup(message: String, view: View) = Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()