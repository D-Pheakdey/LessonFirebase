package com.example.firebase_and;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    Button btnSave,btnUpdate, btnDelete, btnSearch,btnList;
    EditText id,name,pass;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("User");
//        myRef = database.getReference("Search");

        btnSave = findViewById(R.id.btnSave);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        btnSearch = findViewById(R.id.btnSearch);
        btnList = findViewById(R.id.btnList);

        id = findViewById(R.id.Userid);
        name = findViewById(R.id.UserName);
        pass = findViewById(R.id.Userpass);

        Intent recive_i=getIntent();
        id.setText(recive_i.getStringExtra("uid"));
        name.setText(recive_i.getStringExtra("uname"));
        pass.setText(recive_i.getStringExtra("upass"));

        // Set an OnClickListener for the btnSave button
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushData();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchData();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData();
            }
        });
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, userlist.class);
                startActivity(i);
            }
        });

    }

    // Method to push data to Firebase
    public void pushData() {

//        myRef.setValue("Hello, KimEng Kak IT!");
//        myRef.push().setValue("ID");
//        myRef.push().setValue("Name");
//        myRef.push().setValue("Pass");
//        User user = new User("001","Pheakdey","1234");
//        User user = new User(id.getText().toString(),name.getText().toString(),pass.getText().toString());
//        myRef.push().setValue(user);

        String userId = id.getText().toString();
        String userName = name.getText().toString();
        String userPass = pass.getText().toString();

        if (!userId.isEmpty() && !userName.isEmpty() && !userPass.isEmpty()) {
            User user = new User(userId, userName, userPass);
            myRef.push().setValue(user);
            Toast.makeText(this, "Data saved successfully", Toast.LENGTH_SHORT).show();
//            clearFields();
        } else {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        }
    }
    public void updateData() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("User");

        Query query = myRef.orderByChild("id").equalTo(id.getText().toString());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ds.getRef().child("name").setValue(name.getText().toString());
                    ds.getRef().child("pass").setValue(pass.getText().toString());
                }
                Toast.makeText(MainActivity.this, "Data Edited Successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void searchData(){
        String userId = id.getText().toString().trim();
        if (userId.isEmpty()) {
            Toast.makeText(MainActivity.this, "Please enter a User ID", Toast.LENGTH_SHORT).show();
            return;
        }

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean userFound = false;
                for (DataSnapshot databaseSnapshot : snapshot.getChildren()) {
                    String dbUserId = databaseSnapshot.child("id").getValue(String.class);
                    if (dbUserId != null && dbUserId.equals(userId)) {
                        String dbName = databaseSnapshot.child("name").getValue(String.class);
                        String dbPass = databaseSnapshot.child("pass").getValue(String.class);

                        name.setText(dbName);
                        pass.setText(dbPass);
                        userFound = true;
                        break;
                    }
                }
                if (!userFound) {
                    Toast.makeText(MainActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void deleteData() {
        DatabaseReference myRef= FirebaseDatabase.getInstance().getReference("User");
        myRef.orderByChild("id").equalTo(id.getText().toString().trim()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot ds:snapshot.getChildren()){
                        ds.getRef().removeValue();
                        id.setText("");
                        name.setText("");
                        pass.setText("");
                    }
                    Toast.makeText(MainActivity.this, "Record has been deleted!", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(MainActivity.this, "User not Found!", Toast.LENGTH_SHORT).show();
                    id.setText("");
                    name.setText("");
                    pass.setText("");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void EditData(View view ){
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("User");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    if(ds.child("id").getValue(String.class).equals(id.getText().toString())){
                        ref.child("name").setValue(name.getText().toString());

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void clearFields() {
        id.setText("");
        name.setText("");
        pass.setText("");
    }
}
