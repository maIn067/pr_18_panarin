package com.example.pr4_library_panarin

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class InfoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_info, container, false)

        val textUser: AppCompatTextView = view.findViewById(R.id.text_user)
        val textBooks: AppCompatTextView = view.findViewById(R.id.text_books)
        val btnBack: AppCompatButton = view.findViewById(R.id.btn_back)

        val prefs = requireActivity().getSharedPreferences("library", Context.MODE_PRIVATE)
        val gson = Gson()

        val user = gson.fromJson(prefs.getString("user", ""), User::class.java)
        textUser.text = getString(R.string.user, user.login)

        val booksJson = prefs.getString("books_" + user.login, "[]")
        val type = object : TypeToken<List<Book>>() {}.type
        val books: List<Book> = gson.fromJson(booksJson, type)

        if (books.isEmpty()) {
            textBooks.text = getString(R.string.no_books)
        } else {
            var text = ""
            for (i in books.indices) {
                val book = books[i]
                text += "${i + 1}. ${book.title}\n"
                text += "Автор: ${book.author}\n"
                text += "Год: ${book.year}\n"
                text += "Жанр: ${book.genre}\n\n"
            }
            textBooks.text = text
        }

        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return view
    }
}
