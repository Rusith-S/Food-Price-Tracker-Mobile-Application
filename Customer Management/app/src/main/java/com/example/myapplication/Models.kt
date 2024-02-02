package com.example.myapplication

class ShoppingList(val id:        String = "",
                   var name:      String = "",
                   var items:     List<ShoppingListItem>? = listOf(),
                   var timestamp: Long = System.currentTimeMillis() ) {

    override fun toString(): String {
        return name
    }
}

class ShoppingListItem(val id:        String = "",
                       var product:   String = "",
                       var quantity:  Int = 0,
                       var timestamp: Long = System.currentTimeMillis() ) {

    override fun toString(): String {
        return "$product - $quantity"
    }
}
