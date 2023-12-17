package com.example.p_402

class Moto(ruedas: Int, motor: String, numAsientos: Int, color: String, modelo: String):
    Vehiculo(ruedas, motor, if (numAsientos<=2) numAsientos else 2, color, modelo) {

}