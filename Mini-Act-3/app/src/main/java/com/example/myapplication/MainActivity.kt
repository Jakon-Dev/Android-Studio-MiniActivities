package com.example.myapplication

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                AppContent()
            }
        }
    }
}

@Composable
fun AppContent() {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LanguageDisplay()
        Spacer(modifier = Modifier.height(16.dp))
        GreetingText()
        Spacer(modifier = Modifier.height(16.dp))
        ValueCalculator()
        Spacer(modifier = Modifier.height(16.dp))
        FlagImage()
    }
}

@Composable
fun LanguageDisplay() {
    val locale = Locale.getDefault().displayLanguage
    Text(text = stringResource(id = R.string.language_name) + ": $locale", style = MaterialTheme.typography.titleLarge)
}

@Composable
fun GreetingText() {
    Text(text = stringResource(id = R.string.hello_world), style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
}

@Composable
fun ValueCalculator() {
    var initialValue = if (Locale.getDefault().language == "es") 2000000 else if (Locale.getDefault().language == "ca") 4000000 else if (Locale.getDefault().language == "ru") 7000000 else 1000000
    var value by rememberSaveable { mutableIntStateOf(initialValue) }
    val increaseFactor = if (Locale.getDefault().language == "es") 1.2 else if (Locale.getDefault().language == "ca") 1.4 else if (Locale.getDefault().language == "ru") 1.8 else 1.1


    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "PIB 1: $initialValue", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { value = (value * increaseFactor).toInt() }) {
            Text(text = stringResource(id = R.string.Action1))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "PIB 2: $value", style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun FlagImage() {
    Image(painter = painterResource(id = R.drawable.flag), contentDescription = "Flag", modifier = Modifier.size(150.dp))
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun LightPreview() {
    MyApplicationTheme {
        AppContent()
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun ValueCalculatorPreview() {
    MyApplicationTheme {
        ValueCalculator()
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun FlagPreview() {
    MyApplicationTheme {
        FlagImage()
    }
}