package com.furkansoyleyici.tipapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.furkansoyleyici.tipapplication.ui.components.MainPage
import com.furkansoyleyici.tipapplication.ui.theme.TipApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApp {
                MainContent()
            }
           
        }
    }
}

@Composable
fun MyApp(modifier: Modifier = Modifier, mainContent : @Composable () -> Unit)  {
    TipApplicationTheme {
        Scaffold (
            modifier = modifier.fillMaxSize()
        ){ innerPadding ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)

            ){
                mainContent()

            }
        }
    }
}

@Preview
@Composable
private fun MainContent() {
    MainPage()
    
}

