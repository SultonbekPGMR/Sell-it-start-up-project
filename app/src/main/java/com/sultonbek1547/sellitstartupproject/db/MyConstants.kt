package com.sultonbek1547.sellitstartupproject.db

import android.util.Log
import com.sultonbek1547.sellitstartupproject.models.MyProduct
import com.sultonbek1547.sellitstartupproject.models.User


object MyConstants {
    fun getUser(userId: String): User {
        userList.forEach {
            if (it.uid == userId) return it

        }
        return userList[0]
    }

    val mainCategories = arrayListOf<String>(
        "Elektronika",
        "Transport",
        "Bolalar uchun",
        "Uy va bog'",
        "Fitnes&hobbi",
        "Servislar",
        "Hayvonlar",
        "Mebel",
        "Ko'chmas mulk",
    )

    private val listNamesElektronika = arrayListOf<String>(
        "Telefonlar",
        "Kompyuterlar",
        "Aksessuarlar",
        "Audio texnika",
        "Foto va Video",
        "Televizor va Video texnika",
        "Elektronika boshqa",
    )

    private val listNamesTransport = arrayListOf(
        "Yengil avtomashinalar",
        "Avto ehtiyot qismlari",
        "Shinalar, diskalar va g'ildiraklar",
        "Moto",
        "Moto ehtiyot qismlari",
        "Avtobuslar",
        "Yuk mashinalari",
        "Boshqa transport",
        "Maxsus texnika",
    )
    val subCategories = hashMapOf<String, ArrayList<String>>(
        Pair("Elektronika", listNamesElektronika),
        Pair("Transport", listNamesTransport),
    )
    var selectedCategory: String? = null
    var isPoppingBackFromCategoryFragment = false

    var likedProductsList: ArrayList<MyProduct>? = ArrayList()

    var userList = ArrayList<User>()
}