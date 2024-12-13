package com.example.glusity.data

import android.content.Context
import android.content.SharedPreferences

object UserPreference {
  fun initPref(context: Context, name: String): SharedPreferences {
    return context.getSharedPreferences(name, Context.MODE_PRIVATE)
  }

  private fun editorPreference(context: Context, name: String): SharedPreferences.Editor {
    val sharedPref = context.getSharedPreferences(name, Context.MODE_PRIVATE)
    return sharedPref.edit()
  }

  fun saveSessionAndToken(token: String, isLoggedIn: Boolean, context: Context) {
    val editor = editorPreference(context, "onSignIn")
    editor.putString("token", token)
    editor.putBoolean("isLoggedIn", isLoggedIn)
    editor.apply()
  }

  fun isLoggedIn(context: Context): Boolean {
    val sharedPref = initPref(context, "onSignIn")
    return sharedPref.getBoolean("isLoggedIn", false) && !getAccessToken(context).isNullOrEmpty()
  }


  fun logOut(context: Context) {
    val editor = editorPreference(context, "onSignIn")
    editor.remove("token")
    editor.remove("isLoggedIn") // Remove session info
    editor.apply()
  }
  fun saveRefreshToken(refreshToken: String, context: Context) {
    val editor = editorPreference(context, "onSignIn")
    editor.putString("refreshToken", refreshToken)
    editor.apply()
  }

  fun getRefreshToken(context: Context): String? {
    val sharedPref = initPref(context, "onSignIn")
    return sharedPref.getString("refreshToken", null)
  }
  fun getAccessToken(context: Context): String? {
    val sharedPref = initPref(context, "onSignIn")
    return sharedPref.getString("token", null)
  }
}