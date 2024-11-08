package code.fortomorrow.happyshopping.view

class Products {
    @JvmField
    var pname: String? = null
    var description: String? = null
    @JvmField
    var price: String? = null
    @JvmField
    var image: String? = null
    var category: String? = null
    @JvmField
    var pid: String? = null
    var date: String? = null
    var time: String? = null

    constructor()

    constructor(
        pname: String?,
        description: String?,
        price: String?,
        image: String?,
        category: String?,
        pid: String?,
        date: String?,
        time: String?
    ) {
        this.pname = pname
        this.description = description
        this.price = price
        this.image = image
        this.category = category
        this.pid = pid
        this.date = date
        this.time = time
    }
}
