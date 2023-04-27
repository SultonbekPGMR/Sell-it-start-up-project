package com.sultonbek1547.sellitstartupproject.models

data class MyProduct(
    val productId:String = "",
    val listOfProductImageLinks:List<String> = emptyList(),
    val productName:String = "",
    val productParentCategory:String = "",
    val productChildCategory:String = "",
    val productDescription:String = "",
    val productStatus:String = "",
    val productOwnerLocation:String = "",
    val productOwnerName:String = "",
    val productOwnerPhoneNumber:String = "",
    val productOwnerEmail:String = "",
    val productPostedDataAndTime:String = "",
    var productPrice:String = ""
) {
    constructor() : this("", emptyList(), "", "", "", "", "", "", "", "", "", "", "")
}
