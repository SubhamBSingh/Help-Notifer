package com.example.subham.helpnotifier;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity
{
    SQLiteDatabase db;
    ArrayList AL = new ArrayList();
    ListView listView;
    int SelectedItemPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        db = openOrCreateDatabase("Test", MODE_PRIVATE, null);

        Button btnAdd = (Button) findViewById(R.id.btnAdd);
        Button btnBack = (Button) findViewById(R.id.btnBack);

        listView = (ListView) findViewById(R.id.listView);

        btnAdd.setOnClickListener(new MyClickNN());
        btnBack.setOnClickListener(new MyClickNN());

        Display();

        listView.setOnItemClickListener(new MyItemClick());
    }

    public void Display()
    {
        Cursor cur = db.rawQuery("select * from Contacts",null);

        if(cur.getCount() > 0)
        {

            while (cur.moveToNext())
            {
                String Name = cur.getString(0);
                String Number = cur.getString(1);

                AL.add(Name + "\n" + Number);
            }
            cur.close();
        }
        else
        {
            AL.add("Contact List Empty");
        }

        ArrayAdapter AA = new ArrayAdapter(Main2Activity.this,android.R.layout.simple_list_item_1,AL);
        listView.setAdapter(AA);
    }

    class MyClickNN implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            if(v.getId() == R.id.btnAdd)
            {
                Intent I = new Intent(Main2Activity.this, Main3Activity.class);
                startActivity(I);
            }
            else if(v.getId() == R.id.btnBack)
            {
                Intent intent = new Intent(Main2Activity.this,MainActivity.class);
                startActivity(intent);
            }
        }
    }

    class MyItemClick implements AdapterView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            SelectedItemPosition = position;

            AlertDialog.Builder AB = new AlertDialog.Builder(Main2Activity.this);

            AB.setTitle("Action");

            AB.setMessage("Do you want to Delete ?");

            AB.setPositiveButton("YES", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    String data = AL.get(SelectedItemPosition).toString();
                    String Arr[] = data.split("\n");

                    db.execSQL("delete from Contacts where Name='"+Arr[0]+"'");

                    Toast.makeText(Main2Activity.this, "Deleted", Toast.LENGTH_SHORT).show();

                    AL.clear();
                    Display();
                }
            });

            AB.setNegativeButton("NO", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    Toast.makeText(Main2Activity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                }
            });

            AB.show();
        }
    }
}
