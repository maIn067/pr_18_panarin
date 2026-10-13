package com.example.pr4_library_panarin

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import com.google.gson.Gson
class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        val editLogin: AppCompatEditText = view.findViewById(R.id.edit_login)
        val editPassword: AppCompatEditText = view.findViewById(R.id.edit_password)
        val btnLogin: AppCompatButton = view.findViewById(R.id.btn_login)

        val prefs = requireActivity().getSharedPreferences("library", Context.MODE_PRIVATE)
        val gson = Gson()

        val userJson = prefs.getString("user", null)
        if (userJson != null) {
            val savedUser = gson.fromJson(userJson, User::class.java)
            editLogin.setText(savedUser.login)
        }

        btnLogin.setOnClickListener {
            val login = editLogin.text.toString().trim()
            val password = editPassword.text.toString().trim()

            if (login.isEmpty()) {
                editLogin.error = getString(R.string.error_empty)
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                editPassword.error = getString(R.string.error_empty)
                return@setOnClickListener
            }

            val savedJson = prefs.getString("user", null)
            if (savedJson != null) {
                val savedUser = gson.fromJson(savedJson, User::class.java)
                if (savedUser.login == login && savedUser.password != password) {
                    editPassword.error = getString(R.string.error_password)
                    return@setOnClickListener
                }
            }

            val user = User(login, password)
            prefs.edit().putString("user", gson.toJson(user)).apply()

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, FormFragment())
                .commit()
        }

        return view
    }
}
