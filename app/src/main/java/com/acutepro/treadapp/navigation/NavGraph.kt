package com.acutepro.treadapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.acutepro.treadapp.screen.AddThreads
import com.acutepro.treadapp.screen.BottomNav
import com.acutepro.treadapp.screen.Home
import com.acutepro.treadapp.screen.Login
import com.acutepro.treadapp.screen.Notification
import com.acutepro.treadapp.screen.OtherUser
import com.acutepro.treadapp.screen.Profile
import com.acutepro.treadapp.screen.Register
import com.acutepro.treadapp.screen.Search
import com.acutepro.treadapp.screen.Splash

@Composable
fun NavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Routes.Splash.routes
    ) {
        composable(Routes.Splash.routes){
            Splash(navController)
        }


        composable(Routes.Home.routes){
            Home(navController)
        }

        composable(Routes.Notification.routes){
            Notification()
        }

        composable(Routes.Search.routes){
            Search(navController)
        }

        composable(Routes.AddThreads.routes){
            AddThreads(navController)
        }

        composable(Routes.Profile.routes){
            Profile(navController)
        }

        composable(Routes.BottomNav.routes){
            BottomNav(navController)
        }

        composable(Routes.Login.routes){
            Login(navController)
        }

        composable(Routes.Register.routes){
            Register(navController)
        }
        composable(Routes.OtherUser.routes){
            val data = it.arguments!!.getString("data")
            OtherUser(navController,data!!)
        }

    }

}