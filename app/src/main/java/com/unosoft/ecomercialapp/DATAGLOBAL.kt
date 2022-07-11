package com.unosoft.ecomercialapp

import android.app.Application
import androidx.room.Room
import com.unosoft.ecomercialapp.db.TablaBasica

class DATAGLOBAL: Application() {

    companion object{
        lateinit var database: TablaBasica
    }

    override fun onCreate() {
        super.onCreate()
        database =  Room.databaseBuilder(this, TablaBasica::class.java, "database").fallbackToDestructiveMigration().build()
    }
}