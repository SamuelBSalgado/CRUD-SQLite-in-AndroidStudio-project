package com.example.examen2daparcial

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.example.examen2daparcial.Product
import com.example.examen2daparcial.R


class Edit_Delete : AppCompatActivity() {

    //LISTAR PRODUCTOS
    private fun loadProducts(){
        val databaseHelper = DatabaseHelper(this)
        val productList = databaseHelper.getAllProducts()
        val listView = findViewById<ListView>(R.id.productListView)
        val newAdapter = ProductAdapter(this, productList)
        listView.adapter = newAdapter
        newAdapter.notifyDataSetChanged()
    }

    //private fun deleteProduct(productId: Long){
     //   val databaseHelper = DatabaseHelper(this)
       // val call_functionDelete = databaseHelper.deleteProduct(productId)
   // }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_delete)

        val btn_edit = findViewById<Button>(R.id.btn_edit)
        val btn_delete = findViewById<Button>(R.id.btn_delete)


        val product = intent.getSerializableExtra("selected_item") as? Product
        if (product != null){
            val nameEdit = findViewById<EditText>(R.id.et_name)
            val descriptionEdit = findViewById<EditText>(R.id.et_description)
            val etQUANTITY = findViewById<EditText>(R.id.et_cantidad)
            val etPRECIOcost = findViewById<EditText>(R.id.et_precio_costo)
            val etPRECIOventa = findViewById<EditText>(R.id.et_precio_venta)
            val etURL = findViewById<EditText>(R.id.et_URL)
            nameEdit.setHint(product.name)
            descriptionEdit.setHint(product.description)
            etQUANTITY.setHint(product.quantity.toString())
            etPRECIOcost.setHint(product.costPrice.toString())
            etPRECIOventa.setHint(product.sellingPrice.toString())
            etURL.setHint(product.imageUrl.toString())
        } else {
            Toast.makeText(this, "Error al visualizar el producto", Toast.LENGTH_LONG).show()
        }

        /* <-------------------------- FUNCIONES EDITAR Y ELIMINAR --------------------------> */
        //ELIMINAR PRODUCTO
        fun deleteProduct(productId:Long){
            val databaseHelper = DatabaseHelper(this)
            val call_deleteMethod = databaseHelper.deleteProduct(productId)
            Toast.makeText(this, "Producto eliminado con éxito", Toast.LENGTH_LONG).show()
            loadProducts()
            finish()
        }

        //EDITAR PRODUCTO
        fun editProduct(productId: Long, updatedProduct: Product){
            val databaseHelper = DatabaseHelper(this)
            val call_editMethod = databaseHelper.updateProduct(updatedProduct)

            if (call_editMethod > 0){
                Toast.makeText(this, "Producto editado con éxito", Toast.LENGTH_LONG).show()
                loadProducts()
                finish()
            } else {
                Toast.makeText(this, "Ocurrió un error al editar el producto", Toast.LENGTH_LONG).show()
            }
        }

        /* <----------------------- BOTONES EDITAR Y ELIMINAR -----------------------> */
        //BOTÓN ELIMINAR
        btn_delete.setOnClickListener(){
            if (product != null){
                deleteProduct(product.id)
            } else {
                Toast.makeText(this, "Error al eliminar el producto", Toast.LENGTH_LONG).show()
            }
            //finish()//--------------------------------------------------
        }

        //BOTÓN EDITAR
        btn_edit.setOnClickListener(){
            if (product != null){
                //editProduct(product.id, updatedProduct)
                val nameEdit = findViewById<EditText>(R.id.et_name)
                val descriptionEdit = findViewById<EditText>(R.id.et_description)
                val etQUANTITY = findViewById<EditText>(R.id.et_cantidad)
                val etPRECIOcost = findViewById<EditText>(R.id.et_precio_costo)
                val etPRECIOventa = findViewById<EditText>(R.id.et_precio_venta)
                val etURL = findViewById<EditText>(R.id.et_URL)

                val updatedProduct = Product(
                    product.id,
                    nameEdit.text.toString(),
                    descriptionEdit.text.toString(),
                    etQUANTITY.text.toString().toInt(),
                    etPRECIOcost.text.toString().toDouble(),
                    etPRECIOventa.text.toString().toDouble(),
                    etURL.text.toString()
                )
                editProduct(product.id, updatedProduct)
            } else {
                Toast.makeText(this, "Ocurrió un error al editar el producto", Toast.LENGTH_LONG).show()
            }
            //finish() //-------------------------------------------
        }
    }
}