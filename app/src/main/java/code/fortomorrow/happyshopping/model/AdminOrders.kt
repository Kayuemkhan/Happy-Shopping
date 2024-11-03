package code.fortomorrow.happyshopping.model

class AdminOrders {
    var name: String? = null
    var phone: String? = null
    var address: String? = null
    var city: String? = null
    var state: String? = null
    var date: String? = null
    var time: String? = null
    var totalAmount: String? = null

    constructor()

    constructor(
        name: String?,
        phone: String?,
        address: String?,
        city: String?,
        state: String?,
        date: String?,
        time: String?,
        totalAmount: String?
    ) {
        this.name = name
        this.phone = phone
        this.address = address
        this.city = city
        this.state = state
        this.date = date
        this.time = time
        this.totalAmount = totalAmount
    }
}
