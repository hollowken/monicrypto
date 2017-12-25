package com.gasgear.monicrypto

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.TextView


class CryptoAdapter(private val cryptoList : ArrayList<Crypto>) : RecyclerView.Adapter<CryptoAdapter.ViewHolder>() {
    val downColor = "#C40000"
    val upColor = "#00C0D6"

    override fun getItemCount(): Int {
        return cryptoList.count()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder (view) {
        val name = view.findViewById<TextView>(R.id.name)
        val symbol = view.findViewById<TextView>(R.id.symbol)
        val price = view.findViewById<TextView>(R.id.price)
        val percent_change_24h = view.findViewById<TextView>(R.id.percent_change_24h)
        val percent_change_7d = view.findViewById<TextView>(R.id.percent_change_7d)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CryptoAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.crypto_list_row, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CryptoAdapter.ViewHolder, position: Int) {
        val crypto : Crypto = cryptoList[position]
        holder.name.text = crypto.name
        holder.symbol.text = crypto.symbol

        var temp = ""

        when (crypto.currency) {
            "RUB" -> {
                temp = crypto.price_rub
                if ((temp.substring(temp.indexOf('.'))).length > 2) {
                    temp = temp.substring(0, temp.indexOf('.') + 3)
                }
                holder.price.text = ("$temp руб.")
            }
            "EUR" -> {
                temp = crypto.price_eur
                if ((temp.substring(temp.indexOf('.'))).length > 2) {
                    temp = temp.substring(0, temp.indexOf('.') + 3)
                }
                holder.price.text = ("$temp €")
            }
            else -> {
                temp = crypto.price_usd
                if ((temp.substring(temp.indexOf('.'))).length > 2) {
                    temp = temp.substring(0, temp.indexOf('.') + 3)
                }
                holder.price.text = ("$temp $")
            }
        }

        if (crypto.percent_change_24h.contains('-')) {
            holder.percent_change_24h.setTextColor(Color.parseColor(downColor))
        } else {
            holder.percent_change_24h.setTextColor(Color.parseColor(upColor))
        }
        holder.percent_change_24h.text = (crypto.percent_change_24h + " %")

        if (crypto.percent_change_7d.contains('-')) {
            holder.percent_change_7d.setTextColor(Color.parseColor(downColor))
        } else {
            holder.percent_change_7d.setTextColor(Color.parseColor(upColor))
        }
        holder.percent_change_7d.text = (crypto.percent_change_7d + " %")

    }
}