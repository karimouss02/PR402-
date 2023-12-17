package com.example.p_402

abstract class Vehiculo(var ruedas: Int, val motor: String, val numAsientos: Int, val color: String, val modelo: String, open val cargaMaxima: Int = 0) {
    override fun toString(): String {
        return "Tipo: ${javaClass.simpleName}, Ruedas: $ruedas, Motor: $motor, Asientos: $numAsientos, Color: $color, Modelo: $modelo"
    }
}


