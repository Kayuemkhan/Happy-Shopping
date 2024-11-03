package code.fortomorrow.happyshopping.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AdminViewOrders {
    @SerializedName("date")
    @Expose
    var date: String? = null

    @SerializedName("discount")
    @Expose
    var discount: String? = null

    @SerializedName("pid")
    @Expose
    var pid: String? = null

    @SerializedName("pname")
    @Expose
    var pname: String? = null

    @SerializedName("price")
    @Expose
    var price: String? = null

    @SerializedName("quantity")
    @Expose
    var quantity: String? = null

    @SerializedName("time")
    @Expose
    var time: String? = null
}
