package com.sultonbek1547.sellitstartupproject.models

import java.io.Serializable

class User: Serializable {
    var uid:String?=null
    var name:String?=null
    var imageLink:String?=null
    var email:String?=null
    var password:String?=null
    var statusTime:String?=null
    var isOnline:String?=null
    var token:String?=null
    var likedProductIds:String?=null

    constructor(
        uid: String?,
        name: String?,
        imageLink: String?,
        email: String?,
        password: String?,
        statusTime: String?,
        isOnline: String?,
        token: String?,
        likedProductIds: String?,
    ) {
        this.uid = uid
        this.name = name
        this.imageLink = imageLink
        this.email = email
        this.password = password
        this.statusTime = statusTime
        this.isOnline = isOnline
        this.token = token
        this.likedProductIds = likedProductIds
    }

    constructor()


}