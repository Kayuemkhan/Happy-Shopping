package code.fortomorrow.happyshopping.prevalent

import code.fortomorrow.happyshopping.model.Users

object Prevalent {
    @JvmField
    var currentOnlineUser: Users? = null

    const val UserPhoneKey: String = "UserPhone"
    const val UserPasswordKey: String = "UserPassword"
}