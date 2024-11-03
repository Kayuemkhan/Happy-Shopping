package code.fortomorrow.happyshopping.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Orders {
    @SerializedName("category")
    @Expose
    var category: String? = null

    @SerializedName("date")
    @Expose
    var date: String? = null

    @SerializedName("description")
    @Expose
    var description: String? = null

    @SerializedName("image")
    @Expose
    var image: String? = null

    @SerializedName("pid")
    @Expose
    var pid: String? = null

    @JvmField
    @SerializedName("pname")
    @Expose
    var pname: String? = null

    @JvmField
    @SerializedName("price")
    @Expose
    var price: String? = null

    @SerializedName("time")
    @Expose
    var time: String? = null

    fun toMap(): Map<String, Any?> {
        val result = HashMap<String, Any?>()
        result["author"] = pname
        result["body"] = price
        result["image"] = image
        return result
    }
}
