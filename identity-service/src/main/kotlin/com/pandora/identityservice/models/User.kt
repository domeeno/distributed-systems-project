package com.pandora.identityservice.models

import javax.persistence.Column
import javax.persistence.Entity

@Entity
class User {

    @Column(name="user_id")
    var userId : String = "";

    var username : String = "";

    var email : String = "";

    var password : String = "";


}