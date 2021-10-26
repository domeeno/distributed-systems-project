package com.pandora.identityservice.models

import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name="users")
class User {

    @Id
    @Column(name="user_id")
    var userId : UUID = UUID.randomUUID();

    var username : String = "";

    var email : String = "";

    var password : String = "";


}