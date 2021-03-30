package com.example.trademetest

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.trademetest.networking.ServiceBuilder
import com.example.trademetest.networking.networkInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

enum class Areas{
    NotSpecified, All, Marketplace, Property, Motors, Jobs, Services
}

class MainActivity : AppCompatActivity() {
    var rootCategory: Category? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        println("hello world")

        URLQuery("0")
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

    fun URLQuery(id : String) {
        val request = ServiceBuilder.buildService(networkInterface::class.java)
        val call = request.getRoot(id)

        call.enqueue(object : Callback<Category>{
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


data class Category(val Name: String?,
                    val Number: String?,
                    val Path: String?,
                    val Subcategories: ArrayList<Category>?,
                    val Count: Integer,
                    val IsRestricted: Boolean,
                    val HasLegalNotice: Boolean,
                    val HasClassifieds: Boolean,
                    val AreaOfBusiness: Areas,
                    val CanHaveSecondCategory: Boolean,
                    val CanBeSecondCategory: Boolean,
                    val isLeaf: Boolean) {

    override fun toString(): String {
        if (Name.isNullOrEmpty())
        {
            return ""
        }
        else
        {
            return Name
        }
    }

}