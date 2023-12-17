package com.example.p_402

class Furgoneta(ruedas: Int, motor: String, numAsientos: Int, color: String, modelo: String,override val cargaMaxima: Int):
    Vehiculo(if (ruedas<6) ruedas else 6, motor, numAsientos, color, modelo) {

}