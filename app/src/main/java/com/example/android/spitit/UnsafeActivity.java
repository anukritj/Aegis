package com.example.android.spitit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class UnsafeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ArrayList<String> list=new ArrayList<>();
    private HashMap<String,Object> map=new HashMap<>();
    private ListView listView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unsafe);

        toolbar=(Toolbar)findViewById(R.id.unsafe_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Unsafe people");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView=(ListView)findViewById(R.id.unsafe_list);
        mAuth=FirebaseAuth.getInstance();
        String uid=getIntent().getExtras().getString("UID");
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Emergency").child(uid).child("People_list");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                map=(HashMap<String,Object>)dataSnapshot.getValue();
                ArrayList<String> keySet=new ArrayList<>(map.keySet());
                final ArrayList<String> unsafe=new ArrayList<>();
                for (final String key:keySet)
                {
                    if (map.get(key).equals("Unsafe"))
                    {

                        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("Users").child(key);
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Log.e("Name",dataSnapshot.child("First name").getValue().toString()+" "+dataSnapshot.child("Last name").getValue().toString());
                                unsafe.add(dataSnapshot.child("First name").getValue().toString()+" "+dataSnapshot.child("Last name").getValue().toString());
                                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(UnsafeActivity.this, android.R.layout.simple_list_item_1, unsafe);
                                listView.setAdapter(arrayAdapter);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
                Log.e("unsafe people",unsafe.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
