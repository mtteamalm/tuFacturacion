package com.example.tufacturacion.ui;

import android.database.DatabaseUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tufacturacion.R;
import com.example.tufacturacion.modelo.CabeceraPedido;
import com.example.tufacturacion.modelo.Cliente;
import com.example.tufacturacion.modelo.DetallePedido;
import com.example.tufacturacion.modelo.FormaPago;
import com.example.tufacturacion.modelo.Producto;
import com.example.tufacturacion.sqlite.OperacionesBaseDatos;

import java.util.Calendar;

public class ActividadListaPedidos extends AppCompatActivity {

    OperacionesBaseDatos datos;

    public class TareaPruebaDatos extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            //INSERCIONES
            String fechaActual = Calendar.getInstance().getTime().toString();

            try {
                datos.getDb().beginTransaction();

                //Inserción de Clientes
                String cliente1 = datos.insertarCliente(new Cliente(null,
                        "Amancio", "Ortega", "4552000"));
                String cliente2 = datos.insertarCliente(new Cliente(null,
                        "Sergio", "Ramos", "4440000"));

                // Inserción Formas de pago
                String formaPago1 = datos.insertarFormaPago(new FormaPago(null, "Efectivo"));
                String formaPago2 = datos.insertarFormaPago(new FormaPago(null, "Tarjeta"));

                // Inserción Productos
                String producto1 = datos.insertarProducto(new Producto(null, "Ferrari F40", 630500, 100));
                String producto2 = datos.insertarProducto(new Producto(null, "Bentley 3", 365888000, 230));
                String producto3 = datos.insertarProducto(new Producto(null, "Rolls Royce", 50000000, 55));
                String producto4 = datos.insertarProducto(new Producto(null, "Lamborghini Diablo", 3000000.6f, 60));

                // Inserción Pedidos
                String pedido1 = datos.insertarCabeceraPedido(
                        new CabeceraPedido(null, fechaActual, cliente1, formaPago1));
                String pedido2 = datos.insertarCabeceraPedido(
                        new CabeceraPedido(null, fechaActual,cliente2, formaPago2));

                // Inserción Detalles
                datos.insertarDetallePedido(new DetallePedido(pedido1, 1, producto1, 5, 2));
                datos.insertarDetallePedido(new DetallePedido(pedido1, 2, producto2, 10, 3));
                datos.insertarDetallePedido(new DetallePedido(pedido2, 1, producto3, 30, 5));
                datos.insertarDetallePedido(new DetallePedido(pedido2, 2, producto4, 20, 3.6f));

                // Eliminación Pedido
                datos.eliminarCabeceraPedido(pedido1);

                // Actualización Cliente
                datos.actualizarCliente(new Cliente(cliente2, "Sergio", "Ramos Garcia", "3333333"));

                datos.getDb().setTransactionSuccessful();
            }finally {
                datos.getDb().endTransaction();
            }
            // [QUERIES]
            Log.d("Clientes","Clientes");
            DatabaseUtils.dumpCursor(datos.obtenerClientes());
            Log.d("Formas de pago", "Formas de pago");
            DatabaseUtils.dumpCursor(datos.obtenerFormasPago());
            Log.d("Productos", "Productos");
            DatabaseUtils.dumpCursor(datos.obtenerProductos());
            Log.d("Cabeceras de pedido", "Cabeceras de pedido");
            DatabaseUtils.dumpCursor(datos.obtenerCabecerasPedido());
            Log.d("Detalles de pedido", "Detalles de pedido");
            DatabaseUtils.dumpCursor(datos.obtenerDetallesPedido());

            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.pedidos_lista_actividad);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getApplicationContext().deleteDatabase("tufacturacion.db");
        datos = OperacionesBaseDatos
                .obtenerInstancia(getApplicationContext());

        new TareaPruebaDatos().execute();
    }
}
