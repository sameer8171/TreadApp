package com.acutepro.treadapp.item_view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.acutepro.treadapp.R
import com.acutepro.treadapp.model.AddThreadsModel
import com.acutepro.treadapp.model.UserModel
import com.acutepro.treadapp.utils.SharedPref

@Composable
fun ThreadItem(
    threads:AddThreadsModel,
    users:UserModel,
    navHostController: NavHostController,
    userId:String
) {



    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, top = 10.dp)) {
            Image(painter = rememberAsyncImagePainter(model = users.imageUrl) , contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(40.dp)
                    .background(Color.LightGray),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(horizontal = 10.dp)) {
                Text(text = users.userName, style =
                TextStyle(
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp,
                    color = Color.Black)
                )
                Text(text = threads.thread, style =
                TextStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = 15.sp,
                    color = Color.Gray)
                )

            }
        }

        if (threads.image != "") {
            Card(
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 5.dp
                ),
                modifier = Modifier.padding(12.dp)
            ) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .size(250.dp)){
                    Image(painter = rememberAsyncImagePainter(model = threads.image), contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize())
                }
            }
        }

        Divider(color = Color.LightGray, thickness = 1.dp, modifier = Modifier.padding( 5.dp))

    }

}
