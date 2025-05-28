package org.example.calc.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.calc.model.Calculation;
import org.example.calc.model.CustomSpiderWebPlot;
import org.example.calc.requests.RadarChartRequest;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.CategoryTextAnnotation;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class RadarChartController {

    @PostMapping("/radar-chart")
    public ResponseEntity<byte[]> getRadarChart(
            @RequestBody RadarChartRequest params
    ) throws IOException {

        String action = params.getAction();
        System.out.println("action: " + action);
        List<Double> metrics = params.getMetrics();
        List<String> metricsName = params.getMetricsName();
        List<Double> minMetrics = params.getMinMetrics();
        List<Integer> maxFuncs = params.getMaxFuncs();
        Map<String, Map<String, Double>> disturbances = params.getDisturbances();
        Map<String, Map<String, Double>> additionalRows = params.getAdditionalRows();



        // Проверка, что длины массивов совпадают
        if (metrics.size() != 15 || minMetrics.size() != 15) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);  // Если длины не равны 15, возвращаем ошибку
        }
        Map<Double, List<Double>> calculatedMetrics;
        Calculation calc;
        try {
           calc = new Calculation(metrics, minMetrics, maxFuncs, disturbances, additionalRows);
           calculatedMetrics = calc.calculateMetrics(action);
        } catch (IllegalArgumentException e){
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> result = new HashMap<String, String>(){
                {
                    this.put("errorMsg", e.getMessage());
                }
            };
            return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body(objectMapper.writeValueAsBytes(result));
        }


        System.out.println(calculatedMetrics);

        // Каждая диаграмма будет занимать 600x600 пикселей
        int chartWidth = 600;
        int chartHeight = 600;

        // Расчет итоговой высоты изображения: одна диаграмма под другой
        int totalHeight = chartHeight * (calculatedMetrics.size() + 1); // +1 для линейного графика
        BufferedImage combinedImage = new BufferedImage(chartWidth, totalHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = combinedImage.createGraphics();

        int y = 0;

        // Генерация каждой диаграммы и рисование ее на итоговом изображении
        for (Map.Entry<Double, List<Double>> entry : calculatedMetrics.entrySet()) {
            Double key = entry.getKey();
            List<Double> values = entry.getValue();

            // Создание набора данных для каждой диаграммы
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();

            for (int i = 0; i < values.size(); i++) {
                dataset.addValue(values.get(i), "Metrics", "L" + (i + 1));
                dataset.addValue(minMetrics.get(i), "Minimums", "L" + (i + 1));
            }

            // Создание паутинной диаграммы для текущего ключа
            CustomSpiderWebPlot plot = new CustomSpiderWebPlot(dataset);
            plot.setMaxValue(1.0);  // Устанавливаем максимум для шкалы

            plot.setSeriesPaint(0, Color.BLUE); // Цвет для метрик (основные данные)
            plot.setSeriesPaint(1, Color.RED);  // Цвет для минимальных значений



            JFreeChart chart = new JFreeChart("" + key, JFreeChart.DEFAULT_TITLE_FONT, plot, false);
            chart.setBackgroundPaint(Color.white);

            // Создание изображения диаграммы
            BufferedImage chartImage = chart.createBufferedImage(chartWidth, chartHeight);
            g2d.drawImage(chartImage, 0, y, null);
            y += chartHeight;  // Смещаемся на следующую строку для новой диаграммы
        }

        // Создаем линейный график для значений Li от t
        DefaultCategoryDataset lineDataset = new DefaultCategoryDataset();
        double[] tValues = {0, 0.25, 0.5, 0.75, 1};  // Значения t

        // Заполняем набор данных для линейного графика
        for (double t : tValues) {
            List<Double> liList = calculatedMetrics.get(t);
            if (liList != null) {
                for (int i = 0; i < 15; i++) { // Используем i от 0 до 14 для 15 переменных
                    if (i < liList.size()) { // Проверяем, есть ли значение для этого индекса
                        double liValue = liList.get(i);
                        lineDataset.addValue(liValue, "L" + (i + 1), String.valueOf(t));
                    }
                }
            }
        }


        // Создание линейного графика
        JFreeChart lineChart = ChartFactory.createLineChart(
                "Зависимость Li от t", // заголовок
                "t", // метка оси X
                "Li", // метка оси Y
                lineDataset, // набор данных
                PlotOrientation.VERTICAL, // ориентация графика
                true,
                true,
                false
        );

        // Настройка линейного графика
        lineChart.setBackgroundPaint(Color.white);

        // Настройка рендера для кастомизации линий
        CategoryPlot plot = lineChart.getCategoryPlot();
        LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();

        // Устанавливаем разные цвета для каждой линии
        for (int i = 0; i < 15; i++) {
            renderer.setSeriesPaint(i, Color.getHSBColor((float) i / 15, 1.0f, 1.0f)); // Разные цвета для разных линий
        }

        // Добавление названий линий над концами
        for (int i = 0; i < 15; i++) {
            // Получаем последнее значение t и соответствующее значение y для каждой линии
            Comparable<?> tValueEnd = String.valueOf(tValues[tValues.length - 1]); // Последнее значение t как строка (Comparable)
            Number yValueEnd = lineDataset.getValue("L" + (i + 1), tValueEnd); // Значение y в последней точке

            // Проверяем, что yValueEnd не null
            if (yValueEnd != null) {
                // Смещаем аннотацию вверх на 5% от значения y
                double yOffset = yValueEnd.doubleValue() * 1.05;

                // Добавляем текстовое аннотирование над точкой (чуть выше последней точки линии)
                CategoryTextAnnotation annotation = new CategoryTextAnnotation("L" + (i + 1), tValueEnd, yOffset);
                annotation.setFont(new Font("SansSerif", Font.BOLD, 12));
                annotation.setPaint(Color.getHSBColor((float) i / 15, 1.0f, 0.7f)); // Цвет текста соответствует линии
                plot.addAnnotation(annotation);
            }
        }

        BufferedImage lineChartImage = lineChart.createBufferedImage(chartWidth, chartHeight);
        g2d.drawImage(lineChartImage, 0, y, null);

        g2d.dispose();

        // Конвертация итогового изображения в байты
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(combinedImage, "png", baos);
        byte[] imageBytes = baos.toByteArray();

        // Установка заголовков для ответа
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.IMAGE_PNG);
        headers.setContentLength(imageBytes.length);

        // Возвращаем итоговое изображение
        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }

}

