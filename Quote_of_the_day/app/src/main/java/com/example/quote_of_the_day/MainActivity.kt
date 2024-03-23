package com.example.quote_of_the_day

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.recyclerview.RvModel
import com.google.firebase.Firebase
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val quoteTextView = findViewById<TextView>(R.id.quote)

        val db = FirebaseFirestore.getInstance()
        db.collection("QuotesList")
            .get()
            .addOnSuccessListener { documents ->
                val quotesList = mutableListOf<String>()

                for (document in documents) {
                    val quote = document.getString("quote")
                    quote?.let { quotesList.add(it) }
                }

                if (quotesList.isNotEmpty()) {
                    val randomQuoteIndex = (0 until quotesList.size).random()
                    quoteTextView.text = "\" ${quotesList[randomQuoteIndex]} \""
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Failed to fetch quotes: ${exception.message}", Toast.LENGTH_SHORT).show()
            }


        val add= findViewById<ImageButton>(R.id.add_to_fav)

        add.setOnClickListener {
            val newQuote = quoteTextView.text.toString()

            val data = hashMapOf("quote" to newQuote)

            val db = FirebaseFirestore.getInstance()
            db.collection("FavoriteQuotesList")
                .add(data)
                .addOnSuccessListener { documentReference ->
                    Toast.makeText(this, "Process Successful", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Failed to add quote: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }


        val saved= findViewById<ImageButton>(R.id.saved)
        saved.setOnClickListener {
            val intent = Intent(this, FavoriteQuotes::class.java)
            startActivity(intent)
        }

        val share= findViewById<ImageButton>(R.id.shareButton)
        share.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, quoteTextView.text.toString())
            startActivity(Intent.createChooser(shareIntent, "Share via"))
        }

    }
}

