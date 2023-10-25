package com.acutepro.treadapp.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.acutepro.treadapp.utils.SharedPref
import com.acutepro.treadapp.viewModels.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun OtherUser(navHostController: NavHostController, uid: String) {

    val userViewModel: UserViewModel = viewModel()
    val threadList by userViewModel.threads.observeAsState(null)
    val user by userViewModel.users.observeAsState(null)
    val followerList by userViewModel.followerList.observeAsState(null)
    val followingList by userViewModel.followingList.observeAsState(null)

    var currentUserId = ""
    if (FirebaseAuth.getInstance().currentUser != null) currentUserId =
        FirebaseAuth.getInstance().currentUser!!.uid


    userViewModel.fetchThreads(uid)
    userViewModel.fetchUser(uid)
    userViewModel.getFollowing(uid)
    userViewModel.getFollower(uid)


    val context = LocalContext.current


    /** For Profile content ui */
    if (threadList != null && user != null) {
        Column {


            Box(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            ) {
                /** For User name and name */
                Column(modifier = Modifier.padding(start = 10.dp, end = 80.dp)) {
                    Text(
                        text = user!!.name,
                        style = TextStyle(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp
                        ),
                        modifier = Modifier.padding(top = 10.dp)
                    )

                    Text(
                        text = user!!.userName, style =
                        TextStyle(
                            fontWeight = FontWeight.Medium,
                            fontSize = 15.sp,
                            color = Color.Black
                        )
                    )

                    Text(
                        text = user!!.bio, style =
                        TextStyle(
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            color = Color.Gray
                        ), modifier = Modifier.padding(top = 8.dp)
                    )
                }

                /** For User profile Picture */
                Image(
                    painter = rememberAsyncImagePainter(model = user!!.imageUrl),
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

            ElevatedButton(onClick = {
                if (currentUserId != "" && followerList != null && !followerList!!.contains(currentUserId)) userViewModel.followUsers(
                    userId = uid,
                    currentUserId = currentUserId
                )else if (followerList!!.contains(currentUserId)){
                    userViewModel.unFollow(userId = uid, currentUserId = currentUserId)
                }

            }, modifier = Modifier.padding(start = 20.dp, top = 10.dp)) {
                Text(
                    text = if (followerList != null && followerList!!.isNotEmpty() && followerList!!.contains(currentUserId)) "Following" else "Follow", style =
                    TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                )

            }
            Box(modifier = Modifier.heightIn(5.dp))

            LazyColumn {

                items(threadList ?: emptyList()) { thread ->
                    ThreadItem(
                        threads = thread,
                        users = user!!,
                        navHostController = navHostController,
                        userId = SharedPref.getUserName(context)
                    )

                }

            }


        }
    }


}