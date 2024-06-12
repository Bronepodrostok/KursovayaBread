/*package it.mirea.kursovayabread;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

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
import it.mirea.kursovayabread.models.UserRole;

public class AdminActivity extends AppCompatActivity {
    FirebaseAuth auth;//авторизация
    FirebaseDatabase db;//база данных
    DatabaseReference users;//Колонка пользователей в бд

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Menu menu;
    Button btnCode;
    RelativeLayout root;

    User user;
    Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_admin);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        menu = navigationView.getMenu();
        toolbar = findViewById(R.id.navigation_bar);
        btnCode = findViewById(R.id.btnCode);
        root = findViewById(R.id.admin_activity);

        setActionBar();
        getUserInfo();
        btnCode.setOnClickListener(v -> adminCode());
    }

    private void setActionBar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Режим разработчика");
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
                user = snapshot.child(auth.getCurrentUser().getUid()).getValue(User.class);
                UserRole userRole = user.getRole();
                boolean engineerVisible = false;
                boolean managerVisible = false;
                boolean adminVisible = false;

                if (userRole.equals(UserRole.ENGINEER)) {
                    engineerVisible = true;
                } else if (userRole.equals(UserRole.MANAGER)) {
                    managerVisible = true;
                } else if (userRole.equals(UserRole.ADMIN)) {
                    engineerVisible = true;
                    managerVisible = true;
                    adminVisible = true;
                }
                menu.findItem(R.id.engineer).setVisible(engineerVisible);
                menu.findItem(R.id.manager).setVisible(managerVisible);
                menu.findItem(R.id.admin).setVisible(adminVisible);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
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
            products.child(product.getId())
                    .setValue(product)
                    .addOnSuccessListener(unused -> {
                                Snackbar.make(root, "Продукт добавлен!", Snackbar.LENGTH_SHORT).show();
                            }
                    );
        });
        dialog.show();
    }

    private void adminCode() {
        FirebaseDatabase db;//база данных
        DatabaseReference codes;
        db = FirebaseDatabase.getInstance();
        codes = db.getReference("Codes");

        final EditText code = findViewById(R.id.code_field);
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
                        UserRole userRole = user.getRole();
                        if (userRole.equals(UserRole.USER)) {
                            user.setRole(UserRole.ENGINEER);
                        } else if (userRole.equals(UserRole.ENGINEER)){
                            user.setRole(UserRole.MANAGER);
                        } else {
                            user.setRole(UserRole.ADMIN);
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}*/