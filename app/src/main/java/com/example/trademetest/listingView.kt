package com.example.trademetest

import RecyclerAdapter
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trademetest.datatypes.SearchResults
import com.example.trademetest.networking.ServiceBuilder
import com.example.trademetest.networking.networkInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class listingView : AppCompatActivity() {
    val CUSTOMER_KEY = "A1AC63F0332A131A78FAC304D007E7D1"
    val SECRET_KEY = "EC7F18B17A062962C6930A8AE88B16C7"
    val SIG_METHOD = "PLAINTEXT"

    var rootCategory: Category? = null
    var SearchResults: SearchResults? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listing_view)
        URLQuery(intent.extras?.getString("CATEGORY_NUM"))
        listingQuery(intent.extras?.getString("CATEGORY_NUM"))
    }



    fun URLQuery(id: String?) {
        val request = ServiceBuilder.buildService(networkInterface::class.java)
        val call = request.getRoot(id)

        call.enqueue(object : Callback<Category> {
            override fun onResponse(call: Call<Category>, response: Response<Category>) {
                if (response.isSuccessful) {
                    rootCategory = response.body()
                }
            }

            override fun onFailure(call: Call<Category>, t: Throwable) {
                var textView = findViewById<TextView>(R.id.text)
                println("---------------------------------------------------------")
                t.printStackTrace()
                println("---------------------------------------------------------")
            }

        })
    }

    fun listingQuery(id: String?)
    {
        val request = ServiceBuilder.buildService(networkInterface::class.java)
        val call = request.getListing(id, 20)
        println(call.request().url().toString())

        call.enqueue(object : Callback<SearchResults> {
            override fun onResponse(call: Call<SearchResults>, response: Response<SearchResults>) {
                if (response.isSuccessful) {
                    Toast.makeText(baseContext, "Success!", Toast.LENGTH_LONG).show()
                    SearchResults = response.body()
                    setUI()
                    configRefine()
                } else {
                    Toast.makeText(baseContext, "Fail!", Toast.LENGTH_LONG).show()
                    println(response.code())
                }
            }

            override fun onFailure(call: Call<SearchResults>, t: Throwable) {
                Toast.makeText(baseContext, "Hard Fail", Toast.LENGTH_LONG).show()
                println("---------------------------------------------------------")
                t.printStackTrace()
                println("---------------------------------------------------------")
            }

        })
    }

    private fun configRefine() {
        val button = findViewById<Button>(R.id.refineButton)

        if (rootCategory?.Subcategories == null)
        {
            button.setTextColor(Color.LTGRAY);
            button.isEnabled = false
        }
    }

    fun refine(view: View)
    {
        val intent = Intent(this, refine::class.java)
        intent.putExtra("CATEGORY_NUM", rootCategory?.Number)
        startActivity(intent)
    }

    private fun setUI() {
        var listings = findViewById<RecyclerView>(R.id.listingViews)
        if(SearchResults?.List != null)
        {
            var listingAdapter = SearchResults!!.List?.let { RecyclerAdapter(it, this) }
            listings.layoutManager = LinearLayoutManager(this)

            println("connecting adapter")
            listings.adapter = listingAdapter

        }
    }
}