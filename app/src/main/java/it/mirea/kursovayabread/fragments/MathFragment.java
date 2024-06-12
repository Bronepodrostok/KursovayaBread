package it.mirea.kursovayabread.fragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import it.mirea.kursovayabread.R;
import it.mirea.kursovayabread.mathModel.ProductionOptimization;

public class MathFragment extends BaseFragment {

    private TextView oeeTextView;
    private TextView materialsTextView;
    private TextView demandTextView;
    private TextView optimizationAdviceTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_math, container, false);

        oeeTextView = view.findViewById(R.id.oeeTextView);
        materialsTextView = view.findViewById(R.id.materialsTextView);
        demandTextView = view.findViewById(R.id.demandTextView);
        optimizationAdviceTextView = view.findViewById(R.id.optimizationAdviceTextView);

        calculateAndDisplayMetrics();

        return view;
    }

    private void calculateAndDisplayMetrics() {
        // Пример данных
        double availableTime = 100;
        double downtime = 10;
        double completedTasks = 80;
        double plannedTasks = 100;
        double totalProduced = 90;
        double defectiveProduced = 5;
        int orders = 50;
        int materialPerOrder = 2;
        double[] historicalData = {30, 40, 50, 60, 70};
        // Комплексная оптимизация плана производства
        ProductionOptimization.optimizeProductionPlan(
                availableTime, downtime, completedTasks, plannedTasks,
                totalProduced, defectiveProduced, orders, materialPerOrder, historicalData,
                oeeTextView, materialsTextView, demandTextView, optimizationAdviceTextView);

    }
}