package com.example.p_402

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast

/**
 * Clase que maneja las acciones de los botones en la interfaz de usuario
 * @property context Contexto de la aplicación
 * @property concesionario Lista que almacena los vehículos creados
 */
class ButtonActions(private val context: Context, private val concesionario: ArrayList<Vehiculo?>) {
    private val dialogInflater: LayoutInflater = LayoutInflater.from(context)
    private val maxVehicles = 100 // Establece el tamaño máximo del array
    private var vehicleCount: Int = 0
    private val vehicleArray: Array<Vehiculo?> = arrayOfNulls(maxVehicles)

    /**
     * Maneja el clic en el botón para crear un vehículo y muestra el cuadro de diálogo correspondiente
     */
    fun handleCreateVehicleButtonClick() {
        showCreateVehicleDialog()
    }

    /**
     * Valida la creación de un vehículo según sus propiedades
     * @param vehicleType Tipo de vehículo a crear
     * @param ruedas Número de ruedas del vehículo
     * @param motor Tipo de motor del vehículo
     * @param numAsiento Número de asientos del vehículo
     * @param color Color del vehículo
     * @param modelo Modelo del vehículo
     * @return Booleano que indica si la validación es exitosa
     */
    fun validateVehicleCreation(
        vehicleType: String,
        ruedas: Int,
        motor: String,
        numAsiento: Int,
        color: String,
        modelo: String
    ): Boolean {
        //Lógica de validación según el tipo de vehículo
        when (vehicleType) {
            "Patinete" -> {
                if (numAsiento > 0) {
                    showToast("Error: No se pueden poner asientos al patinete")
                    return false
                }
            }
            "Trailer" -> {
                if (ruedas < 6) {
                    showToast("Error: Los tráilers deben tener al menos 6 ruedas")
                    return false
                }
            }
            "Furgoneta" -> {
                if (ruedas > 6 || ruedas <= 0) {
                    showToast("Error: Las furgonetas deben tener como máximo 6 ruedas")
                    return false
                }
            }
            "Moto" -> {
                if (numAsiento > 2) {
                    showToast("Error: Las motos no pueden tener más de 2 asientos.")
                    return false
                }
            }
        }
        return true
    }

    /**
     * Maneja el clic en el botón para consultar la cantidad de vehículos y muestra el cuadro de diálogo correspondiente
     */
    fun handleConsultCountButtonClick() {
        showConsultCountDialog()
    }

    /**
     * Maneja el clic en el botón para listar los vehículos y muestra el cuadro de diálogo correspondiente
     */
    fun handleListVehiclesButtonClick() {
        showListVehiclesDialog()
    }

    /**
     * Maneja el clic en el botón de salida y muestra un mensaje de salida
     */
    fun handleExitButtonClick() {
        showToast("Saliendo de la aplicación")
    }

    /**
     * Muestra un cuadro de diálogo para crear un nuevo vehículo
     */
    private fun showCreateVehicleDialog() {
        //Lógica para mostrar el cuadro de diálogo de creación de vehículos
        val view = dialogInflater.inflate(R.layout.dialog_create_vehicle, null)
        val spinnerVehicleTypeDialog: Spinner = view.findViewById(R.id.spinnerVehicleTypeDialog)
        val ruedasEditText: EditText = view.findViewById(R.id.editTextRuedas)
        val motorEditText: EditText = view.findViewById(R.id.editTextMotor)
        val numAsientoEditText: EditText = view.findViewById(R.id.editTextAsiento)
        val colorEditText: EditText = view.findViewById(R.id.editTextColor)
        val modeloEditText: EditText = view.findViewById(R.id.editTextModelo)

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Crear Vehículo")
        builder.setView(view)

        val layoutNumAsiento: LinearLayout = view.findViewById(R.id.layoutNumAsiento)

        builder.setPositiveButton("Crear") { dialog, _ ->

            val vehicleType = spinnerVehicleTypeDialog.selectedItem.toString()
            val ruedas = ruedasEditText.text.toString().toIntOrNull() ?: 0
            val motor = motorEditText.text.toString()
            val numAsiento = if (vehicleType == "Patinete") 0 else numAsientoEditText.text.toString().toIntOrNull() ?: 0
            val color = colorEditText.text.toString()
            val modelo = modeloEditText.text.toString()

            if (validateVehicleCreation(vehicleType, ruedas, motor, numAsiento, color, modelo)) {
                createVehicle(vehicleType, ruedas, motor, numAsiento, color, modelo)
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.cancel()
        }

        if (spinnerVehicleTypeDialog.selectedItem.toString() == "Patinete") {
            layoutNumAsiento.visibility = View.GONE
        } else {
            layoutNumAsiento.visibility = View.VISIBLE
        }

        builder.show()
    }


    /**
     * Crea un vehículo según el tipo y sus propiedades y lo agrega a la lista
     * @param vehicleType Tipo de vehículo a crear
     * @param ruedas Número de ruedas del vehículo
     * @param motor Tipo de motor del vehículo
     * @param numAsiento Número de asientos del vehículo
     * @param color Color del vehículo
     * @param modelo Modelo del vehículo
     */
    private fun createVehicle(vehicleType: String, ruedas: Int, motor: String, numAsiento: Int, color: String, modelo: String) {
        //Lógica para crear un vehículo y agregarlo a la lista
        if (vehicleCount < maxVehicles) {
            val vehiculo: Vehiculo? = when (vehicleType) {
                "Coche" -> Coche(ruedas, motor, numAsiento, color, modelo)
                "Moto" -> Moto(ruedas, motor, numAsiento, color, modelo)
                "Patinete" -> Patinete(ruedas, motor, color, modelo)
                "Furgoneta" -> Furgoneta(ruedas, motor, numAsiento, color, modelo, 0)
                "Trailer" -> Trailer(ruedas, motor, numAsiento, color, modelo, 0)
                else -> null
            }

            if (vehiculo != null) {
                showToast("Vehículo creado: $vehiculo")
                vehicleCount++
                vehicleArray[vehicleCount - 1] = vehiculo
                showConsultCountDialog()
            } else {
                showToast("Tipo de vehículo no reconocido: $vehicleType")
            }
        } else {
            showToast("Se ha alcanzado la cantidad máxima de vehículos")
        }
    }


    /**
     * Muestra un cuadro de diálogo con información sobre la cantidad de vehículos creados
     */
    private fun showConsultCountDialog() {
        //Lógica para mostrar el cuadro de diálogo de consulta de cantidad de vehículos
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Consultar Cantidad")
        val contadorCoches = vehicleArray.count { it is Coche }
        val contadorMotos = vehicleArray.count { it is Moto }
        val contadorPatinetes = vehicleArray.count { it is Patinete }
        val contadorFurgonetas = vehicleArray.count { it is Furgoneta }
        val contadorTrailers = vehicleArray.count { it is Trailer }

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
        //Lógica para mostrar el cuadro de diálogo de lista de vehículos
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Listado de Vehículos")
        val vehicleList = vehicleArray.filterNotNull()
        val message = if (vehicleList.isNotEmpty()) {
            vehicleList.joinToString("\n") { "${it.modelo}: ${it.javaClass.simpleName}" }
        } else {
            "No hay vehículos creados"
        }
        builder.setMessage(message)
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    /**
     * Muestra un mensaje Toast
     * @param message Mensaje a mostrar
     */
    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
