package com.example.mealideas

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import kotlinx.coroutines.launch
import androidx.room.Database
import androidx.room.Delete
import androidx.room.OnConflictStrategy
import androidx.room.RoomDatabase
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "greeting") {
        composable("greeting") { Greeting(navController) }
        composable("profile") { Profile(navController) }
        composable("recipes") { Recipes(navController) }
        composable("cookingIdeas") {CookingIdeas(navController)}

    }
}



@Composable
fun Greeting(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        //verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Menu_display(navController)
        Spacer(modifier = Modifier.height(45.dp))
        Welcome_display()
        Spacer(modifier = Modifier.height(45.dp))
        MyStats()
        Spacer(modifier = Modifier.height(45.dp))
        Greetings_menu_box(navController)
        Spacer(modifier = Modifier.height(8.dp))
        //MyRowItem(navController ,text = "Quit", buttonText = "")
    }
}

@Composable
fun Greetings_menu_box(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(color = Color(0xFFF2F2F2), shape = RoundedCornerShape(23.dp))
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Menu_Row(navController, "recipes", imageResId = R.drawable.recipes)
        Spacer(modifier = Modifier.height(10.dp))
        Menu_Row(navController, "cookingIdeas", imageResId = R.drawable.cooking_icon1)
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun Menu_Row(navController: NavHostController, text: String, imageResId: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            //.background(color = Color.White, shape = RoundedCornerShape(8.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = null,
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape)
        )
        if(text === "cookingIdeas"){
            Text(text = "WHAT TO COOK", color = Color.Black, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }else {
            Text(
                text = text.uppercase(),
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.width(2.dp))
        Image(
            painter = painterResource(id = R.drawable.arrow_right),
            contentDescription = null,
            modifier = Modifier
                .size(20.dp)
                .clickable { navController.navigate(text) }
        )
        /*Button(
            onClick = { navController.navigate(text) },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
        ) {
            Text(text = text, color = Color.White)
        }*/
    }
}

@Composable
fun Menu_display(navController: NavHostController) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Image(
            painter = painterResource(id = R.drawable.menu_dark),
            contentDescription = null,
            modifier = Modifier.size(40.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.profile_placeholder),
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .clickable { navController.navigate("profile") }
        )
    }
}

@Composable
fun Welcome_display() {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ){
            Text(text = "Welcome", color = Color.Gray, fontSize = 20.sp)
            //User Name Variable
            Text(text = "Daniel Rauch", color = Color.Black, fontSize = 28.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun Profile(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        //verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Top_back(navController)
        Spacer(modifier = Modifier.height(50.dp))
        Image(
            painter = painterResource(id = R.drawable.profile_placeholder),
            contentDescription = null,
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
                .clickable { navController.navigate("profile") }
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Daniel Rauch", color = Color.Black, fontSize = 25.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(20.dp))
        MyStats()
        Spacer(modifier = Modifier.height(20.dp))
        ProfileOptions(navController)
    }
}

@Composable
fun Top_back(navController: NavHostController) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Image(
            painter = painterResource(id = R.drawable.arrow_left),
            contentDescription = null,
            modifier = Modifier
                .size(25.dp)
                .clickable { navController.navigate("greeting") }
        )
    }
}

@Composable
fun MyStats(){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        StatusItem(number = "69", status = "Meals", isActive = true, modifier = Modifier.weight(2f))
        StatusItem(number = "3.5", status = "Difficulty", isActive = false, modifier = Modifier.weight(2f))
        StatusItem(number = "2.5", status = "Taste", isActive = false, modifier = Modifier.weight(2f))
    }
}

@Composable
fun StatusItem(number: String, status: String, isActive: Boolean, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .height(130.dp)
            .padding(8.dp)
            .background(
                color = if (isActive) Color(0xFFA597FC) else Color(0xFFF2F2F2),
                shape = RoundedCornerShape(15.dp)
            )
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = number,
            color = if (isActive) Color.White else Color.Black,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )
        if(!isActive){
            Text(
                text = "/5",
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }else{
            Spacer(modifier = Modifier.height(16.dp))
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = status,
            color = if (isActive) Color.White else Color.Gray,
            fontSize = 13.sp,
        )
    }
}

