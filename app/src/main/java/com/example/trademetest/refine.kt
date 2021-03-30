package com.example.trademetest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import com.example.trademetest.networking.ServiceBuilder
import com.example.trademetest.networking.networkInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class refine : AppCompatActivity() {
    var rootCategory: Category? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        println("hello world")

        URLQuery(intent.extras?.getString("CATEGORY_NUM"))
    }

    fun onClick(view: View, get: String)
    {
        var intent = Intent(baseContext, listingView::class.java)
        for (i in rootCategory?.Subcategories!!)
        {
            if(i.Name == get)
            {
                println("____________________________")
                println(i.Number)
                intent.putExtra("CATEGORY_NUM", i.Number)
                startActivity(intent)
            }
        }

        //Toast.makeText(this, "no such cat found!", Toast.LENGTH_LONG).show()
    }

    private fun getCatNumber(tag: Any) {
        for (i in getCatArray())
        {
        }
    }

    private fun setUI() {
        var catArray = getCatArray()
        var adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, catArray)
        var listView = findViewById<ListView>(R.id.lisitings)

        listView.setOnItemClickListener(AdapterView.OnItemClickListener {
                parent, view, position, id -> onClick(view, getCatArray().get(position)) })
        listView.adapter = adapter
    }

    private fun getCatArray(): ArrayList<String> {
        val array = arrayListOf<String>()
        if(rootCategory != null)
        {
            for(i in rootCategory!!.Subcategories!!)
            {
                if (!i.Name.isNullOrEmpty())
                    array.add(i.Name)
            }
            return array
        }
        return array
    }

    fun URLQuery(id: String?) {
        val request = ServiceBuilder.buildService(networkInterface::class.java)
        val call = request.getRoot(id)

        call.enqueue(object : Callback<Category> {
            override fun onResponse(call: Call<Category>, response: Response<Category>) {
                if (response.isSuccessful)
                {
                    rootCategory = response.body()
                    setUI()
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
}