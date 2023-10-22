package com.example.examen2daparcial

import java.io.Serializable

//Clase Producto (con sus valores que se le pedirán al usuario).
data class Product(
    val id: Long,
    val name: String,
    val description: String,
    val quantity: Int,
    val costPrice: Double,
    val sellingPrice: Double,
    val imageUrl: String
): Serializable