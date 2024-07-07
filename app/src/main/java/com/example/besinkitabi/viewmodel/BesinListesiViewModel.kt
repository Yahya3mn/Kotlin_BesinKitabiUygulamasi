package com.example.besinkitabi.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.besinkitabi.model.Besin
import com.example.besinkitabi.roomdb.BesinDatabase
import com.example.besinkitabi.service.BesinAPIServis
import com.example.besinkitabi.util.OzelSharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BesinListesiViewModel(application: Application) : AndroidViewModel(application) {

    val besinler = MutableLiveData<List<Besin>>()
    val besinHataMesaji = MutableLiveData<Boolean>()
    val besinYukleniyor = MutableLiveData<Boolean>()


    private val besinApiServis = BesinAPIServis()
    private val ozelSharedPreference = OzelSharedPreferences(getApplication())

    private val guncellemeZamani = 10 * 60 * 1000 * 1000 * 1000L

    fun refreshData(){
        val kaydedilmeZamani = ozelSharedPreference.zamaniAl()
        if(kaydedilmeZamani != null && kaydedilmeZamani != 0L && System.nanoTime() - kaydedilmeZamani < guncellemeZamani){
            //Roomdan verileri alacağız
            verileriRoomdanAl()
        }else{
            verileriInternettenAl()
        }
    }

    fun refreshDataFromInternet(){
        verileriInternettenAl()
    }

    private fun verileriRoomdanAl(){
        besinYukleniyor.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val besinListesi = BesinDatabase(getApplication()).BesinDAO().getAllBesin()
            withContext(Dispatchers.Main){
                besinleriGoster(besinListesi)
                Toast.makeText(getApplication(),"Besinleri Roomdan Aldık",Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun verileriInternettenAl(){

        besinYukleniyor.value = true

        viewModelScope.launch(Dispatchers.IO) {
            val besinListesi = besinApiServis.getData()
            withContext(Dispatchers.Main){
                besinYukleniyor.value = false
                //room'a kaydedecez
                roomaKaydet(besinListesi)
                Toast.makeText(getApplication(),"Besinleri Internetten aldık",Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun besinleriGoster(besinListesi: List<Besin>){
        besinler.value = besinListesi
        besinHataMesaji.value = false
        besinYukleniyor.value = false
    }

    private fun roomaKaydet(besinListesi : List<Besin>){
        viewModelScope.launch {
            val dao = BesinDatabase(getApplication()).BesinDAO()
            dao.deleteAllBesin()
            //Listeleri tek tel alır
           val uuidListesi = dao.insertAll(*besinListesi.toTypedArray())
            var i=0
            while(i<besinListesi.size){
                besinListesi[i].uuid = uuidListesi[i].toInt()
                i++
            }
            besinleriGoster(besinListesi)
        }
        ozelSharedPreference.zamaniKaydet(System.nanoTime())
    }
}