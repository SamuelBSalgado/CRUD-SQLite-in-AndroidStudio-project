package com.example.examen2daparcial

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.Cursor
import android.widget.Toast
import java.sql.SQLException


class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "Examen2daParcial.db"
        private const val DATABASE_VERSION = 1
    }


    /*Crear valores y asignarles nombres para posteriormente
    crear la tabla y sus columnas con los nombres de los valores.*/
    //Valor nombre de la tabla.
    private val TABLE_NAME = "products"

    //Valores de las columnas.
    private val COLUMN_ID = "id"
    private val COLUMN_NAME = "name"
    private val COLUMN_DESCRIPTION = "description"
    private val COLUMN_QUANTITY = "quantity"
    private val COLUMN_COST_PRICE = "cost_price"
    private val COLUMN_SELLING_PRICE = "selling_price"
    private val COLUMN_IMAGE_URL = "image_url"

    //Crear la tabla con sus columnas.
    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_TABLE = ("CREATE TABLE $TABLE_NAME " +
                "($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_NAME TEXT, " +
                "$COLUMN_DESCRIPTION TEXT, " +
                "$COLUMN_QUANTITY INTEGER, " +
                "$COLUMN_COST_PRICE REAL, " +
                "$COLUMN_SELLING_PRICE REAL, " +
                "$COLUMN_IMAGE_URL TEXT)")
        db.execSQL(CREATE_TABLE)
    }

    //Función que se ejecuta cuando detecta que se ha actualizado (onUpgrade) la base de datos.
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        //Borra la tabla si es que ya existe para que no se creen varias de la misma cada vez que se ejecute el código.
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        //Crear otra vez la estructura que se eliminó en la línea anterior.
        onCreate(db)
    }


    // +----------------Métodos para operaciones CRUD----------------+
    //Método para INSERTAR un nuevo producto
    fun insertProduct(product: Product): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NAME, product.name)
        values.put(COLUMN_DESCRIPTION, product.description)
        values.put(COLUMN_QUANTITY, product.quantity)
        values.put(COLUMN_COST_PRICE, product.costPrice)
        values.put(COLUMN_SELLING_PRICE, product.sellingPrice)
        values.put(COLUMN_IMAGE_URL, product.imageUrl)

        return db.insert(TABLE_NAME, null, values)
    }

    //Método para ACTUALIZAR/EDITAR un producto existente
    fun updateProduct(product: Product): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NAME, product.name)
        values.put(COLUMN_DESCRIPTION, product.description)
        values.put(COLUMN_QUANTITY, product.quantity)
        values.put(COLUMN_COST_PRICE, product.costPrice)
        values.put(COLUMN_SELLING_PRICE, product.sellingPrice)
        values.put(COLUMN_IMAGE_URL, product.imageUrl)

        return db.update(TABLE_NAME, values, "$COLUMN_ID=?", arrayOf(product.id.toString()))
    }

    //Método para ELIMINAR un producto por ID
    fun deleteProduct(productId: Long): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_NAME, "$COLUMN_ID=?", arrayOf(productId.toString()))
    }

    //Método para LISTAR todos los productos
    fun getAllProducts(): List<Product> {
        val productList = ArrayList<Product>()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor: Cursor?

        try {
            cursor = db.rawQuery(query, null)
        }

        catch (e: SQLException) {
            db.execSQL(query)
            return ArrayList()
        }

        var id: Long
        var name: String
        var description: String
        var quantity: Int
        var costPrice: Double
        var sellingPrice: Double
        var imageUrl: String

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID))
                name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
                description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION))
                quantity = cursor.getInt(cursor.getColumnIndex(COLUMN_QUANTITY))
                costPrice = cursor.getDouble(cursor.getColumnIndex(COLUMN_COST_PRICE))
                sellingPrice = cursor.getDouble(cursor.getColumnIndex(COLUMN_SELLING_PRICE))
                imageUrl = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_URL))
                productList.add(Product(id, name, description, quantity, costPrice, sellingPrice, imageUrl))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return productList
    }
}