package com.example.fitnessapp.view

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.widget.TimePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.fitnessapp.model.Exercise
import com.example.fitnessapp.viewmodel.CreatePlanViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePlanScreen(
    viewModel: CreatePlanViewModel = hiltViewModel(),
    navController: NavController
) {
    var planName by remember { mutableStateOf("") } // Plan adı için bir durum
    var notificationTime by remember { mutableStateOf("") } // Bildirim saati için bir durum
    val exerciseList by viewModel.exerciseList // ViewModel'den egzersiz listesini alıyoruz
    val checkedState = remember { mutableStateOf<Map<Exercise, Boolean>>(emptyMap()) } // Seçili olan egzersizlerin durumlarını saklıyoruz
    val quantity = remember { mutableStateOf<Map<Exercise,Int>>(emptyMap()) }
    var selectedExercise by remember { mutableStateOf<Exercise?>(null) }
    var showQuantityDialog by remember { mutableStateOf(false) }



    LaunchedEffect(key1 = Unit) {
        viewModel.refreshData() // Ekran açıldığında egzersiz verilerini yenile
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Plan") },
                actions = {
                    TextButton(onClick = {
                        val selectedExercisesWithQuantity = checkedState.value.filter { it.value }.map { exerciseEntry ->
                            val exercise = exerciseEntry.key
                            var qty = quantity.value[exercise] ?: 1
                            if (qty <= 0){
                                qty = 1
                            }
                            exercise to qty
                        }.toMap()
                        viewModel.selectExerciseandQuantity.value = selectedExercisesWithQuantity
                        viewModel.savePLanAndExercise(planName,notificationTime).apply {
                            navController.navigateUp()
                        }
                    }) {
                        Text("Save")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues) // Scaffold'tan gelen padding değerlerini ekliyoruz
                .padding(top = 20.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(5.dp)) // Elemanlar arasında boşluk bırakıyoruz

            TextField(
                value = planName,
                onValueChange = { planName = it },
                label = { Text("Plan Name") },
                modifier = Modifier.fillMaxWidth(0.8f)
            )

            Spacer(modifier = Modifier.height(5.dp))

            TimePickerField(
                selectedTime = notificationTime, // Zaman seçici için selectedTime
                onTimeSelected = { notificationTime = it }, // Zaman seçildiğinde güncellenir
                modifier = Modifier.fillMaxWidth(0.8f) // Genişlik %80 olarak ayarlanır
            )

            Spacer(modifier = Modifier.height(20.dp)) // Boşluk bırakıyoruz

            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(exerciseList, key = { it.id }) { exercise -> // Egzersiz listesini göster
                    ExerciseItem(
                        exercise = exercise,
                        isChecked = checkedState.value[exercise] ?: false, // Seçili durum
                        onCheckedChange = { exercise, isChecked ->
                            checkedState.value = checkedState.value.toMutableMap().apply {
                                this[exercise] = isChecked // Seçim değiştiğinde durum güncellenir
                                if (isChecked) {
                                    selectedExercise = exercise
                                    showQuantityDialog = true
                                } else {
                                    checkedState.value = checkedState.value.toMutableMap().apply {
                                        remove(exercise)
                                    }
                                    quantity.value = quantity.value.toMutableMap().apply {
                                        remove(exercise)
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
        if (showQuantityDialog && selectedExercise != null) {
            QuantityInputDialog(
                onDismissRequest = { showQuantityDialog = false },
                onQuantitySubmit = { qty ->
                    checkedState.value = checkedState.value.toMutableMap().apply {
                        this[selectedExercise!!] = true
                    }
                    quantity.value = quantity.value.toMutableMap().apply {
                        this[selectedExercise!!] = qty
                    }
                    showQuantityDialog = false
                }
            )
        }
    }
}

@Composable
fun ExerciseItem(
    exercise: Exercise,
    isChecked: Boolean,
    onCheckedChange: (Exercise, Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp) // Kartın etrafında 8 dp boşluk bırak
            .fillMaxWidth() // Kartın genişliğini tam ekran yap
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp) // Row'un etrafında 8 dp boşluk bırak
                .fillMaxWidth(), // Row'un genişliğini tam ekran yap
            horizontalArrangement = Arrangement.SpaceBetween, // Yatayda boşluk bırak
            verticalAlignment = Alignment.CenterVertically // Dikeyde ortala
        ) {
            Text(
                text = exercise.name, // Egzersizin ismini göster
                fontSize = 16.sp, // Font boyutunu ayarla
                modifier = Modifier.weight(1f) // Text'in genişliğini ayarla
            )
            Checkbox(
                checked = isChecked, // Checkbox'ın seçili olup olmadığını belirle
                onCheckedChange = { checked ->
                    onCheckedChange(exercise, checked) // Checkbox durumu değiştiğinde güncelle
                }
            )
        }
    }
}

@Composable
fun QuantityInputDialog(
    onDismissRequest: () -> Unit,
    onQuantitySubmit: (Int) -> Unit
) {
    var quantity by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            Button(
                onClick = {
                    val qty = quantity.toIntOrNull() ?: 0
                    onQuantitySubmit(qty)
                }
            ) {
                Text("Submit")
            }
        },
        text = {
            Column {
                Text(text = "Enter Quantity")
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = quantity,
                    onValueChange = {
                        if (it.all { char -> char.isDigit() }) {
                            quantity = it
                        }
                    },
                    label = { Text("Quantity") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    )
}

@Composable
fun TimePickerField(
    selectedTime: String,
    onTimeSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current // Şu anki Context'i al
    val calendar = Calendar.getInstance() // Şu anki tarih ve saat bilgisini al
    val hour = calendar.get(Calendar.HOUR_OF_DAY) // Saat
    val minute = calendar.get(Calendar.MINUTE) // Dakika

    // Zaman seçici dialogunu oluştur
    val timePickerDialog = TimePickerDialog(
        context,
        { _: TimePicker, selectedHour: Int, selectedMinute: Int ->
            val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute) // Seçilen zamanı formatla
            onTimeSelected(formattedTime) // Zaman seçildiğinde callback'i tetikle
        }, hour, minute, true // 24 saat formatı
    )

    TextField(
        value = selectedTime, // Seçili zamanı göster
        onValueChange = {}, // TextField'in içeriği değiştirilemez
        label = { Text("Select Time") }, // Etiket
        modifier = modifier
            .fillMaxWidth() // Genişliği tam ekran yap
            .clickable { timePickerDialog.show() }, // Tıklanınca zaman seçici dialogunu göster
        enabled = false // Kullanıcı manuel olarak yazamaz
    )
}
