package it.mirea.kursovayabread.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import it.mirea.kursovayabread.R;

public abstract class BaseFragment extends Fragment {
    protected FirebaseAuth auth;
    protected FirebaseDatabase db;
    protected DatabaseReference users;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");
    }
}
