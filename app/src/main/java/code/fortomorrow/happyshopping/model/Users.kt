package code.fortomorrow.happyshopping.model

class Users {
    @JvmField
    var name: String? = null
    @JvmField
    var phone: String? = null
    @JvmField
    var password: String? = null
    @JvmField
    var image: String? = null
    var address: String? = null

    constructor()

    constructor(
        name: String?,
        phone: String?,
        password: String?,
        image: String?,
        address: String?
    ) {
        this.name = name
        this.phone = phone
        this.password = password
        this.image = image
        this.address = address
    }
}