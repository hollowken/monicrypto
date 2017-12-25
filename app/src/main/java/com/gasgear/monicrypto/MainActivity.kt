package com.gasgear.monicrypto

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.*
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private val API_BASE_URL = "https://api.coinmarketcap.com/v1/"
    val httpClient = OkHttpClient.Builder()
    val builder = Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
    val retrofit = builder.client(httpClient.build()).build()
    val client = retrofit.create(CryptoQuery::class.java)

    var currentCurrency : String = "USD"

    private var cryptoList: ArrayList<Crypto> = ArrayList()
    private lateinit var recyclerView : RecyclerView
    private lateinit var cAdapter: CryptoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar as Toolbar)

        sendCallChangeCurrency("USD")

        cAdapter = CryptoAdapter(cryptoList)
        recyclerView = recycler_view
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        recyclerView.adapter = cAdapter

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val spinner : Spinner = menu!!.findItem(R.id.spinner).actionView as Spinner
        val adapter : ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(this,
                R.array.action_bar_spinner,
                R.layout.spinner_item)

        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val currencyTextView = p1 as TextView
                sendCallChangeCurrency(currencyTextView.text.toString())
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
        return super.onCreateOptionsMenu(menu)
    }

    fun sendCallChangeCurrency(currency: String) {
        currentCurrency = currency

        val call = client.cryptoCurrency(null, 20, currency)

        call.enqueue(object : Callback<ArrayList<Crypto>> {
            override fun onResponse(call: Call<ArrayList<Crypto>>?, response: Response<ArrayList<Crypto>>?) {
                if (response != null) {
                    cryptoList.clear()

                    for (item in response.body()!!) {
                        cryptoList.add(item)
                        cryptoList[cryptoList.size - 1].currency = currentCurrency
                    }

                    cAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<ArrayList<Crypto>>?, t: Throwable?) {
                Toast.makeText(this@MainActivity, "Something going wrong, try later.", Toast.LENGTH_SHORT).show()
            }
        })

    }
}
