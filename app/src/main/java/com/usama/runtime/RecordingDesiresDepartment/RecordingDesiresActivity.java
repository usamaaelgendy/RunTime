package com.usama.runtime.RecordingDesiresDepartment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.usama.runtime.R;
import com.usama.runtime.model.Student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RecordingDesiresActivity extends AppCompatActivity {

    MyRecyclerViewAdapter adapter;

    private RecyclerView recyclerView;
    public List<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording_desires);
        retriveArrayListOfDepartmentName();


        recyclerView = findViewById(R.id.desiresRecyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(arrayList);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        Button button = findViewById(R.id.confirmDesiresOfDepartment);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                putDesiresInDatabase();
//                System.out.println(arrayList);
//                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
//                DatabaseReference eventIdRef = rootRef.child("Student").child("Desires");
//                eventIdRef.setValue(arrayList);
            }
        });

    }

    private void putDesiresInDatabase() {

        Student student = new Student();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Student");

        HashMap<String, Object> studentMap = new HashMap<>();
        studentMap.put("desires", arrayList);
        String studentNationalId = getIntent().getStringExtra("studentNationalId");
        ref.child(studentNationalId).updateChildren(studentMap);

        Toast.makeText(RecordingDesiresActivity.this, "Your Desires update successfully.", Toast.LENGTH_SHORT).show();

    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            Collections.swap(arrayList, fromPosition, toPosition);

            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };


    // this method return arrayList of string include department name to use in your fucken Collections.swap
    // i spend 24 hours to make this Stupid method
    private void retriveArrayListOfDepartmentName() {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference eventIdRef = rootRef.child("Department");
        arrayList = new ArrayList<>();
        eventIdRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String plate = snapshot.child("departmentName").getValue().toString();
                    arrayList.add(plate);
                    Log.d("test", plate);
                    for (int i = 0; i < arrayList.size(); i++) {
                        System.out.println(arrayList.get(i));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }


}
