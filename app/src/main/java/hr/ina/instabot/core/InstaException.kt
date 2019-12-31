package hr.ina.instabot.core

class InstaException(message: String, val type : Type) : Exception(message) {

    lateinit var response: InstaResponse

    enum class Type {
        REQUEST, MISSING_VALUE, ENTRY_EXISTS, FAKE_USER
    }

    constructor(message: String, type : Type, response: InstaResponse) : this(message, type) {
        this.response = response
    }

}