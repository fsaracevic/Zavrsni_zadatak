package dao

import dao.Users.bindTo
import model.User
import org.ktorm.database.Database
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

val Database.users get() = this.sequenceOf(Users)

object Users: Table<User>("users") {
    val id = int("id").primaryKey().bindTo { it.id }
    val username = varchar("username").bindTo { it.username }
    val password = varchar("password").bindTo { it.password }
    val location = varchar("location").bindTo { it.location }
}