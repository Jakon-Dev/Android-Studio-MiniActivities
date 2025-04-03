package com.example.intentsimplicitscompose

import android.Manifest
import android.app.SearchManager
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.intentsimplicitscompose.ui.theme.IntentsImplicitsComposeTheme
import android.provider.Settings

class MainActivity : ComponentActivity() {

    private var callPhonePerm = mutableStateOf(false)
    private var cameraPerm = mutableStateOf(false)
    private val permissionCallPhone = Manifest.permission.CALL_PHONE
    private val permissionCamera = Manifest.permission.CAMERA

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IntentsImplicitsComposeTheme {
                MyApp(modifier = Modifier)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        callPhonePerm.value = checkSelfPermission(permissionCallPhone) == PackageManager.PERMISSION_GRANTED
        cameraPerm.value = checkSelfPermission(permissionCamera) == PackageManager.PERMISSION_GRANTED
    }

    @Composable
    fun MyApp(modifier: Modifier = Modifier) {
        val context = LocalContext.current
        var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
        val launchCameraPerm = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            cameraPerm.value = granted
            if (!granted) {
                Toast.makeText(context, "Permiso denegado. Concede el permiso manualmente en Ajustes", Toast.LENGTH_LONG).show()
            }
        }

        val pickImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            selectedImageUri = uri
        }

        Surface(modifier = modifier.fillMaxSize()) {
            Column(modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Bienvenidos")

                // Botón de Localización por coordenadas
                ElevatedButton(onClick = {
                    Toast.makeText(context, "Seleccionado Localizacion por coordenadas", Toast.LENGTH_LONG).show()
                    val geo = "geo:$lat,$lon?q=$lat,$lon"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(geo))
                    context.startActivity(intent)
                }) { Text(text = "Abrir Mapa por Coordenadas") }

                // Botón de Localización por dirección
                ElevatedButton(onClick = {
                    Toast.makeText(context, "Seleccionado Localizacion por dirección", Toast.LENGTH_LONG).show()
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=$address"))
                    context.startActivity(intent)
                }) { Text(text = "Abrir Mapa por Dirección") }

                // Botón para abrir sitio web
                ElevatedButton(onClick = {
                    Toast.makeText(context, "Accediendo a la web", Toast.LENGTH_LONG).show()
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(webEPS))
                    context.startActivity(intent)
                }) { Text(text = "Abrir Sitio Web") }

                // Botón para realizar búsqueda en Google
                ElevatedButton(onClick = {
                    Toast.makeText(context, "Buscando en Google", Toast.LENGTH_LONG).show()
                    val intent = Intent(Intent.ACTION_WEB_SEARCH)
                    intent.putExtra(SearchManager.QUERY, textoABuscar)
                    context.startActivity(intent)
                }) { Text(text = "Buscar en Google") }

                // Botón para realizar llamada telefónica
                ElevatedButton(
                    enabled = callPhonePerm.value,
                    onClick = {
                        Toast.makeText(context, "Marcando Tlfn. Consergeria", Toast.LENGTH_LONG).show()
                        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + telef))
                        context.startActivity(intent)
                    }
                ) { Text(text = "Llamar a Conserjería") }

                // SMS Button
                ElevatedButton(onClick = {
                    val smsIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:$smsNumber"))
                    smsIntent.putExtra("sms_body", smsMessage)
                    context.startActivity(smsIntent)
                }) { Text("Enviar SMS") }

                // Contacts Button
                ElevatedButton(onClick = {
                    val contactIntent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
                    context.startActivity(contactIntent)
                }) { Text("Abrir Contactos") }

                // Gallery Button
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ElevatedButton(onClick = { pickImageLauncher.launch("image/*") }) { Text("Seleccionar Imagen") }
                    selectedImageUri?.let {
                        Image(painter = rememberImagePainter(it), contentDescription = null, modifier = Modifier.size(100.dp))
                    }
                }

                // Botón para solicitar permiso de cámara
                ElevatedButton(onClick = {
                    when {
                        context.checkSelfPermission(permissionCamera) == PackageManager.PERMISSION_GRANTED -> {
                            Toast.makeText(context, "Permiso ya concedido", Toast.LENGTH_SHORT).show()
                        }
                        shouldShowRequestPermissionRationale(permissionCamera) -> {
                            Toast.makeText(context, "Debes conceder el permiso desde Ajustes", Toast.LENGTH_LONG).show()
                        }
                        else -> {
                            launchCameraPerm.launch(permissionCamera)
                        }
                    }
                }) { Text("Solicitar Permiso de Cámara") }

                // Mensaje de advertencia si el permiso no está concedido
                if (!cameraPerm.value) {
                    Text(text = "Para usar la cámara, otorga permisos en Configuración")
                    ElevatedButton(onClick = {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:${context.packageName}"))
                        context.startActivity(intent)
                    }) { Text("Abrir Configuración") }
                }

                // Botón para abrir la cámara (deshabilitado si no hay permisos)
                ElevatedButton(onClick = {
                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    context.startActivity(cameraIntent)
                }, enabled = cameraPerm.value) { Text("Abrir Cámara") }
            }
        }
    }

    companion object {
        const val lat = "41.60788"
        const val lon = "0.623333"
        const val address = "Carrer de Jaume II, 69, Lleida"
        const val webEPS = "http://www.eps.udl.cat/"
        const val textoABuscar = "escola politecnica superior UdL"
        const val telef = "666666666"
        const val smsNumber = "123456789"
        const val smsMessage = "Hola, este es un mensaje de prueba."
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewApp() {
        IntentsImplicitsComposeTheme { MyApp() }
    }
}