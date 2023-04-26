package com.sultonbek1547.sellitstartupproject.db

object MyConstants {
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

}