package model

import org.ktorm.entity.Entity


interface User: Entity<User> {
    companion object: Entity.Factory<User>()
    val id: Int
    var username: String
    var password: String
    var location: String
}