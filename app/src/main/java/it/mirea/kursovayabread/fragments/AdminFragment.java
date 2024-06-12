package it.mirea.kursovayabread.fragments;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;

import it.mirea.kursovayabread.R;

public class AdminFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin, container, false);
        Button btnCode = view.findViewById(R.id.btnCode);
        btnCode.setOnClickListener(v -> adminCode(view));
        return view;
    }

    private void adminCode(View view) {
        DatabaseReference codes;
        codes = db.getReference("Codes");
        RelativeLayout root = view.findViewById(R.id.admin_activity);

        final EditText code = view.findViewById(R.id.code_field);
        if (TextUtils.isEmpty(code.getText().toString())) {
            Snackbar.make(root, "Введите код", Snackbar.LENGTH_SHORT).show();
            return;
        }
        codes.child(code.getText().toString())
                .setValue(code.getText().toString())
                .addOnSuccessListener(unused ->
                        Snackbar.make(root, "Код добавлен!", Snackbar.LENGTH_SHORT).show()
                );
    }
}