@Composable
fun ProfileOptions(navController : NavHostController){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(color = Color(0xFFF2F2F2), shape = RoundedCornerShape(23.dp))
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Menu_Row(navController, "username", imageResId = R.drawable.image_placeholder)
        Spacer(modifier = Modifier.height(10.dp))
        Menu_Row(navController, "settings", imageResId = R.drawable.image_placeholder)
        Spacer(modifier = Modifier.height(8.dp))
    }
}

/*
    This section is for the Meals Container

 */

@Entity(tableName = "dishes")
data class Dish(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val imageUri: String?, // Speichere die URI als String
    val difficulty: String,
    val taste: String,
    val instructions: String
)

@Dao
interface DishDao {
    @Query("SELECT * FROM dishes")
    fun getAllDishes(): LiveData<List<Dish>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDish(dish: Dish)

    @Delete
    suspend fun deleteDish(dish: Dish)
}

@Database(entities = [Dish::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dishDao(): DishDao
}

@Composable
fun AddDishForm(onAddDish: (Dish) -> Unit, onDismiss: () -> Unit) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var name by remember { mutableStateOf("") }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }  // Statt URI, verwende Bitmap
    var difficulty by remember { mutableStateOf("0") }
    var taste by remember { mutableStateOf("0") }
    var instructions by remember { mutableStateOf("") }

    // Benutzeroberfläche für das Formular
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White, shape = RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Gerichtname") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = difficulty,
            onValueChange = { value ->
                if (value.toIntOrNull() in 0..5 || value.isEmpty()) {
                    difficulty = value
                }
            },
            label = { Text("Schwierigkeitsgrad (0-5)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = taste,
            onValueChange = { value ->
                if (value.toIntOrNull() in 0..5 || value.isEmpty()) {
                    taste = value
                }
            },
            label = { Text("Geschmack (0-5)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = instructions,
            onValueChange = { instructions = it },
            label = { Text("Anleitung") },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 100.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp)),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Default
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    // Keine spezielle Aktion hier, um Schließen zu verhindern
                }
            ),
            singleLine = false
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Bildauswahl Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            PickImageFromGallery { uri ->
                uri?.let {
                    // Lade das Bitmap von der URI
                    bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                }
            }

            Button(onClick = {
                if (name.isNotEmpty() && difficulty.toIntOrNull() in 0..5 && taste.toIntOrNull() in 0..5) {
                    coroutineScope.launch {
                        bitmap?.let {
                            val imageUri = saveImageToMediaStore(context, it, name)
                            onAddDish(
                                Dish(
                                    name = name,
                                    imageUri = imageUri.toString(),  // Speichere die URI als String
                                    difficulty = difficulty,
                                    taste = taste,
                                    instructions = instructions
                                )
                            )
                        }
                        // Zurücksetzen der Felder nach dem Hinzufügen
                        name = ""
                        bitmap = null
                        difficulty = "0"
                        taste = "0"
                        instructions = ""
                        onDismiss()
                    }
                }
            }) {
                Text("Hinzufügen")
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = onDismiss) {
                Text("Abbrechen")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

fun saveImageToMediaStore(context: Context, bitmap: Bitmap, imageName: String): Uri? {
    // Bereite die Informationen vor, die wir speichern wollen
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, "$imageName.jpg")  // Name der Datei
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")         // Dateityp
        put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/YourAppName")  // Speicherort
    }

    // ContentResolver zum Einfügen der Daten verwenden
    val resolver = context.contentResolver
    val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

    // Öffne den Ausgabestream und speichere das Bild
    uri?.let {
        resolver.openOutputStream(it).use { outputStream ->
            if (outputStream != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            }
        }
    }

    return uri
}

@Composable
fun PickImageFromGallery(onImageUriSelected: (Uri?) -> Unit) {
    val context = LocalContext.current
    val getContent = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        onImageUriSelected(uri)
    }

    Button(onClick = { getContent.launch("image/*") }) {
        Text("Bild auswählen")
    }
}

@Composable
fun DishList(dishes: List<Dish>, selectedDish: Dish?, onDishClick: (Dish) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // Zwei Spalten
        modifier = Modifier.padding(8.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(dishes) { dish ->
            DishItem(
                dish = dish,
                isSelected = dish == selectedDish, // Übergebe den isSelected-Status
                onClick = { onDishClick(dish) }
            )
        }
    }
}

