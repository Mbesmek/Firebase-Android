package com.example.firebase101

class User{
    var name:String?=null
    var phone:String?=null
    var profileImage:String?=null
    var userId:String?=null

    constructor()
    constructor(name: String?, phone: String?, profileImage: String?, userId: String?) {
        this.name = name
        this.phone = phone
        this.profileImage = profileImage
        this.userId = userId
    }


}