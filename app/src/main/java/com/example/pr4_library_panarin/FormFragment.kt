package com.example.pr4_library_panarin
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatSpinner
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
class FormFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_form, container, false)
        val textUser: AppCompatTextView = view.findViewById(R.id.text_user)
        val editTitle: AppCompatEditText = view.findViewById(R.id.edit_title)
        val editAuthor: AppCompatEditText = view.findViewById(R.id.edit_author)
        val editYear: AppCompatEditText = view.findViewById(R.id.edit_year)
        val spinnerGenre: AppCompatSpinner = view.findViewById(R.id.spinner_genre)
        val btnSave: AppCompatButton = view.findViewById(R.id.btn_save)
        val btnBooks: AppCompatButton = view.findViewById(R.id.btn_books)
        val prefs = requireActivity().getSharedPreferences("library", Context.MODE_PRIVATE)
        val gson = Gson()
        val user = gson.fromJson(prefs.getString("user", ""), User::class.java)
        textUser.text = getString(R.string.user, user.login)

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.genres,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerGenre.adapter = adapter
        }

        btnSave.setOnClickListener {
            val title = editTitle.text.toString().trim()
            val author = editAuthor.text.toString().trim()
            val year = editYear.text.toString().trim()

            if (title.isEmpty()) {
                editTitle.error = getString(R.string.error_empty)
                return@setOnClickListener
            }
            if (author.isEmpty()) {
                editAuthor.error = getString(R.string.error_empty)
                return@setOnClickListener
            }
            if (year.isEmpty()) {
                editYear.error = getString(R.string.error_empty)
                return@setOnClickListener
            }

            val book = Book(title, author, year, spinnerGenre.selectedItem.toString())

            val booksKey = "books_" + user.login
            val booksJson = prefs.getString(booksKey, "[]")
            val type = object : TypeToken<MutableList<Book>>() {}.type
            val books: MutableList<Book> = gson.fromJson(booksJson, type)
            books.add(book)
            prefs.edit().putString(booksKey, gson.toJson(books)).apply()

            Toast.makeText(requireContext(), R.string.book_saved, Toast.LENGTH_SHORT).show()
            editTitle.setText("")
            editAuthor.setText("")
            editYear.setText("")
        }

        btnBooks.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, InfoFragment())
                .addToBackStack(null)
                .commit()
        }

        return view
    }
}
