package com.example.p_402

class Trailer(ruedas: Int, motor: String, numAsientos: Int, color: String, modelo: String,cargaMaxima: Int):
    Vehiculo(if (ruedas >= 6) ruedas else 6, motor, numAsientos, color, modelo) {

}