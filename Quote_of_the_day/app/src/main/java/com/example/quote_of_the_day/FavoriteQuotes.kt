package com.example.quote_of_the_day

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerview.RvAdapter
import com.example.recyclerview.RvModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.text.Typography.quote

class FavoriteQuotes : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_favorite_quotes)

        val datalist = ArrayList<RvModel>() // Initialize an empty ArrayList

        val db = FirebaseFirestore.getInstance()
        db.collection("FavoriteQuotesList")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val quote = document.getString("quote")
                    if (quote != null) {
                        // Create an instance of RvModel and add it to datalist
                        var q = "\"${quote}\""
                        datalist.add(RvModel(q))
                    }
                }
                // Initialize RecyclerView and Adapter after fetching data
                val rvAdapter = RvAdapter(datalist, this)
                val rv = findViewById<RecyclerView>(R.id.recyclerView)
                rv.layoutManager = LinearLayoutManager(this)
                rv.adapter = rvAdapter
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Failed to fetch quotes: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
