package com.acutepro.treadapp.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.acutepro.treadapp.R
import com.acutepro.treadapp.navigation.Routes
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun Splash(navHostController: NavHostController) {

      ConstraintLayout(modifier = Modifier.fillMaxSize()) {
          val image = createRef()
          Image(painter = painterResource(id = R.drawable.logo), contentDescription = null,
          modifier = Modifier.constrainAs(image){
              top.linkTo(parent.top)
              bottom.linkTo(parent.bottom)
              end.linkTo(parent.end)
              start.linkTo(parent.start)
          })
      }

    LaunchedEffect(true){
        delay(1000)
        if (FirebaseAuth.getInstance().currentUser!= null) {
            navHostController.navigate(Routes.BottomNav.routes){
                popUpTo(navHostController.graph.startDestinationId)
                launchSingleTop = true
            }
        } else {
            navHostController.navigate(Routes.Login.routes){
                popUpTo(navHostController.graph.startDestinationId)
                launchSingleTop = true
            }
        }
    }
}