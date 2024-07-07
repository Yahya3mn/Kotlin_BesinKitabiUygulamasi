package com.example.besinkitabi.service

import com.example.besinkitabi.model.Besin
import retrofit2.http.GET

interface BesinAPI {

    //BASE URL -> https://raw.githubusercontent.com/
    //ENDPOINT -> atilsamancioglu/BTK20-JSONVeriSeti/master/besinler.json

    @GET("atilsamancioglu/BTK20-JSONVeriSeti/master/besinler.json")
    suspend fun getBesin() : List<Besin>
}