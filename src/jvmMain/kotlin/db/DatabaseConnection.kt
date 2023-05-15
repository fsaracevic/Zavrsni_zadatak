package db

import dao.users
import model.User
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.entity.add
import org.ktorm.entity.find

class DatabaseConnection {

    private val connectionString = "jdbc:mysql://localhost:3306/weather"
    private val user = "root"
    private val pass = "Transcom1!"
    private var connection: Database = Database.connect(
        url = connectionString,
        user = user,
        password = pass
    )

    fun getUserByUsername(userName: String): User? {
        return connection.users.find {
            it.username eq userName
        }
    }

    fun getUserById(userId: Int): User?{
        return connection.users.find {
            it.id eq userId
        }
    }

    fun createNewUser(username: String, password: String, location: String): Boolean {
        return if(getUserByUsername(username) == null){
            val newUser = User {
                this.username = username
                this.password = password
                this.location = location
            }
            connection.users.add(newUser)
            true
        }else{
            false
        }
    }
}