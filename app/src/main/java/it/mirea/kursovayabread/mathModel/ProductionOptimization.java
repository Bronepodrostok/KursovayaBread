package it.mirea.kursovayabread.mathModel;

import android.widget.TextView;

import org.apache.commons.math3.stat.regression.SimpleRegression;
public class ProductionOptimization {

    // Подсчет производственных метрик и OEE
    public static double calculateOEE(double availableTime, double downtime, double completedTasks, double plannedTasks, double totalProduced, double defectiveProduced) {
        double availability = (availableTime - downtime) / availableTime;
        double performance = completedTasks / plannedTasks;
        double quality = (totalProduced - defectiveProduced) / totalProduced;
        return availability * performance * quality;
    }
    // Расчет потребности в материалах
    public static int calculateRequiredMaterials(int orders, int materialPerOrder) {
        return orders * materialPerOrder;
    }

    // Прогнозирование спроса
    public static double forecastDemand(double[] historicalData) {
        SimpleRegression regression = new SimpleRegression();
        for (int i = 0; i < historicalData.length; i++) {
            regression.addData(i, historicalData[i]);
        }
        return regression.predict(historicalData.length);
    }

    // Комплексная функция оптимизации
    public static void optimizeProductionPlan(double availableTime, double downtime, double completedTasks, double plannedTasks, double totalProduced, double defectiveProduced, int orders, int materialPerOrder, double[] historicalData, TextView oeeTextView, TextView materialsTextView, TextView demandTextView, TextView optimizationAdviceTextView) {
        double oee = calculateOEE(availableTime, downtime, completedTasks, plannedTasks, totalProduced, defectiveProduced);
        int requiredMaterials = calculateRequiredMaterials(orders, materialPerOrder);
        double forecastedDemand = forecastDemand(historicalData);

        oeeTextView.setText("Подсчет эффективности оборудования (ЭФ): " + oee*100 + "%");
        materialsTextView.setText("Расчет потребности в материалах (ПМ): " + requiredMaterials+ " шт.");
        demandTextView.setText("Прогнозирование будущего спроса: " + forecastedDemand+ " шт.");

        StringBuilder advice = new StringBuilder();
        if (oee < 0.75) {
            advice.append("I Необходимо улучшить эффективность оборудования.\n");
        } else {
            advice.append("I Эффективность оборудования на высоком уровне.\n");
        }

        // Расчет потребности в материалах
        if (requiredMaterials > forecastedDemand + 30) {
            advice.append("II Проверьте наличие избыточных материалов.\n");
        } else if (requiredMaterials < forecastedDemand) {
            advice.append("II Планируйте закупку дополнительных материалов.\n");
        } else {
            advice.append("II Запасы материалов соответствуют прогнозируемому спросу.\n");
        }

        // Прогнозирование спроса
        if (forecastedDemand > totalProduced) {
            advice.append("III Прогнозируемый спрос превышает общее произведенное количество товаров. Рассмотрите возможность увеличения производства.\n");
        } else if (forecastedDemand < totalProduced) {
            advice.append("III Прогнозируемый спрос ниже общего произведенного количества товаров. Рассмотрите возможность сокращения производства или реализацию мер для стимулирования спроса.\n");
        } else {
            advice.append("III Прогнозируемый спрос соответствует общему произведенному количеству товаров.\n");
        }

        optimizationAdviceTextView.setText(advice.toString());
    }
}