package it.mirea.kursovayabread;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import it.mirea.kursovayabread.models.Product;
import it.mirea.kursovayabread.models.User;

public class HomeActivity extends AppCompatActivity {
    FirebaseAuth auth;//авторизация
    FirebaseDatabase db;//база данных
    DatabaseReference users;//Колонка пользователей в бд

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Menu menu;
    Toolbar toolbar;
    RelativeLayout root;
    User user;
    Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        menu = navigationView.getMenu();
        toolbar = findViewById(R.id.navigation_bar);
        root = findViewById(R.id.home_activity);
        getUserInfo();
    }

    private void setActionBar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Домашняя страница");
        actionBar.setHomeAsUpIndicator(R.drawable.ic_home);

        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    finish();
                    startActivity(new Intent(this, HomeActivity.class));
                    return true;
                case R.id.production: {
                    finish();
                    startActivity(new Intent(this, ProductionActivity.class));
                    return true;
                }
                case R.id.addProduction: {
                    showAddProductWindow();
                    return true;
                }
                case R.id.managment: {
                    finish();
                    startActivity(new Intent(this, ManagmentActivity.class));
                    return true;
                }
                case R.id.admin_mode: {
                    finish();
                    startActivity(new Intent(this, AdminActivity.class));
                    return true;
                }
                case R.id.codes: {
                    showCodesWindow();
                    return true;
                }
                case R.id.exit: {
                    finish();
                    startActivity(new Intent(this, MainActivity.class));
                    return true;
                }
            }
            return true;
        });
    }

    private void getUserInfo() {
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final TextView email = root.findViewById(R.id.home_email);
                final TextView id = root.findViewById(R.id.home_id);
                final TextView name = root.findViewById(R.id.home_name);
                final TextView phone = root.findViewById(R.id.home_phone);
                final TextView role = root.findViewById(R.id.home_role);

                user = snapshot.child(auth.getCurrentUser().getUid()).getValue(User.class);
                id.setText(auth.getCurrentUser().getUid());
                email.setText(user.getEmail());
                name.setText(user.getName());
                phone.setText(user.getPhone());
                if (user.isUser()) {
                    role.setText("Работник");
                    menu.findItem(R.id.director).setVisible(false);
                    menu.findItem(R.id.admin).setVisible(false);
                } else if (user.isDirector()) {
                    role.setText("Директор");
                    menu.findItem(R.id.director).setVisible(true);
                    menu.findItem(R.id.admin).setVisible(false);
                } else {
                    role.setText("Администратор");
                    menu.findItem(R.id.director).setVisible(true);
                    menu.findItem(R.id.admin).setVisible(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    private void showCodesWindow() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);

        LayoutInflater inflater = LayoutInflater.from(this);
        View codes_window = inflater.inflate(R.layout.codes_window, null);
        dialog.setView(codes_window);
        DatabaseReference codes;
        db = FirebaseDatabase.getInstance();
        codes = db.getReference("Codes");

        final EditText id = codes_window.findViewById(R.id.rights_field);
        dialog.setNegativeButton("Отменить", (dialogInterface, i) -> dialogInterface.dismiss());
        dialog.setPositiveButton("Отправить", (dialogInterface, i) -> {
            if (TextUtils.isEmpty(id.getText().toString())) {
                Snackbar.make(root, "Введите код", Snackbar.LENGTH_SHORT).show();
                return;
            }
            codes.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child(id.getText().toString()).getValue() != null) {
                        if (user.isUser()) {
                            user.setDirector();
                        } else {
                            user.setAdmin();
                        }
                        users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user)
                                .addOnSuccessListener(unused -> {
                                    Snackbar.make(root, "Права получены!", Snackbar.LENGTH_SHORT).show();
                                });
                        codes.child(snapshot.child(id.getText().toString()).getValue().toString())
                                .removeValue();
                    } else Snackbar.make(root, "Код неверный!", Snackbar.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        });
        dialog.show();
    }

    private void showAddProductWindow() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);

        LayoutInflater inflater = LayoutInflater.from(this);
        View product_window = inflater.inflate(R.layout.product_window, null);
        dialog.setView(product_window);
        DatabaseReference products;
        db = FirebaseDatabase.getInstance();
        products = db.getReference("Products");

        final EditText id = product_window.findViewById(R.id.id_field);
        final EditText name = product_window.findViewById(R.id.name_product_field);
        final EditText price = product_window.findViewById(R.id.price_product_field);
        dialog.setNegativeButton("Отменить", (dialogInterface, i) -> dialogInterface.dismiss());
        dialog.setPositiveButton("Отправить", (dialogInterface, i) -> {
            if (TextUtils.isEmpty(id.getText().toString())) {
                Snackbar.make(root, "Введите id", Snackbar.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(name.getText().toString())) {
                Snackbar.make(root, "Введите нзвание", Snackbar.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(price.getText().toString())) {
                Snackbar.make(root, "Введите цену", Snackbar.LENGTH_SHORT).show();
                return;
            }
            product = new Product(id.getText().toString(), name.getText().toString(), price.getText().toString());
//            products.child(product.getId())
//                    .setValue(product)
//                    .addOnSuccessListener(unused -> {
//                                Snackbar.make(root, "Продукт добавлен!", Snackbar.LENGTH_SHORT).show();
//                            }
//                    );
            products.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child(id.getText().toString()).getValue() != null) {
                        Snackbar.make(root, "Такой id уже существует", Snackbar.LENGTH_SHORT).show();
                    } else products.child(product.getId())
                            .setValue(product)
                            .addOnSuccessListener(unused -> {
                                        Snackbar.make(root, "Продукт добавлен!", Snackbar.LENGTH_SHORT).show();
                                    }
                            );
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        });
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}