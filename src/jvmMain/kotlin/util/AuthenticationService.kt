package util

import db.DatabaseConnection
import model.User

class AuthenticationService(private val db: DatabaseConnection) {

    private var authenticatedUserId = -1

    fun authenticate(userName: String, password: String): Boolean {
        val user = db.getUserByUsername(userName) ?: return false
        if(!checkUsername(user, userName)) return false
        if(!checkPassword(user, password)) return false
        authenticatedUserId = user.id
        return true
    }

    private fun checkUsername(user: User, username: String): Boolean {
        return user.username == username
    }

    private fun checkPassword(user: User, password: String): Boolean {
        return user.password == password
    }

    fun getAuthenticatedUserId(): Int {
        return authenticatedUserId
    }

    fun logout() {
        authenticatedUserId = -1
    }
}