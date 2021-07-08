package com.example.tubesgroupb;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class HomeActivity extends AppCompatActivity {
    String[] daftar;
    ListView ListView01;
    Menu menu;
    protected Cursor cursor;
    DataHelper dbcenter;
    public static HomeActivity da;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button btn = (Button) findViewById(R.id.buttonInput);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // method ini digunakan untuk menampilkan activity input.
                //Akan dijalankan di tambahan di praktikum selanjutnya
                Intent inte = new Intent(HomeActivity.this, InputActivity.class);
                startActivity(inte);
            }
        });
        da = this;
        dbcenter = new DataHelper(this);
        RefreshList();
    }
    public void RefreshList() {
        SQLiteDatabase db = dbcenter.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM pegawai", null);
        daftar = new String[cursor.getCount()];
        cursor.moveToFirst();
        for (int cc = 0; cc < cursor.getCount(); cc++) {
            cursor.moveToPosition(cc);
            daftar[cc] =cursor.getString(0).toString()+ "-" + cursor.getString(1).toString()+ "-" + cursor.getString(4);
        }
        ListView01 = (ListView) findViewById(R.id.listView1);
        ListView01.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, daftar));
        ListView01.setSelected(true);
        ListView01.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3) {
                final String selection = daftar[arg2];
                //.getItemAtPosition(arg2).toString();
                final CharSequence[] dialogitem = {"Lihat Data", "Update Data", "Hapus Data"};
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setTitle("Pilihan");
                builder.setItems(dialogitem, new
                        DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                switch (item) {
                                    case 0:
                                        // akan memanggil activity detail data
                                        Intent i = new Intent(getApplicationContext(), ProfilActivity.class);
                                        i.putExtra("nama", selection);
                                        startActivity(i);
                                        break;
                                    case 1:
                                        // akan memanggil activity update data
                                        Intent in = new Intent(getApplicationContext(), UpdateActivity.class);
                                        in.putExtra("nama", selection);
                                        startActivity(in);
                                        break;
                                    case 2:
                                        // akan menghapus data
                                        String getInfo = selection;
                                        String[] pecah = getInfo.split("-");
                                        SQLiteDatabase db = dbcenter.getWritableDatabase();

                                        db.execSQL("DELETE FROM pegawai where nama = '"+ pecah[1]+"';");
                                        RefreshList();
                                        break;
                                }
                            }
                        });
                builder.create().show();
            }});
        ((ArrayAdapter)ListView01.getAdapter()).notifyDataSetInvalidated();
    }
}