package com.mujapps.foodieqrscanner

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.mujapps.foodieqrscanner.ui.theme.FoodieQrScannerTheme
import com.mujapps.foodieqrscanner.view_models.MainViewModel
import com.mujapps.foodieqrscanner.views.PreviewViewComposable

class MainActivity : ComponentActivity() {

    private val mMainViewModel: MainViewModel by viewModels()

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FoodieQrScannerTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
                        val mPermissionGivenState = rememberSaveable {
                            mutableStateOf(false)
                        }

                        val requestPermissionLauncher = rememberLauncherForActivityResult(
                            ActivityResultContracts.RequestPermission()
                        ) { isGranted ->
                            if (isGranted) {
                                // Permission granted
                                mPermissionGivenState.value = true
                            } else {
                                // Handle permission denial
                            }
                        }

                        LaunchedEffect(cameraPermissionState) {
                            if (!cameraPermissionState.status.isGranted && cameraPermissionState.status.shouldShowRationale) {
                                // Show rationale if needed
                            } else {
                                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        }

                        if (mPermissionGivenState.value) {
                            PreviewViewComposable { barcodeList ->
                                mMainViewModel.readBarCodes(barcodeList)
                            }
                            Text(
                                text = "Scan QR Code",
                                modifier = Modifier.padding(top = 48.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FoodieQrScannerTheme {

    }
}