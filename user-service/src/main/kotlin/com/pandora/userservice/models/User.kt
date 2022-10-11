package com.pandora.userservice.models

import java.sql.Timestamp
import java.util.Date
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "users")
class User {

    @Id
    @Column(name = "user_id")
    var userId: UUID = UUID.randomUUID()

    var email: String = ""

    var password: String = ""

    var firstname: String = ""

    var lastname: String = ""

    @Column(name = "liked_id")
    var likedId: UUID = UUID.randomUUID()

    @Column(name = "saved_id")
    var savedId: UUID = UUID.randomUUID()

    @Column(name = "date_of_birth")
    var dateOfBirth: Date = Date(System.currentTimeMillis())

    @Column(name = "create_timestamp")
    var createTimestamp: Timestamp = Timestamp(System.currentTimeMillis())

    @Column(name = "update_timestamp")
    var updateTimestamp: Timestamp = Timestamp(System.currentTimeMillis())
}
