package code.fortomorrow.happyshopping.interfaces

import android.view.View

interface ItemClickListener {
    fun onClick(view: View?, position: Int, isLongClick: Boolean)
}