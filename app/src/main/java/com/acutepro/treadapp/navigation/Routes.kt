package com.acutepro.treadapp.navigation

sealed class Routes(val routes:String){
    object Home:Routes("home")
    object Notification:Routes("notification")
    object Profile:Routes("profile")
    object Search:Routes("search")
    object Splash:Routes("splash")
    object AddThreads:Routes("add_threads")
    object BottomNav:Routes("bottom_nav")
    object Login:Routes("login")
    object Register:Routes("register")
    object OtherUser:Routes("other_user/{data}")
}
