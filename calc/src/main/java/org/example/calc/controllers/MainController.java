package org.example.calc.controllers;

import org.example.calc.model.DefaultValues;
import org.example.calc.model.Metric;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MainController {
    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("metrics", new double[] {0.7, 0.3, 0.4, 0.5, 0.7, 0.8, 0.4, 0.4, 0.4, 0.6, 0.8, 0.8, 0.5, 0.6, 0.3});
        model.addAttribute("minMetrics", new double[] {0.2, 0.25, 0.35, 0.2, 0.2, 0.25, 0.2, 0.25, 0.2, 0.15, 0.25, 0.15, 0.4, 0.15, 0.35});
        model.addAttribute("maxFunc", new int[] {3, 4, 5, 5, 3, 5, 4, 5, 5, 4, 7, 3, 6, 3, 4});

        // Возмущения
        Map<String, Map<String, Double>> disturbances = new HashMap<>();

        Map<String, Double> q1 = new HashMap<>();
        q1.put("a", 0.5);
        q1.put("b", 1.0);
        q1.put("c", 2.0);
        q1.put("d", 0.0);
        disturbances.put("q1", q1);

        Map<String, Double> q2 = new HashMap<>();
        q2.put("a", 0.3);
        q2.put("b", 0.8);
        q2.put("c", 2.0);
        q2.put("d", 0.75);
        disturbances.put("q2", q2);

        Map<String, Double> q3 = new HashMap<>();
        q3.put("a", 0.2);
        q3.put("b", 0.5);
        disturbances.put("q3", q3);

        Map<String, Double> q4 = new HashMap<>();
        q4.put("a", 0.1);
        q4.put("b", 0.3);
        q4.put("c", 0.5);
        disturbances.put("q4", q4);

        Map<String, Double> q5 = new HashMap<>();
        q5.put("a", 0.4);
        q5.put("b", 0.6);
        disturbances.put("q5", q5);

        model.addAttribute("disturbances", disturbances);

        // Дополнительные строки
        Map<String, Map<String, Integer>> additionalRows = new HashMap<>();
        for (int i = 1; i <= 98; i++) {
            Map<String, Integer> row = new HashMap<>();
            row.put("a0", i % 9 + 1);
            row.put("a1", i % 9 + 1);
            row.put("a2", i % 9 + 1);
            row.put("a3", i % 9 + 1);
            additionalRows.put("f" + i, row);
        }
        model.addAttribute("additionalRows", additionalRows);

        // Метки
        List<Map<String, String>> metricsList = new ArrayList<>();
        String[] names = {"Время испарения", "Время ликвидации", "Изменяемость", "Площадь заражения",
                "Время подхода", "Пораженные от первичного", "Пораженные от вторичного",
                "Госпитализированные", "Техника", "Количество растворов", "Количество сил",
                "Количесвто средств", "Эффективность системы", "Количество убежищ", "Опасность"};
        for (int i = 0; i < 15; i++) {
            Map<String, String> metric = new HashMap<>();
            metric.put("id", "L" + (i + 1));
            metric.put("name", names[i]);
            metricsList.add(metric);
        }
        model.addAttribute("metricsList", metricsList);

        // Действие
        model.addAttribute("action", "mode1");
        return "main";
    }
}
