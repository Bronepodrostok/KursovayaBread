package it.mirea.kursovayabread;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import it.mirea.kursovayabread.fragments.AdminFragment;
import it.mirea.kursovayabread.fragments.BaseFragment;
import it.mirea.kursovayabread.fragments.HomeFragment;
import it.mirea.kursovayabread.fragments.ManagmentFragment;
import it.mirea.kursovayabread.fragments.MathFragment;
import it.mirea.kursovayabread.fragments.ProductionFragment;
import it.mirea.kursovayabread.models.Product;
import it.mirea.kursovayabread.models.User;
import it.mirea.kursovayabread.models.UserRole;

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

        setActionBar();
        getUserInfo();
        LinearLayout taskListLayout = findViewById(R.id.task_list_layout);
        addTaskToLayout(taskListLayout, "1. Микросхема", "до 15.06.2024");
    }

    private void setActionBar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Домашняя страница");
        actionBar.setHomeAsUpIndicator(R.drawable.ic_home);

        navigationView.setNavigationItemSelectedListener(item -> {
            BaseFragment fragment = null;
            switch (item.getItemId()) {
                case R.id.home:
                    fragment = new HomeFragment();
                    actionBar.setTitle("Домашняя страница");
                    getUserInfo();
                    break;
                case R.id.production: {
                    fragment = new ProductionFragment();
                    actionBar.setTitle("Список продукции");
                    break;
                }
                case R.id.addProduction: {
                    showAddProductWindow();
                    break;
                }
                case R.id.managment: {
                    fragment = new ManagmentFragment();
                    actionBar.setTitle("Учёт продукции");
                    break;
                }
                case R.id.math: {
                    fragment = new MathFragment();
                    actionBar.setTitle("Математическая модель");
                    break;
                }
                case R.id.admin_mode: {
                    fragment = new AdminFragment();
                    actionBar.setTitle("Панель администратора");
                    break;
                }
                case R.id.codes: {
                    showCodesWindow();
                    break;
                }
                case R.id.exit: {
                    finish();
                    startActivity(new Intent(this, MainActivity.class));
                    return true;
                }
            }
            if (fragment != null) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }

            drawerLayout.closeDrawer(GravityCompat.START);
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
                id.setText("id: " + auth.getCurrentUser().getUid());
                email.setText("Почта: " + user.getEmail());
                name.setText("Имя: " + user.getName());
                phone.setText("Телефон: " + user.getPhone());
                UserRole userRole = user.getRole();
                String roleText;
                boolean engineerVisible = false;
                boolean managerVisible = false;
                boolean adminVisible = false;

                if (userRole.equals(UserRole.USER)) {
                    roleText = "Должность: работник";
                } else if (userRole.equals(UserRole.ENGINEER)) {
                    roleText = "Должность: инженер";
                    engineerVisible = true;
                } else if (userRole.equals(UserRole.MANAGER)) {
                    roleText = "Должность: менеджер";
                    managerVisible = true;
                } else {
                    roleText = "Должность: администратор";
                    engineerVisible = true;
                    managerVisible = true;
                    adminVisible = true;
                }
                role.setText(roleText);
                menu.findItem(R.id.engineer).setVisible(engineerVisible);
                menu.findItem(R.id.manager).setVisible(managerVisible);
                menu.findItem(R.id.admin).setVisible(adminVisible);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void addTaskToLayout(LinearLayout layout, String taskName, String dueDate) {
        LinearLayout taskLayout = new LinearLayout(this);
        taskLayout.setOrientation(LinearLayout.HORIZONTAL);

        TextView taskNameView = new TextView(this);
        taskNameView.setText(taskName);
        taskNameView.setTextSize(18);
        taskNameView.setTextColor(getResources().getColor(R.color.text_color));
        LinearLayout.LayoutParams taskNameParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        taskLayout.addView(taskNameView, taskNameParams);

        TextView dueDateView = new TextView(this);
        dueDateView.setText(dueDate);
        dueDateView.setTextSize(18);
        dueDateView.setTextColor(getResources().getColor(R.color.text_color));
        taskLayout.addView(dueDateView);

        CheckBox checkBox = new CheckBox(this);
        checkBox.setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle("Подтверждение")
                .setMessage("Вы уверены, что хотите отметить эту задачу как выполненную?")
                .setPositiveButton("Да", (dialog, which) -> {
                    Toast.makeText(HomeActivity.this, "Задача выполнена", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Нет", (dialog, which) -> checkBox.setChecked(false))
                .show());
        taskLayout.addView(checkBox);

        layout.addView(taskLayout);
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