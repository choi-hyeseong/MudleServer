package com.comet.mudleserver.music.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.util.UUID

@Entity
class User(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long?,
        var name: String?,
        var uuid: UUID?,
        var coin: Int?
)
{
    constructor() : this(null, null, null, null)
}
