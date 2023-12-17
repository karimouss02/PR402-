package com.example.p_402

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

/**
* Clase principal que representa la actividad principal de la aplicación
* Esta actividad permite al usuario interactuar con la aplicación para crear, consultar y listar vehículos
*/
class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var buttonActions: ButtonActions
    /**
     * Lista que almacena los vehículos creados en el concesionario
     */
    private val concesionario = ArrayList<Vehiculo?>()

    companion object {
        const val MAX_VEHICLES = 10
    }

    /**
     * Método de inicialización de la actividad
     * Se llama cuando la actividad se crea por primera vez
     * @param savedInstanceState Estado guardado de la actividad
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Inicialización de objetos y configuración de la interfaz de usuario
        buttonActions = ButtonActions(this, concesionario)

        setupUI()

        val buttonCreateVehicle: Button = findViewById(R.id.btnCreateVehicle)
        buttonCreateVehicle.setOnClickListener(this)

        val buttonConsultCount: Button = findViewById(R.id.btnConsultCount)
        buttonConsultCount.setOnClickListener(this)
    }

    /**
     * Configura la interfaz de usuario de la actividad
     */
    private fun setupUI() {
        val spinnerVehicleType: Spinner = findViewById(R.id.spinnerVehicleType)
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.vehicle_types,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerVehicleType.adapter = adapter
    }

    /**
     * Maneja los eventos de clic en los elementos de la interfaz de usuario
     * @param view Vista que se ha hecho clic
     */
    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnCreateVehicle -> buttonActions.handleCreateVehicleButtonClick()
            R.id.btnConsultCount -> buttonActions.handleConsultCountButtonClick()
            R.id.btnListVehicles -> buttonActions.handleListVehiclesButtonClick()
            R.id.btnExit -> buttonActions.handleExitButtonClick()
        }
    }

    fun onMenuOptionClicked(view: View) {
        when (view.id) {
            R.id.btnCreateVehicle -> {
                buttonActions.handleCreateVehicleButtonClick()
            }
            R.id.btnConsultCount -> {
                buttonActions.handleConsultCountButtonClick()
            }
            R.id.btnListVehicles -> {
                buttonActions.handleListVehiclesButtonClick()
            }
            R.id.btnExit -> {
                buttonActions.handleExitButtonClick()
            }
        }
    }

    /**
     * Muestra un cuadro de diálogo para crear un nuevo vehículo
     */
    private fun showCreateVehicleDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.dialog_vehicle_type))
        val vehicles = resources.getStringArray(R.array.vehicle_types)

        builder.setItems(vehicles) { _, which ->
            val vehicleType = vehicles[which]
            val layout = LinearLayout(this)
            layout.orientation = LinearLayout.VERTICAL

            val ruedasEditText = EditText(this)
            ruedasEditText.hint = "Número de ruedas"
            layout.addView(ruedasEditText)

            val motorEditText = EditText(this)
            motorEditText.hint = "Tipo de motor"
            layout.addView(motorEditText)

            val numAsientosEditText = EditText(this)
            numAsientosEditText.hint = "Número de asientos"
            layout.addView(numAsientosEditText)

            val colorEditText = EditText(this)
            colorEditText.hint = "Color"
            layout.addView(colorEditText)

            val modeloEditText = EditText(this)
            modeloEditText.hint = "Modelo"
            layout.addView(modeloEditText)

            if (vehicleType == "Furgoneta" || vehicleType == "Trailer") {
                val cargaMaximaEditText = EditText(this)
                cargaMaximaEditText.hint = "Carga máxima"
                layout.addView(cargaMaximaEditText)
            }

            builder.setView(layout)

            val cargaMaximaEditText = layout.findViewById<EditText>(R.id.editTextCargaMaxima)

            builder.setPositiveButton("Crear") { _, _ ->
                val ruedas = ruedasEditText.text.toString().toIntOrNull() ?: 0
                val motor = motorEditText.text.toString()
                val numAsientosEditText = layout.getChildAt(2) as EditText
                val numAsientos = numAsientosEditText.text.toString().toIntOrNull() ?: 0
                val color = colorEditText.text.toString()
                val modelo = modeloEditText.text.toString()
                val cargaMaxima_Text = if (vehicleType == "Furgoneta" || vehicleType == "Trailer") {
                    cargaMaximaEditText.text.toString().toIntOrNull() ?: 0
                } else {
                    0
                }

                if (buttonActions.validateVehicleCreation(vehicleType, ruedas, motor, numAsientos, color, modelo)) {
                    val cargaMaxima = when (vehicleType) {
                        "Furgoneta", "Trailer" -> {
                            val cargaMaximaEditText = layout.getChildAt(layout.childCount - 1) as EditText
                            cargaMaximaEditText.text.toString().toIntOrNull() ?: 0
                        }
                        else -> 0
                    }

                    val newVehicle = when (vehicleType) {
                        "Coche" -> Coche(ruedas, motor, numAsientos, color, modelo)
                        "Moto" -> Moto(ruedas, motor, numAsientos, color, modelo)
                        "Patinete" -> Patinete(ruedas, motor, color, modelo)
                        "Furgoneta" -> Furgoneta(ruedas, motor, numAsientos, color, modelo, cargaMaxima)
                        "Trailer" -> Trailer(ruedas, motor, numAsientos, color, modelo, cargaMaxima)
                        else -> null
                    }

                    if (newVehicle != null) {
                        if (newVehicle is Patinete && newVehicle.numAsientos > 0) {

                            Toast.makeText(this, "Error: Los patinetes no tienen asientos.", Toast.LENGTH_SHORT).show()
                        } else if (newVehicle is Trailer && (newVehicle.ruedas < 6 || newVehicle.cargaMaxima <= 0)) {

                            Toast.makeText(this, "Error: Los tráilers deben tener al menos 6 ruedas y una carga máxima válida.", Toast.LENGTH_SHORT).show()
                        } else if (newVehicle is Furgoneta && (newVehicle.ruedas > 6 || newVehicle.cargaMaxima <= 0)) {

                            Toast.makeText(this, "Error: Las furgonetas deben tener como máximo 6 ruedas y una carga máxima válida.", Toast.LENGTH_SHORT).show()
                        } else if (newVehicle is Moto && newVehicle.numAsientos > 2) {

                            Toast.makeText(this, "Error: Las motos no pueden tener más de 2 asientos.", Toast.LENGTH_SHORT).show()
                        } else {

                            Toast.makeText(this, "Vehículo creado: ${newVehicle.modelo}", Toast.LENGTH_SHORT).show()
                            concesionario.add(newVehicle)
                            showConsultCountDialog()
                        }
                    } else {
                        Toast.makeText(this, "Error al crear el vehículo", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            builder.setNegativeButton("Cancelar") { dialog, _ ->
                dialog.cancel()
            }

            builder.show()
        }
    }

    private fun createVehicle(vehicleClass: Class<out Vehiculo>) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.dialog_vehicle_properties))

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL

        val ruedasEditText = EditText(this)
        ruedasEditText.hint = "Número de ruedas"
        layout.addView(ruedasEditText)

        val motorEditText = EditText(this)
        motorEditText.hint = "Tipo de motor"
        layout.addView(motorEditText)

        val numAsientosEditText = EditText(this)
        numAsientosEditText.hint = "Número de asientos"
        layout.addView(numAsientosEditText)

        val colorEditText = EditText(this)
        colorEditText.hint = "Color"
        layout.addView(colorEditText)

        val modeloEditText = EditText(this)
        modeloEditText.hint = "Modelo"
        layout.addView(modeloEditText)

        if (vehicleClass == Furgoneta::class.java || vehicleClass == Trailer::class.java) {
            val cargaMaximaEditText = EditText(this)
            cargaMaximaEditText.hint = "Carga máxima"
            layout.addView(cargaMaximaEditText)
        }

        builder.setView(layout)

        builder.setPositiveButton("Crear") { _, _ ->
            val ruedas = ruedasEditText.text.toString().toIntOrNull() ?: 0
            val motor = motorEditText.text.toString()
            val numAsientos = numAsientosEditText.text.toString().toIntOrNull() ?: 0
            val color = colorEditText.text.toString()
            val modelo = modeloEditText.text.toString()

            val cargaMaxima = if (vehicleClass == Furgoneta::class.java || vehicleClass == Trailer::class.java) {
                val cargaMaximaEditText = layout.getChildAt(layout.childCount - 1) as EditText
                cargaMaximaEditText.text.toString().toIntOrNull() ?: 0
            } else {
                0
            }

            val newVehicle = when (vehicleClass) {
                Coche::class.java -> Coche(ruedas, motor, numAsientos, color, modelo)
                Moto::class.java -> Moto(ruedas, motor, numAsientos, color, modelo)
                Patinete::class.java -> Patinete(ruedas, motor, color, modelo)
                Furgoneta::class.java -> Furgoneta(ruedas, motor, numAsientos, color, modelo, cargaMaxima)
                Trailer::class.java -> Trailer(ruedas, motor, numAsientos, color, modelo, cargaMaxima)
                else -> null
            }

            if (newVehicle != null) {
                Toast.makeText(this, "Vehículo creado: ${newVehicle.modelo}", Toast.LENGTH_SHORT).show()
                concesionario.add(newVehicle)
            } else {
                Toast.makeText(this, "Error al crear el vehículo", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.cancel()
        }
        builder.show()
    }

    /**
     * Muestra un cuadro de diálogo con información sobre la cantidad de vehículos creados
     */
    private fun showConsultCountDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.dialog_consult_count_title))

        val contadorCoches = concesionario.count { it is Coche }
        val contadorMotos = concesionario.count { it is Moto }
        val contadorPatinetes = concesionario.count { it is Patinete }
        val contadorFurgonetas = concesionario.count { it is Furgoneta }
        val contadorTrailers = concesionario.count { it is Trailer }

        val message = """
        Número de vehículos creados:
        - Coches: $contadorCoches
        - Motos: $contadorMotos
        - Patinetes: $contadorPatinetes
        - Furgonetas: $contadorFurgonetas
        - Tráilers: $contadorTrailers
    """.trimIndent()

        builder.setMessage(message)

        builder.setPositiveButton("Aceptar") { dialog, _ ->
            dialog.dismiss()
        }

        builder.show()
    }

    /**
     * Muestra un cuadro de diálogo con la lista de vehículos creados
     */
    private fun showListVehiclesDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.dialog_list_vehicles_title))

        val vehiclesList = concesionario.mapIndexed { index, vehiculo ->
            val tipo = when (vehiculo) {
                is Coche -> "Coche"
                is Moto -> "Moto"
                is Patinete -> "Patinete"
                is Furgoneta -> "Furgoneta"
                is Trailer -> "Tráiler"
                null -> "Vehículo no disponible"
                else -> "Desconocido"
            }

            "$index. Tipo: $tipo"
        }

        val message = vehiclesList.joinToString("\n")

        builder.setMessage(message)

        builder.setPositiveButton("Aceptar") { dialog, _ ->
            dialog.dismiss()
        }

        builder.show()
    }

    /**
     * Método que se llama para crear el menú de opciones en la barra de acción
     * @param menu Menú de opciones
     * @return Booleano que indica si se ha inflado el menú correctamente
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    /**
     * Maneja la selección de elementos en el menú de opciones
     * @param item Elemento del menú que se ha seleccionado
     * @return Booleano que indica si la selección se ha manejado con éxito
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.btnCreateVehicle -> {
                showCreateVehicleDialog()
                return true
            }
            R.id.btnConsultCount -> {
                showConsultCountDialog()
                return true
            }
            R.id.btnListVehicles -> {
                showListVehiclesDialog()
                return true
            }
            R.id.btnExit -> {
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
