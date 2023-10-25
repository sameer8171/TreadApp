package com.acutepro.treadapp.screen

import android.provider.ContactsContract.Profile
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.acutepro.treadapp.item_view.ThreadItem
import com.acutepro.treadapp.model.UserModel
import com.acutepro.treadapp.navigation.Routes
import com.acutepro.treadapp.utils.SharedPref
import com.acutepro.treadapp.viewModels.AuthViewModel
import com.acutepro.treadapp.viewModels.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Profile(navHostController: NavHostController) {
    val userViewModel:UserViewModel = viewModel()
    val threadList by userViewModel.threads.observeAsState(null)
    val followerList by userViewModel.followerList.observeAsState(null)
    val followingList by userViewModel.followingList.observeAsState(null)

    userViewModel.fetchThreads(FirebaseAuth.getInstance().currentUser?.uid ?: "")

    val authViewModel: AuthViewModel = viewModel()
    val firebaseUser by authViewModel.firebaseUser.observeAsState(null)


    val context = LocalContext.current
    LaunchedEffect(firebaseUser) {
        if (firebaseUser == null) {
            navHostController.navigate(Routes.Login.routes) {
                popUpTo(navHostController.graph.startDestinationId)
                launchSingleTop = true
            }
        }
    }

    var currentUserId = ""
    if (FirebaseAuth.getInstance().currentUser != null) currentUserId =
        FirebaseAuth.getInstance().currentUser!!.uid

    if (currentUserId != ""){
        userViewModel.getFollowing(currentUserId)
        userViewModel.getFollower(currentUserId)
    }



    val user = UserModel(
        name = SharedPref.getName(context),
        imageUrl = SharedPref.getImageUrl(context),
        userName = SharedPref.getUserName(context)
    )



    /** For Profile content ui */
    Column {


        Box(modifier = Modifier
            .padding(top = 10.dp)
            .fillMaxWidth()
            .padding(horizontal = 10.dp)) {
            /** For User name and name */
            Column(modifier = Modifier.padding(start = 10.dp, end = 80.dp)) {
                Text(
                    text = SharedPref.getName(context),
                    style = TextStyle(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp
                    ),
                    modifier = Modifier.padding(top = 10.dp)
                )

                Text(
                    text = SharedPref.getUserName(context), style =
                    TextStyle(
                        fontWeight = FontWeight.Medium,
                        fontSize = 15.sp,
                        color = Color.Black
                    )
                )

                Text(
                    text = SharedPref.getBio(context), style =
                    TextStyle(
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        color = Color.Gray
                    ), modifier = Modifier.padding(top = 8.dp)
                )
            }

            /** For User profile Picture */
            Image(
                painter = rememberAsyncImagePainter(model = SharedPref.getImageUrl(context)),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 18.dp)
                    .align(Alignment.TopEnd)
                    .clip(CircleShape)
                    .size(90.dp)
                    .background(Color.LightGray),
                contentScale = ContentScale.Crop
            )
        }

        Text(
            text = "${followerList!!.size} Followers |", style =
            TextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                color = Color.Gray
            ), modifier = Modifier.padding(start = 20.dp, top = 5.dp)
        )
        Text(
            text = "${followingList!!.size} Following |", style =
            TextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                color = Color.Gray
            ), modifier = Modifier.padding(start = 20.dp, top = 5.dp)
        )

        ElevatedButton(onClick = { authViewModel.logOut()}, modifier = Modifier.padding(start = 20.dp, top = 10.dp)) {
            Text(
                text = "Logout", style =
                TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            )

        }
        Box(modifier = Modifier.heightIn(5.dp))

        LazyColumn{

            items(threadList ?: emptyList()){thread ->
                ThreadItem(threads = thread, users = user, navHostController = navHostController , userId = SharedPref.getUserName(context) )

            }

        }


    }

}