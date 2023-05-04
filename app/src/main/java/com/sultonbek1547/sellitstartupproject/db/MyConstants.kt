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
    private val listNamesBolalarDunyosi = arrayListOf(
        "Bolalar kiyimi",
        "Bolalar oyoq kiyimi",
        "Bolalar kolyaskalari",
        "Bolalar mebeli",
        "O'yinchoqlaar",
        "Oziqlantirish",
    )
    private val listNamesMolMulk = arrayListOf(
        "Sutkalik ijara",
        "Kvartiralar",
        "Xususiy uylar",
        "Yer uchastkasi",
        "Garajlar",
        "Tijorat binolari",
    )
 private val listNamesUyVaBog = arrayListOf(
        "Mebel",
        "Bog'-tomorqa",
        "Interyer jihozlar",
        "Xona o'simliklari",
    )
    private val listNamesHayvonlar = arrayListOf(
        "Itlar",
        "Mushuklar",
        "Akvarium baliqlar",
        "Qushlar",
        "Kemiruvchilar",
    )
    private val listNamesXizmatlar = arrayListOf(
        "Qurilish-tamirlash",
        "Moliya Xizmatlari",
        "Tashishlar",
        "Reklama, marketing",
        "Enagalar",
        "Avto-xizmat",
        "Biznesni sotish",
        "Yuridik xizmatlar",
    )
    private val listNamesFitnessHobby = arrayListOf(
        "Antikvar kolleksiyalar",
        "Musiqa anjomlari",
        "Sport dam olish",
        "Kitoblar-jurnallar",
        "Chiptalar",
    )
    val mainCategories = arrayListOf<String>(
        "Elektronika",
        "Transport",
        "Bolalar uchun",
        "Uy va bog'",
        "Fitnes&hobbi",
        "Xizmatlar",
        "Hayvonlar",
        "Ko'chmas mulk",
    )

    val subCategories = hashMapOf<String, ArrayList<String>>(
        Pair("Elektronika", listNamesElektronika),
        Pair("Transport", listNamesTransport),
        Pair("Bolalar uchun", listNamesBolalarDunyosi),
        Pair("Fitnes&hobbi", listNamesFitnessHobby),
        Pair("Xizmatlar", listNamesXizmatlar),
        Pair("Hayvonlar", listNamesHayvonlar),
        Pair("Uy va bog'", listNamesUyVaBog),
        Pair("Ko'chmas mulk", listNamesMolMulk),
    )
    var selectedCategory: String? = null
    var isPoppingBackFromCategoryFragment = false

    var likedProductsList: ArrayList<MyProduct>? = ArrayList()

    var userList = ArrayList<User>()
}