@Composable
fun DishItem(dish: Dish, isSelected: Boolean, onClick: () -> Unit) {
    val imagePainter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(dish.imageUri)  // Nutze die gespeicherte URI
            .crossfade(true)
            .placeholder(R.drawable.image_placeholder)
            .error(R.drawable.image_placeholder)
            .build(),
        contentScale = ContentScale.Crop
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = if (isSelected) Color.LightGray else Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Image(
                painter = imagePainter,
                contentDescription = dish.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = dish.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}

@Composable
fun Recipes(navController: NavHostController) {
    val context = LocalContext.current
    val db = remember {
        Room.databaseBuilder(context, AppDatabase::class.java, "dishes-db").build()
    }
    val dishDao = db.dishDao()

    // Beobachte LiveData
    val dishes by dishDao.getAllDishes().observeAsState(emptyList())
    val coroutineScope = rememberCoroutineScope()
    var isAddDishFormVisible by remember { mutableStateOf(false) }
    var selectedDish by remember { mutableStateOf<Dish?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(20.dp))
            Top_back(navController)
            Spacer(modifier = Modifier.height(30.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "My Recipes", fontSize = 25.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(16.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.padding(8.dp),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(dishes) { dish ->
                    DishItem(
                        dish = dish,
                        isSelected = dish == selectedDish,
                        onClick = {
                            selectedDish = if (selectedDish == dish) null else dish
                        }
                    )
                }
            }
        }

        if (isAddDishFormVisible) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                AddDishForm(
                    onAddDish = { newDish ->
                        coroutineScope.launch {
                            dishDao.insertDish(newDish)
                            isAddDishFormVisible = false
                        }
                    },
                    onDismiss = { isAddDishFormVisible = false }
                )
            }
        }

        if (selectedDish != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable { selectedDish = null },
                contentAlignment = Alignment.Center
            ) {
                DishDetailOverlay(
                    dish = selectedDish!!,
                    onDismiss = { selectedDish = null },
                    onDelete = {
                        showDeleteDialog = true
                    }
                )
            }
        }

        if (showDeleteDialog) {
            ShowDeleteConfirmationDialog(
                onConfirm = {
                    coroutineScope.launch {
                        selectedDish?.let { dish ->
                            // Bild aus dem MediaStore löschen
                            dish.imageUri?.let { uri ->
                                context.contentResolver.delete(Uri.parse(uri), null, null)
                            }
                            // Gericht aus der Datenbank löschen
                            dishDao.deleteDish(dish)
                            selectedDish = null
                        }
                        showDeleteDialog = false
                    }
                },
                onDismiss = {
                    showDeleteDialog = false
                }
            )
        }

        FloatingActionButton(
            onClick = { isAddDishFormVisible = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .padding(bottom = 30.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Add Dish",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun DishDetailOverlay(dish: Dish, onDismiss: () -> Unit, onDelete: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable { onDismiss() }, // Schließt das Overlay, wenn der Hintergrund geklickt wird
        contentAlignment = Alignment.Center
    ) {
        // Container für den Detailinhalt
        Box(
            modifier = Modifier
                .wrapContentSize() // Auf die Größe des Inhalts begrenzen
                .fillMaxWidth()
                .background(Color.White)
                .clip(RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp) // Padding oben hinzufügen, um den Schließen-Button nicht zu überlappen
            ) {
                // Container für Schließen- und Löschen-Button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    // Löschen-Button
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp)
                    ) {
                        Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete")
                    }

                    // Schließen-Button
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                    ) {
                        Icon(imageVector = Icons.Filled.Close, contentDescription = "Close")
                    }
                }

                // Bild
                val imageRequest = ImageRequest.Builder(LocalContext.current)
                    .data(dish.imageUri)
                    .crossfade(true)
                    .placeholder(R.drawable.image_placeholder)
                    .error(R.drawable.image_placeholder)
                    .build()

                val imagePainter = rememberAsyncImagePainter(model = imageRequest)

                Image(
                    painter = imagePainter,
                    contentDescription = dish.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.LightGray)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Gerichtname
                Text(
                    text = dish.name,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Schwierigkeit
                Text(
                    text = "Difficulty: ${dish.difficulty}",
                    fontSize = 16.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Geschmack
                Text(
                    text = "Taste: ${dish.taste}",
                    fontSize = 16.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Anleitung
                Text(
                    text = "Instructions:",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = dish.instructions,
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun ShowDeleteConfirmationDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confirm Deletion") },
        text = { Text("Are you sure you want to delete the recipe?") },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Yes")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("No")
            }
        }
    )
}


