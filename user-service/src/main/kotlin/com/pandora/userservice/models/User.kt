package com.pandora.userservice.models

import java.sql.Timestamp
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
    var userId : UUID = UUID.randomUUID()

    var email : String = ""

    var password : String = ""

    var firstName : String = ""

    var lastName : String = ""

    @Column(name="date_of_birth")
    var dateOfBirth : Date = Date(System.currentTimeMillis())

    @Column(name="create_timestamp")
    var createTimestamp: Timestamp = Timestamp(System.currentTimeMillis())

    @Column(name="update_timestamp")
    var updateTimestamp: Timestamp = Timestamp(System.currentTimeMillis())
}