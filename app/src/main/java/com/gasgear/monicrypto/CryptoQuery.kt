package com.gasgear.monicrypto

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CryptoQuery {
    @GET("ticker/")
    fun cryptoCurrency(
            @Query("start") start : Int?,
            @Query("limit") limit: Int?,
            @Query("convert") convert: String?
    ) : Call<ArrayList<Crypto>>
}