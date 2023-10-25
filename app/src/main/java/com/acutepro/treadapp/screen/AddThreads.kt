package com.acutepro.treadapp.screen

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.acutepro.common.showToast
import com.acutepro.treadapp.R
import com.acutepro.treadapp.navigation.Routes
import com.acutepro.treadapp.utils.SharedPref
import com.acutepro.treadapp.viewModels.AddThreadViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AddThreads(navHostController: NavHostController) {
    val context = LocalContext.current

    val threadViewModel:AddThreadViewModel = viewModel()
    val isPosted by threadViewModel.isPosted.observeAsState(false)

    var thread by remember {
        mutableStateOf("")
    }

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
        }

    val permissionToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else Manifest.permission.READ_EXTERNAL_STORAGE

    val permissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {

            } else {

            }
        }

    LaunchedEffect(isPosted){
        if (isPosted!!){
            thread = ""
            imageUri = null
            context.showToast("Thread added")
            navHostController.navigate(Routes.Home.routes){
                popUpTo(navHostController.graph.startDestinationId){
                    inclusive = true
                }
            }
        }
    }


    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.baseline_close_24),
                contentDescription = null
            )
            Text(
                text = "New tread", modifier = Modifier.padding(start = 10.dp),
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            )

        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp)) {
            Image(painter = rememberAsyncImagePainter(model =SharedPref.getImageUrl(context)) , contentDescription = null,
            modifier = Modifier
                .clip(CircleShape)
                .size(40.dp)
                .background(Color.LightGray),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(horizontal = 10.dp)) {
                Text(text = SharedPref.getUserName(context), style =
                TextStyle(
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp,
                    color = Color.Black))

                BasicTextFieldWithHint(hint = "Start a thread ...", modifier =Modifier.padding(

                    top = 10.dp,
                    bottom = 5.dp
                ) , value =thread , onValueChange = {thread = it})


            }
        }

        if (imageUri == null) {
            Image(
                painter = painterResource(id = R.drawable.attach),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 60.dp, top = 5.dp)
                    .clickable {
                        imageSelect(context, permissionToRequest, permissionLauncher, launcher)
                    }
            )
        } else {
            Card(
                elevation =CardDefaults.cardElevation(
                    defaultElevation = 5.dp
                ),
                modifier = Modifier.padding(12.dp)
            ) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .size(250.dp)){
                    Image(painter = rememberAsyncImagePainter(model = imageUri) , contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize())
                    Icon(imageVector = Icons.Default.Close, contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .clickable {
                                imageUri = null
                            })
                }
            }



        }
        Box(modifier =Modifier.fillMaxSize()) {
            Text(text = "Anyone can reply",
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 10.dp, bottom = 10.dp)
            )

            TextButton(onClick = {

                                 if (imageUri == null){
                                     if (thread.isNotEmpty()){
                                         threadViewModel.saveData(thread,FirebaseAuth.getInstance().currentUser!!.uid,"")
                                     }else{
                                         context.showToast("Please enter threads")
                                     }
                                 }else{
                                     threadViewModel.saveImage(thread,FirebaseAuth.getInstance().currentUser!!.uid,imageUri!!)
                                 }

            }, modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 15.dp, end = 10.dp)) {
                Text(text = "Post",
                    style = TextStyle(
                        fontSize = 20.sp
                    ),
                    modifier = Modifier
                )
            }


            
        }




    }
}

fun imageSelect(
    context: Context,
    permissionToRequest: String,
    permissionLauncher: ManagedActivityResultLauncher<String, Boolean>,
    launcher: ManagedActivityResultLauncher<String, Uri?>
) {
    val isGranted = ContextCompat.checkSelfPermission(
        context, permissionToRequest
    ) == PackageManager.PERMISSION_GRANTED

    if (isGranted) {
        launcher.launch("image/*")
    } else {
        permissionLauncher.launch(permissionToRequest)
    }
}




@Composable
fun BasicTextFieldWithHint(
    hint:String,modifier: Modifier,value:String,
    onValueChange:(String) ->Unit
) {

    Box(modifier = modifier) {
        if (value.isEmpty()){
            Text(text = hint, color = Color.Gray)
        }
        BasicTextField(value = value, onValueChange = onValueChange,
        textStyle = TextStyle.Default.copy(Color.Black),
        modifier = Modifier.fillMaxWidth())
        
    }
    
}