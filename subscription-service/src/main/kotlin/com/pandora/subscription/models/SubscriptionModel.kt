package com.pandora.subscription.models

import com.vladmihalcea.hibernate.type.array.ListArrayType
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import java.sql.Date
import java.sql.Timestamp
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import kotlin.collections.ArrayList


@Entity
@Table(name="subscriptions")
@TypeDef(
    name = "list-array",
    typeClass = ListArrayType::class
    )
class SubscriptionModel {
    @Id
    @Column(name="id")
    var id : UUID = UUID.randomUUID()

    @Column(name="user_id")
    var userId : UUID = UUID.randomUUID()

    @Column(name="pack_id")
    var packId : UUID = UUID.randomUUID()

    @Type(type = "list-array")
    @Column(
        name = "members",
        columnDefinition = "uuid[]"
    )
    var members : ArrayList<UUID> = arrayListOf();

    var months : Int = 0;

    @Column(name="purchase_date")
    var purchaseDate: Timestamp = Timestamp(System.currentTimeMillis())

    @Column(name="exp_date")
    var expDate: Timestamp = Timestamp(System.currentTimeMillis())

}