package org.example.calc.model;

import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.SpiderWebPlot;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.event.*;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.PlotState;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;


public class CustomSpiderWebPlot extends SpiderWebPlot {

    public CustomSpiderWebPlot(CategoryDataset dataset) {
        super(dataset);
        // Дополнительные настройки, если нужно
    }

    @Override
    public void draw(Graphics2D g2, Rectangle2D area, Point2D anchor,
                     PlotState parentState, PlotRenderingInfo info) {
        // Вызов стандартной отрисовки паутинной диаграммы
        super.draw(g2, area, anchor, parentState, info);

        // Добавление кастомного рендера
        System.out.println("custom draw()");
    }

    @Override
    public String getPlotType() {
        return "Custom Spider Web Plot";
    }

    @Override
    public void drawBackground(Graphics2D g2, Rectangle2D area) {
        super.drawBackground(g2, area);

        double centerX = area.getX() + area.getWidth() / 2;
        double centerY = area.getY() + area.getHeight() / 2;

        Graphics2D g2d = (Graphics2D) g2.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Stroke stroke = new BasicStroke(1.0f);
        g2d.setStroke(stroke);
        g2d.setColor(Color.LIGHT_GRAY);

        double radius = Math.min(area.getWidth(), area.getHeight()) / 2;
        int steps = 5;

        g2d.setFont(new Font("SansSerif", Font.PLAIN, 10));
        g2d.setColor(Color.BLACK); // 🔹 Цвет текста — черный

        for (int i = 1; i <= steps; i++) {
            double r = (radius * i) / steps;

            // Рисуем окружность
            Ellipse2D circle = new Ellipse2D.Double(centerX - r, centerY - r, r * 2, r * 2);
            if (i < steps){
                g2d.draw(circle);
            }


            // Подпись
            double value = i * 1.0 / steps;
            String label = String.format("%.1f", value);

            // Позиция — вдоль вертикального луча вверх
            double angle = -Math.PI / 2;
            double dx = r * 0.75 * Math.cos(angle) + 12.5f;
            double dy = r * 0.75 * Math.sin(angle);

            float labelX = (float) (centerX + dx - g2d.getFontMetrics().stringWidth(label) / 2.0);
            float labelY = (float) (centerY + dy + g2d.getFontMetrics().getHeight() / 3.0);

            AffineTransform original = g2d.getTransform();
            g2d.translate(labelX, labelY);
            g2d.rotate(0); // 90° по часовой стрелке
            g2d.drawString(label, 0, 0);
            g2d.setTransform(original);
        }

        g2d.dispose();
    }


}

