package com.example.subham.helpnotifier;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Main3Activity extends AppCompatActivity
{
    SQLiteDatabase db;
    EditText etName,etNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        db = openOrCreateDatabase("Test",MODE_PRIVATE,null);

         etName = (EditText) findViewById(R.id.etName);
         etNumber = (EditText) findViewById(R.id.etNumber);

        Button btnSave = (Button) findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new MyClickN());
    }

    class MyClickN implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            String Name = etName.getText().toString();
            String Number = etNumber.getText().toString();

            Cursor cur = db.rawQuery("select * from Contacts",null);

            if(cur.getCount() > 5)
            {
                Toast.makeText(Main3Activity.this, "Maximum Limit Reached", Toast.LENGTH_SHORT).show();

                Intent I = new Intent(Main3Activity.this,Main2Activity.class);
                startActivity(I);
            }

            else
            {
                db.execSQL("insert into Contacts values('" + Name + "','" + Number + "')");

                Toast.makeText(Main3Activity.this, "Saved", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Main3Activity.this,Main2Activity.class);
                startActivity(intent);
            }
        }
    }
}
