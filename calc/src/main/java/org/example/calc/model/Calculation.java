package org.example.calc.model;

import java.util.*;

public class Calculation {
    private final List<Double> metrics;
    private final List<Double> minMetrics;
    private final List<Integer> Lmaxs;
    private final Map<String, Map<String, Double>> disturbances;
    private final Map<String, Map<String, Double>> additionalRows;

    public Calculation(List<Double> metrics, List<Double> minMetrics, List<Integer> Lmaxs, Map<String, Map<String, Double>> disturbances, Map<String, Map<String, Double>> additionalRows) {
        this.metrics = metrics;
        int i = 1;
        for (Double d: metrics){
            if (d <= 0){
                throw new IllegalArgumentException("L"+i + " должна быть > 0");
            }
        }
        i = 0;
        this.minMetrics = minMetrics;
        for (Double d: minMetrics){
            if (d <= 0){
                throw new IllegalArgumentException("L"+i + "(min) должна быть > 0");
            }
        }
        i=0;
        this.disturbances = disturbances;
        this.additionalRows = additionalRows;
        this.Lmaxs = Lmaxs;
        for (Integer max: Lmaxs){
            if (max <=0 ){
                throw new IllegalArgumentException("L"+i + "(max) должна быть > 0");
            }
        }

    }

    public double q1(double t) {
        Map<String, Double> dist = disturbances.get("q1");
        double res = dist.get("a") + dist.get("b") * Math.sin(dist.get("c") * t + dist.get("d"));
        if (res <= 0){
            throw new IllegalArgumentException("Опасные вещества на объекте должны быть > 0");
        }
        return dist.get("a") + dist.get("b") * Math.sin(dist.get("c") * t + dist.get("d"));
    }

    public double q2(double t) {
        Map<String, Double> dist = disturbances.get("q2");
        double res = dist.get("a") + dist.get("b") * Math.sin(dist.get("c") * t + dist.get("d"));
        if (res <= 0) {
            throw new IllegalArgumentException("Количество персонала должно быть > 0");
        }

        return res;
    }

    public double q3(double t) {
        Map<String, Double> dist = disturbances.get("q3");
        double res =  dist.get("a") * t + dist.get("b");
        if (res <= 0){
            throw new IllegalArgumentException("Скорость ветра должна быть > 0");
        }
        return res;
    }

    public double q4(double t) {
        Map<String, Double> dist = disturbances.get("q4");
        double res = dist.get("a") * t * t + dist.get("b") * t + dist.get("c");
        if (res <= 0){
            throw new IllegalArgumentException("Температура воздуха должна быть > 0");
        }
        return res;
    }

    public double q5(double t) {
        Map<String, Double> dist = disturbances.get("q5");
        double res = dist.get("a") * Math.pow(t, 0.5) + dist.get("b");
        if (res <=0) {
            throw new IllegalArgumentException("Время до начала оповещения должно быть > 0");
        }
        return res;
    }

    public double f(int index, double l) {

        Map<String, Double> row = additionalRows.get("f" + (index - 1));
        double a0 = row.get("a0");
        double a1 = row.get("a1");
        double a2 = row.get("a2");
        double a3 = row.get("a3");

        return a0 + a1 * l + a2 * l * l + a3 * l * l * l;
    }

    public double getL(int index) {
        return metrics.get(index - 1);
    }

    public double getLMin(int index){
        return minMetrics.get(index-1);
    }

    public double Lmax(int index) {
        return Lmaxs.get(index - 1);
    }

    public double l1(double t) {
        return 1 / Lmax(1) * (f(1, getL(5)) * f(2, getL(6)) * f(3, getL(7))
                * f(4, getL(10)) * f(5, getL(13))
                * f(6, getL(14)) *
                (q1(t) + q2(t)) - (q3(t)));
    }

    public double l2(double t) {
        return 1 / Lmax(2) * (f(7, getL(3)) * f(8, getL(12)) * f(9, getL(13))
                * f(10, getL(14)) * f(11, getL(15)) * (q2(t) + q5(t)) - (q4(t)));
    }

    public double l3(double t) {
        return 1 / Lmax(3) * (f(12, getL(5)) * f(13, getL(6)) *
                f(14, getL(7)) * f(15, getL(10)) * f(16, getL(13))
                * f(17, getL(14)) * f(18, getL(15)) * q2(t) - q5(t));
    }

    public double l4(double t) {
        return 1 / Lmax(4) * (f(19, getL(1)) * f(20, getL(5)) *
                f(21, getL(6)) * f(22, getL(7)) * f(23, getL(8)) *
                f(24, getL(10)) * q2(t) - f(28, getL(9)));
    }

    public double l5(double t) {
        return 1 / Lmax(5) * (f(29, getL(1)) * f(30, getL(6)) *
                f(31, getL(7)) * f(32, getL(8)) * f(33, getL(10))
                * (q1(t) + q2(t) + q3(t)) - f(36, getL(9)));
    }

