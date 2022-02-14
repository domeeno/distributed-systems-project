package com.pandora.subscription.models

import java.sql.Timestamp
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name="pack")
class PackModel {
    @Id
    @Column(name="id")
    var id : UUID = UUID.randomUUID()

    var name : String = ""

    @Column(name="memory_amount")
    var memoryAmount : Int = 0;

    @Column(name="member_amount")
    var memberAmount : Int = 0;

    @Column(name="live_edit_amount")
    var liveEditAmount : Int = 0;

    var price : Float = 0.0F;

}