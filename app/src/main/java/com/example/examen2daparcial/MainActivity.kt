package com.example.examen2daparcial

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.examen2daparcial.R
import com.example.examen2daparcial.DatabaseHelper
import java.io.IOException
import java.net.URL
import java.io.Serializable


//Adaptador personalizado para el ListView
class ProductAdapter(private val context: Context, private val productList: List<Product>) : BaseAdapter(){
    override fun getCount(): Int {
        return productList.size
    }

    override fun getItem(position: Int): Any {
        return productList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val product = productList[position]
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView = inflater.inflate(R.layout.activity_list, parent, false)

        //Inicialización de los elementos externos del activity_list.xml(es ahí donde se cargan los datos y después se extraen para ponerlos en el main).
        val idText = rowView.findViewById<TextView>(R.id.idText)
        val nameTextView = rowView.findViewById<TextView>(R.id.nameTextView)
        val descriptionTextView = rowView.findViewById<TextView>(R.id.descriptionTextView)
        val cantidadTextView = rowView.findViewById<TextView>(R.id.cantidadTextView)
        val precioCostoTextView = rowView.findViewById<TextView>(R.id.precioCostoTextView)
        val precioVentaTextView = rowView.findViewById<TextView>(R.id.PrecioVentaTextView)
        val imageview= rowView.findViewById<ImageView>(R.id.imagen)
        //Se usa glide para acceder a las imágenes en línea y descargarlas, y de una vez se manda la imagen a activity_list.xml
        Glide.with(context)
            .load(product.imageUrl)
            .into(imageview)

        //Aquí se mandan los datos a activity_list.xml
        idText.text = "Id: ${product.id}"
        nameTextView.text = "Nombre de producto: ${product.name}"
        descriptionTextView.text = "Descripción: ${product.description}"
        cantidadTextView.text = "Cantidad: ${product.quantity.toString()}"
        precioCostoTextView.text = "Precio costo: ${product.costPrice.toString()}MXN"
        precioVentaTextView.text = "Precio venta: ${product.sellingPrice.toString()}MXN"

        return rowView
    }
}

class MainActivity : AppCompatActivity() {

    val items = mutableListOf<Product>()
    //val add_product_request = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val databaseHelper = DatabaseHelper(this)
        val productList = databaseHelper.getAllProducts()

        val listView = findViewById<ListView>(R.id.productListView)

        val newAdapter = ProductAdapter(this, productList)
        listView.adapter = newAdapter
        newAdapter.notifyDataSetChanged()

        /*Si el booleano que indica que se han añadido los datos
        (escrito en la línea 57 del AddProductActivity.kt) llega en true,
        se llama a la función que carga todo en el main.*/
        /*if (intent.getBooleanExtra("productInserted", true)){
            Toast.makeText(this, "El producto", Toast.LENGTH_LONG).show()
            //Actualizar lista de productos

        }*/

        //Botón para agregar productos.
        val addButton = findViewById<Button>(R.id.addProductButton)
        addButton.setOnClickListener(){
            //Al presionarse el botón, se crea el Intent hacia AddProductActivity.
            val intent = Intent(this, AddProductActivity::class.java)
            startActivity(intent)
            //Toast.makeText(this, "Toast", Toast.LENGTH_LONG).show()
        }

        val list = findViewById<ListView>(R.id.productListView)
        //val listed_product = mutableListOf<String>()

        //Al hacer clic en un producto de la ListView, nos direcciona a la activity de eliminar y editar.
        list.setOnItemClickListener(){parent, view, position, id, ->
            //Ir a la activity que edita y elimina el item/producto seleccionado
            val adapter = list.adapter as ProductAdapter
            val selected_product = adapter.getItem(position) as Product
            val intentoEdit_Delete = Intent(this, Edit_Delete::class.java)
            intentoEdit_Delete.putExtra("selected_item", selected_product)
            startActivity(intentoEdit_Delete)
        }
    }

    //Función que carga/actualiza los datos
    /*private fun loadProducts(){
        /*val listView = findViewById<ListView>(R.id.productListView)
        val newAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, items)
        listView.adapter = newAdapter
        newAdapter.notifyDataSetChanged()
        registerForContextMenu(listView)*/

        val databaseHelper = DatabaseHelper(this)
        val productList = databaseHelper.getAllProducts()

        val productListView = findViewById<ListView>(R.id.productListView)
        val adapter = ProductAdapter(this, productList)

        productListView.adapter = adapter
    }*/
}