/*
//What to Cook Container
 */
@Composable
fun CookingIdeas (navController: NavHostController) {
    val context = LocalContext.current
    val db = remember {
        Room.databaseBuilder(context, AppDatabase::class.java, "dishes-db").build()
    }
    val dishDao = db.dishDao()
    val dishes by dishDao.getAllDishes().observeAsState(emptyList())
    val randomDish = remember { mutableStateOf(getRandomDish(dishes)) }
    val expandedDish = remember { mutableStateOf<Dish?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column (
            modifier = Modifier.padding(10.dp)
        ){
            Top_back(navController)
        }

        // Gericht im Zentrum des Bildschirms anzeigen
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(horizontal = 16.dp), // Padding für bessere Sichtbarkeit
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            randomDish.value?.let { dish ->
                DishItem(
                    dish = dish,
                    isSelected = expandedDish.value == dish,
                    onClick = {
                        expandedDish.value = if (expandedDish.value == dish) null else dish
                    }
                )
            } ?: Text("No Meals displayed yet")
        }

        // Knopf zum Zufälligen Aussuchen am unteren Rand in der Mitte platzieren
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(50.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = {
                randomDish.value = getRandomDish(dishes) // Zustand aktualisieren
                expandedDish.value = null // Zurücksetzen der erweiterten Ansicht, wenn das Gericht geändert wird
            }) {
                Text("Show random Meal")
            }
        }

        // Detailansicht ohne Löschoption
        expandedDish.value?.let { dish ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable { expandedDish.value = null },
                contentAlignment = Alignment.Center
            ) {
                DishDetailView(
                    dish = dish,
                    onDismiss = { expandedDish.value = null }
                )
            }
        }
    }
}

fun getRandomDish(dishes: List<Dish>): Dish? {
    return if (dishes.isNotEmpty()) {
        dishes[Random.nextInt(dishes.size)]
    } else {
        null
    }
}

@Composable
fun DishDetailView(dish: Dish, onDismiss: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable { onDismiss() }, // Schließt das Overlay, wenn der Hintergrund geklickt wird
        contentAlignment = Alignment.Center
    ) {
        // Container für den Detailinhalt
        Box(
            modifier = Modifier
                .wrapContentSize() // Auf die Größe des Inhalts begrenzen
                .background(Color.White)
                .clip(RoundedCornerShape(16.dp))
                .padding(16.dp) // Padding um den Inhalt herum hinzufügen
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Container für Schließen- und Löschen-Button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    // Schließen-Button
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                    ) {
                        Icon(imageVector = Icons.Filled.Close, contentDescription = "Close")
                    }
                }

                // Bild
                val imageRequest = ImageRequest.Builder(LocalContext.current)
                    .data(dish.imageUri)
                    .crossfade(true)
                    .placeholder(R.drawable.image_placeholder)
                    .error(R.drawable.image_placeholder)
                    .build()

                val imagePainter = rememberAsyncImagePainter(model = imageRequest)

                Image(
                    painter = imagePainter,
                    contentDescription = dish.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.LightGray)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Gerichtname
                Text(
                    text = dish.name,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Schwierigkeit
                Text(
                    text = "Difficultly: ${dish.difficulty}",
                    fontSize = 16.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Geschmack
                Text(
                    text = "Taste: ${dish.taste}",
                    fontSize = 16.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Anleitung
                Text(
                    text = "Instructions:",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = dish.instructions,
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRandomScreen() {
    val navController = rememberNavController()

    CookingIdeas(navController)
}

/*@Preview(showBackground = true)
@Composable
fun PreviewDishScreen() {
    val navController = rememberNavController()

    Recipes(navController)
}*/

@Preview(showBackground = true)
@Composable
fun PreviewMyApp() {
    MyApp()
}

@Preview(showBackground = true)
@Composable
fun PreviewProfile() {
    val navController = rememberNavController()

    Profile(navController)
}