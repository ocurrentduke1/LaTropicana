package com.example.prueba2.ui.gallery;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.prueba2.DB;
import com.example.prueba2.Producto;
import com.example.prueba2.databinding.FragmentGalleryBinding;
import com.google.gson.Gson;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.Arrays;

public class GalleryFragment extends Fragment {
    private FragmentGalleryBinding binding;
    private EditText id, nombre, precio, descripcion, imagen;
    private Spinner spinner;
    private String[] opt = {
            "beers", "wines", "food", "snaks" // Por comodidad lo dejamos asi...
    };
    private DB db;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        spinner = binding.spinner;
        spinner.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, opt));
        // id = binding.txtEscaner;
        nombre = binding.txtNombreProducto;
        precio = binding.txtPrecio;
        descripcion = binding.txtDescripcionProducto;
        imagen = binding.txtImagenProducto;
        id = binding.txtEscaner;

        db = new DB(getContext());

        //boton para escaner
        TextView btnescan = (TextView) binding.btnescaner;
        btnescan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                escanearCodigoBarra(v);
            }
        });

        //boton registrar
        TextView btn_reg = (TextView) binding.btnRegistrarProducto;
        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrar(v);
            }
        });

        TextView btn_search = (TextView) binding.btnBuscarProducto;
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search(v);
            }
        });

        TextView btn_update = (TextView) binding.btnActualizarProducto;
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update(v);
            }
        });

        TextView btn_delete = (TextView) binding.btnBorrarProducto;
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(v);
            }
        });

        //final TextView textView = binding.textGallery;
        //galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //metodo de escaner
    public void escanearCodigoBarra(View view) {
        IntentIntegrator intentIntegrator = IntentIntegrator.forSupportFragment(this);

        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);

        intentIntegrator.setPrompt("Lector - CDP");
        intentIntegrator.setCameraId(0);
        intentIntegrator.setBeepEnabled(true);
        intentIntegrator.setBarcodeImageEnabled(true);
        intentIntegrator.initiateScan();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){

        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null){
            if (intentResult.getContents() == null){
                Toast.makeText(requireContext(), "Lectura cancelada.",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(requireContext(), "Datos leído.", Toast.LENGTH_SHORT).show();
                id.setText(intentResult.getContents());
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }//onActivityResult

    private void registrar(View v) {
        String selected = spinner.getSelectedItem().toString();
        String name = nombre.getText().toString();
        double price = Double.parseDouble(precio.getText().toString());
        String description = descripcion.getText().toString();
        String image = imagen.getText().toString();
        String _id = id.getText().toString();

        /* Registro con base de datos SQLite */
        SQLiteDatabase database = this.db.getWritableDatabase();
        if (!_id.isEmpty() && !name.isEmpty() && !precio.getText().toString().isEmpty() && !description.isEmpty() && !image.isEmpty()) {
            ContentValues values = new ContentValues();
            values.put("id", _id);
            values.put("name", name);
            values.put("price", price);
            values.put("image", image);
            values.put("category", selected);
            values.put("description", description);
            if (database != null) {
                long x = 0;
                try {
                    x = database.insert("product", null, values);
                } catch (SQLException e) {
                    Log.e("Error", e.toString());
                }
                database.close();
            }
            id.setText("");
            nombre.setText("");
            precio.setText("");
            descripcion.setText("");
            imagen.setText("");
            Toast.makeText(getContext(), "Producto registrado", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Faltan campos por llenar", Toast.LENGTH_SHORT).show();
        }

    }

    private void search(View v) {
        SQLiteDatabase database = this.db.getWritableDatabase();
        String _id = id.getText().toString();
        if (!_id.isEmpty()) {
            Cursor cursor = database.rawQuery("SELECT * FROM product WHERE id = " + _id, null);
            if (cursor.moveToFirst()) {
                nombre.setText(cursor.getString(1));
                precio.setText(cursor.getString(2));
                imagen.setText(cursor.getString(3));
                descripcion.setText(cursor.getString(5));
            } else {
                Toast.makeText(getContext(), "Producto no encontrado", Toast.LENGTH_SHORT).show();
            }
            database.close();
        } else {
            Toast.makeText(getContext(), "Ingrese un ID", Toast.LENGTH_SHORT).show();
        }
    }

    private void update(View v) {
        SQLiteDatabase database = this.db.getWritableDatabase();
        String _id = id.getText().toString();
        if (!_id.isEmpty()) {
            ContentValues values = new ContentValues();
            values.put("name", nombre.getText().toString());
            values.put("price", precio.getText().toString());
            values.put("image", imagen.getText().toString());
            values.put("description", descripcion.getText().toString());
            long x = database.update("product", values, "id = " + _id, null);
            if (x > 0) {
                Toast.makeText(getContext(), "Producto actualizado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Producto no encontrado", Toast.LENGTH_SHORT).show();
            }
            database.close();
        } else {
            Toast.makeText(getContext(), "Ingrese un ID", Toast.LENGTH_SHORT).show();
        }
    }

    private void delete(View v) {
        SQLiteDatabase database = this.db.getWritableDatabase();
        String _id = id.getText().toString();
        if (!_id.isEmpty()) {
            long x = database.delete("product", "id = " + _id, null);
            if (x > 0) {
                Toast.makeText(getContext(), "Producto eliminado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Producto no encontrado", Toast.LENGTH_SHORT).show();
            }
            database.close();
        } else {
            Toast.makeText(getContext(), "Ingrese un ID", Toast.LENGTH_SHORT).show();
        }
    }
}