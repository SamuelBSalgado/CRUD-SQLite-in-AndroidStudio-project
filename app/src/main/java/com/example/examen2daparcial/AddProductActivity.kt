package com.example.examen2daparcial

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Button
import android.view.View
import android.widget.ListView
import android.widget.Toast

class AddProductActivity : AppCompatActivity() {
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var editTextName: EditText
    private lateinit var editTextDescription: EditText
    private lateinit var editTextQuantity: EditText
    private lateinit var editTextCostPrice: EditText
    private lateinit var editTextSellingPrice: EditText
    private lateinit var editTextImageUrl: EditText
    private lateinit var btnSaveProduct: Button

    //LISTAR LOS PRODUCTOS
    private fun loadProducts(){
        val databaseHelper = DatabaseHelper(this)
        val productList = databaseHelper.getAllProducts()
        val listView = findViewById<ListView>(R.id.productListView)
        val newAdapter = ProductAdapter(this, productList)
        listView.adapter = newAdapter
        newAdapter.notifyDataSetChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        databaseHelper = DatabaseHelper(this)

        editTextName = findViewById(R.id.editTextName)
        editTextDescription = findViewById(R.id.editTextDescription)
        editTextQuantity = findViewById(R.id.editTextQuantity)
        editTextCostPrice = findViewById(R.id.editTextCostPrice)
        editTextSellingPrice = findViewById(R.id.editTextSellingPrice)
        editTextImageUrl = findViewById(R.id.editTextImageUrl)
        btnSaveProduct = findViewById(R.id.btnSaveProduct)

        btnSaveProduct.setOnClickListener(View.OnClickListener {
            //Obtener los valores de los campos de entrada y darles formato.
            val name = editTextName.text.toString()
            val description = editTextDescription.text.toString()
            val quantity = editTextQuantity.text.toString().toInt()
            val costPrice = editTextCostPrice.text.toString().toDouble()
            val sellingPrice = editTextSellingPrice.text.toString().toDouble()
            val imageUrl = editTextImageUrl.text.toString()

            //Crear un objeto Product con los valores
            val product = Product(0, name, description, quantity, costPrice, sellingPrice, imageUrl)

            //Insertar el producto en la base de datos
            val productId = databaseHelper.insertProduct(product)

            //Comprobar si la inserción fue exitosa
            if (productId > 0) {
                //El producto se insertó correctamente en la tabla de la base de datos.
                Toast.makeText(this, "El producto ${name} se agregó exitosamente", Toast.LENGTH_LONG).show()

                val intent = Intent()
                intent.putExtra("productInserted", true)
                setResult(RESULT_OK, intent)

                //Regresar a la pantalla principal.
                loadProducts()
                finish() //Este finish pertenece al Intent que se hizo en el MainActivity.
            } else {
                //Hubo un error en la inserción
                Toast.makeText(this, "Hubo un error para insertar los datos de ${name}.", Toast.LENGTH_LONG).show()
            }
        })
    }
}