    public double l6(double t) {
        return 1 / Lmax(6) * (f(37, getL(1)) * f(38, getL(3)) *
                f(39, getL(4)) * f(40, getL(7)) * f(41, getL(8)) *
                f(34, getL(11)) *
                (q1(t) + q3(t)) - f(42, getL(9)));
    }

    public double l7(double t) {
        return 1 / Lmax(7) * (f(43, getL(4)) * f(44, getL(6)) * f(25, getL(10)) *
                f(26, getL(8)) * f(45, getL(13)) * f(46, getL(14)) * (q3(t) + q4(t)) - f(47, getL(9)));
    }

    public double l8(double t) {
        return 1 / Lmax(8) * (f(48, getL(4)) * f(49, getL(6)) *
                f(50, getL(7)) * f(51, getL(11)) * f(52, getL(13)) *
                f(53, getL(15)) * q3(t) - (f(54, getL(5)) * f(55, getL(9))));
    }

    public double l9(double t) {
        return 1 / Lmax(9) * (f(56, getL(3)) * f(57, getL(6)) * f(33, getL(10)) * f(59, getL(5)) *
                f(23, getL(8)) * (q1(t) + q3(t) + q4(t)) - (f(58, getL(4)) *
                f(60, getL(7)) * f(61, getL(8)) * f(62, getL(10))));
    }

    public double l10(double t) {
        return 1 / Lmax(10) * (f(63, getL(1)) * f(64, getL(6)) // * f(65, getL(11))
                * f(66, getL(12)) * f(67, getL(13)) *
                f(68, getL(14)) * (q2(t) + q5(t)) - f(69, getL(9)));
    }

    public double l11(double t) {
        return 1 / Lmax(11) * (f(70, getL(4)) * f(71, getL(6)) * f(72, getL(8)) * f(35, getL(12)) *
                f(73, getL(10)) * f(74, getL(13)) * (q1(t) + q3(t)) - (f(75, getL(5)) * f(76, getL(7))));
    }

    public double l12(double t) {
        return 1 / Lmax(12) * (f(77, getL(2)) * f(78, getL(3)) * f(65, getL(11)) *
                f(79, getL(4)) * (q1(t) + q2(t) + q5(t)) - f(80, getL(9)));
    }

    public double l13(double t) {
        return 1 / Lmax(13) * (f(81, getL(1)) * f(82, getL(3)) *
                f(83, getL(4)) * f(84, getL(5)) * f(86, getL(10)) *
                f(87, getL(14)) * (q1(t) + q2(t)) - q4(t));
    }

    public double l14(double t) {
        return 1 / Lmax(14) * (f(88, getL(1)) * f(89, getL(7)) * f(98, getL(4)) *
                f(90, getL(10)) * f(91, getL(13)) * (q2(t) + q3(t)) - q5(t));
    }

    public double l15(double t) {
        return 1 / Lmax(15) * (f(92, getL(2)) * f(93, getL(3)) *
                f(94, getL(4)) * f(95, getL(6)) * f(96, getL(8)) * f(97, getL(9)) *
                (q1(t) + q2(t) + q5(t)) - q3(t));
    }

    public double g1(double t) {
        return -1 / Lmax(1) * (f(1, 10) * f(2, 11) * f(3, 14));
    }

    public double g2(double t) {
        return 1 / Lmax(2) * (f(4, 3) * f(5, 7) * f(6, 8) * f(7, 9) * f(8, 13) - (f(9, 10) * f(10, 11) * f(11, 14) * f(12, 15) * q1(t) + q2(t) + q3(t) + q4(t)));
    }

    public double g3(double t) {
        return 1 / Lmax(3) * (f(13, 1) - (f(14, 15) * q1(t) + q3(t) + q4(t)));
    }

    public double g4(double t) {
        return 1 / Lmax(4) * f(15, 1);
    }

    public double g5(double t) {
        return 1 / Lmax(5) * (f(16, 1) * q2(t) - q1(t));
    }

    public double g6(double t) {
        return 1 / Lmax(6) * (q2(t) - (f(17, 4) * f(18, 11) * f(19, 12) * f(20, 14) * q1(t)));
    }

    public double g7(double t) {
        return 1 / Lmax(7) * (f(21, 5) * f(22, 6) * f(23, 13) * f(24, 15) * q1(t) + q2(t) + q3(t));
    }

    public double g8(double t) {
        return 1 / Lmax(8) * (f(25, 5) * f(26, 6) * f(27, 11) * f(28, 13) * f(29, 14) * f(30, 15) * q1(t) + q2(t) + q3(t));
    }

    public double g9(double t) {
        return 1 / Lmax(9) * (f(31, 3) * f(32, 13) * q2(t) - (f(33, 10) * f(34, 11) * f(35, 14) * q1(t)));
    }

