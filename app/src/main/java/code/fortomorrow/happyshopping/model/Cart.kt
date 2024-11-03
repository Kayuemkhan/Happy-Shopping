package code.fortomorrow.happyshopping.model

class Cart {
    @JvmField
    var pid: String? = null
    @JvmField
    var pname: String? = null
    @JvmField
    var price: String? = null
    @JvmField
    var quantity: String? = null
    var discount: String? = null

    constructor()

    constructor(
        pid: String?,
        pname: String?,
        price: String?,
        quantity: String?,
        discount: String?
    ) {
        this.pid = pid
        this.pname = pname
        this.price = price
        this.quantity = quantity
        this.discount = discount
    }
}
