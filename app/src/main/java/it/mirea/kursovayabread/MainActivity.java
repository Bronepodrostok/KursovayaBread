package it.mirea.kursovayabread;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import it.mirea.kursovayabread.models.User;

public class MainActivity extends AppCompatActivity {
    Button btnSignIn, btnRegister; //кнопка входа и регистрации
    FirebaseAuth auth;//авторизация
    FirebaseDatabase db;//база данных
    DatabaseReference users;//Колонка пользователей в бд

    RelativeLayout root;//главная активность

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSignIn = findViewById(R.id.btnSignIn);
        btnRegister = findViewById(R.id.btnRegister);

        root = findViewById(R.id.root_element);
        //работа с FireBase
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");
        //кнопка регистрации
        btnRegister.setOnClickListener(view -> showRegistrationWindow());
        //Кнопка входа
        btnSignIn.setOnClickListener(view -> showSignInWindow());
    }

    //Окно входа
    private void showSignInWindow() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);

        LayoutInflater inflater = LayoutInflater.from(this);
        View sign_in_window = inflater.inflate(R.layout.sign_in_window, null);
        dialog.setView(sign_in_window);

        final EditText email = sign_in_window.findViewById(R.id.email_field);
        final EditText pass = sign_in_window.findViewById(R.id.pass_field);
        //закрыть окно
        dialog.setNegativeButton("Отменить", (dialogInterface, i) -> dialogInterface.dismiss());
        //Зайти
        dialog.setPositiveButton("Войти", (dialogInterface, i) -> {
            if (TextUtils.isEmpty(email.getText().toString())) {
                Snackbar.make(root, "Введите почту", Snackbar.LENGTH_SHORT).show();
                return;
            }
            if (pass.getText().toString().length() < 5) {
                Snackbar.make(root, "Пароль слишком короткий", Snackbar.LENGTH_SHORT).show();
                return;
            }
            //Авторизация пользователя
            auth.signInWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                    .addOnSuccessListener(authResult -> {   //успешная авторизация
                        finish();
                        startActivity(new Intent(this, HomeActivity.class));
                    })
                    .addOnFailureListener(e -> //не успешная авторизация
                            Snackbar.make(root, "Ошибка авторизации. " + e.getMessage(), Snackbar.LENGTH_SHORT).show());
        });
        dialog.show();
    }

    //Окно регистрации
    private void showRegistrationWindow() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);

        LayoutInflater inflater = LayoutInflater.from(this);
        View register_window = inflater.inflate(R.layout.register_window, null);
        dialog.setView(register_window);

        final EditText email = register_window.findViewById(R.id.email_field);
        final EditText pass = register_window.findViewById(R.id.pass_field);
        final EditText name = register_window.findViewById(R.id.name_field);
        final EditText phone = register_window.findViewById(R.id.phone_field);
        //закрыть окно
        dialog.setNegativeButton("Отменить", (dialogInterface, i) -> dialogInterface.dismiss());
        //Добавить пользователя
        dialog.setPositiveButton("Добавить", (dialogInterface, i) -> {
            if (TextUtils.isEmpty(email.getText().toString())) {
                Snackbar.make(root, "Введите почту", Snackbar.LENGTH_SHORT).show();
                return;
            }
            if (pass.getText().toString().length() < 5) {
                Snackbar.make(root, "Пароль слишком короткий", Snackbar.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(name.getText().toString())) {
                Snackbar.make(root, "Введите имя", Snackbar.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(phone.getText().toString())) {
                Snackbar.make(root, "Введите телефон", Snackbar.LENGTH_SHORT).show();
                return;
            }
            //Регистрация пользователя
            auth.createUserWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                    .addOnSuccessListener(authResult -> { //успешная регистрация
                        User user = new User();
                        user.setEmail(email.getText().toString());
                        user.setPass(pass.getText().toString());
                        user.setName(name.getText().toString());
                        user.setPhone(phone.getText().toString());
                        users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user)
                                .addOnSuccessListener(unused -> {
                                            Snackbar.make(root, "Пользователь добавлен!", Snackbar.LENGTH_SHORT).show();
                                            finish();
                                            startActivity(new Intent(this, HomeActivity.class));
                                        }
                                );
                    })
                    .addOnFailureListener(e ->  //не успешная регистрация
                            Snackbar.make(root, "Ошибка регистрации. " + e.getMessage(), Snackbar.LENGTH_SHORT).show());
        });
        dialog.show();
    }
}