    public double g10(double t) {
        return 1 / Lmax(10) * (f(36, 3) * f(37, 9) * f(38, 15) * q1(t) + q2(t) + q3(t) + q4(t));
    }

    public double g11(double t) {
        return 1 / Lmax(11) * (f(39, 3) * f(40, 13) * f(41, 14) * q1(t) + q2(t) + q3(t) - (f(42, 15) * q4(t)));
    }

    public double g12(double t) {
        return 1 / Lmax(12) * (f(43, 11) * f(44, 13) * f(45, 14) * q1(t) + q2(t) + q3(t) - f(46, 15));
    }

    public double g13(double t) {
        return 1 / Lmax(13) * (f(47, 2) * f(48, 3) * q2(t));
    }

    public double g14(double t) {
        return 1 / Lmax(14) * (f(49, 11) * f(50, 12) * f(51, 13) * q1(t) + q2(t));
    }

    public double g15(double t) {
        return 1 / Lmax(15) * (f(52, 2) * f(53, 3) * f(54, 13) * f(55, 14) * q1(t) + q2(t));
    }

    public List<Double> getListByT(double t) {
        List<Double> res = Arrays.asList(
                //Math.min(l1(t), getLMin(1)),
                l1(t),
                l2(t),
                l3(t),
                l4(t),
                l5(t),
                l6(t),
                l7(t),
                l8(t),
                l9(t),
                l10(t),
                l11(t),
                l12(t),
                l13(t),
                l14(t),
                l15(t));
        return res;
    }

    public List<Double> getSecondListByT(double t) {
        return Arrays.asList(
                g1(t),
                g2(t),
                g3(t),
                g4(t),
                g5(t),
                g6(t),
                g7(t),
                g8(t),
                g9(t),
                g10(t),
                g11(t),
                g12(t),
                g13(t),
                g14(t),
                g15(t)
        );
    }

    public Map<Double, List<Double>> calculateMetrics(String action) {
        Map<Double, List<Double>> map = new HashMap<>();
        for (double i = 0; i <= 1; i += 0.25) {
            if (action.equals("mode1")) {
                map.put(i, getListByT(i));
            } else map.put(i, getSecondListByT(i));
        }
        if (action.equals("model1")){
            return normalizeMin(map);
        }
        return normalizeMin(map);
    }

    private Map<Double, List<Double>> normalize(Map<Double, List<Double>> map) {

        List<Double> allValues = new ArrayList<>();
        map.values().forEach(allValues::addAll);

        double minValue = allValues.stream().min(Double::compare).get();
        double maxValue = allValues.stream().max(Double::compare).get();

        if (maxValue == minValue) {
            Map<Double, List<Double>> zeroMap = new HashMap<>();
            map.forEach((key, value) -> zeroMap.put(key, new ArrayList<>(Collections.nCopies(value.size(), 0.0))));
            return zeroMap;
        }

        Map<Double, List<Double>> normalizedMap = new HashMap<>();
        map.forEach((key, value) -> {
            List<Double> normalizedList = new ArrayList<>();
            for (int i = 0; i < value.size(); i++) {
                double v = value.get(i);
                double normalizedValue = ((v - minValue) / (maxValue - minValue)) * (1 - 0.25) + 0.25;
                //normalizedValue = Math.min(normalizedValue, minMetrics.get(i));
                normalizedList.add(normalizedValue);
                System.out.println(normalizedList);
            }
            normalizedMap.put(key, normalizedList);
        });

        return new TreeMap<>(normalizedMap);
    }

    private Map<Double, List<Double>> normalizeMin(Map<Double, List<Double>> map) {

        List<Double> allValues = new ArrayList<>();
        map.values().forEach(allValues::addAll);

        double minValue = allValues.stream().min(Double::compare).get();
        double maxValue = allValues.stream().max(Double::compare).get();

        if (maxValue == minValue) {
            Map<Double, List<Double>> zeroMap = new HashMap<>();
            map.forEach((key, value) -> zeroMap.put(key, new ArrayList<>(Collections.nCopies(value.size(), 0.0))));
            return zeroMap;
        }

        Map<Double, List<Double>> normalizedMap = new HashMap<>();
        map.forEach((key, value) -> {
            List<Double> normalizedList = new ArrayList<>();
            for (int i = 0; i < value.size(); i++) {
                double v = value.get(i);
                double normalizedValue = ((v - minValue) / (maxValue - minValue)) * (1 - 0.25) + 0.25;
                normalizedValue = Math.min(normalizedValue, minMetrics.get(i));
                normalizedList.add(normalizedValue);
                System.out.println(normalizedList);
            }
            normalizedMap.put(key, normalizedList);
        });

        return new TreeMap<>(normalizedMap);
    }
}
