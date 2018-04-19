package sample;

import java.util.function.Function;

public class Computer {
    private F<Double, Double, Double> function;

    private double XInitial;
    private double XFinal;

    private double YInitial;

    private int steps;

    private double step;

    public Computer(F<Double, Double, Double> function, int steps, double YInitial, double XInitial, double XFinal) {
        this.function = function;

        this.XInitial = XInitial;
        this.XFinal = XFinal;

        this.steps = steps;
        this.step = (XFinal - XInitial) / steps;

        this.YInitial = YInitial;
    }

    /**
     * computes a set of points using Euler method
     * @return
     */
    public double[] EulerMethod() {
        double[] y = new double[steps + 1];

        double x = XInitial;
        y[0] = YInitial;

        for (int i = 1; i <= steps; i++) {
            double value = function.apply(x, y[i - 1]);

            double delta = step * value;
            x += step;
            y[i] = y[i - 1] + delta;
        }

        return y;
    }

    /**
     * computes a set of points using ImprovedEuler method
     * @return
     */
    public double[] UpgradedEulerMethod() {
        double[] y = new double[steps + 1];

        double x = XInitial;
        y[0] = YInitial;

        for (int i = 1; i <= steps; i++) {
            double value = function.apply(x, y[i - 1]);

            double delta = step * function.apply(x + step / 2, y[i - 1] + step * value / 2);
            x += step;
            y[i] = y[i - 1] + delta;
        }

        return y;
    }

    /**
     * computes a set of points using RungeKutta method
     * @return
     */
    public double[] RungeKuttaMethod() {
        double[] y = new double[steps + 1];

        double x = XInitial;
        y[0] = YInitial;

        for (int i = 1; i <= steps; i++) {
            double k1 = function.apply(x, y[i - 1]);
            double k2 = function.apply(x + step / 2, y[i - 1] + step * k1 / 2);
            double k3 = function.apply(x + step / 2, y[i - 1] + step * k2 / 2);
            double k4 = function.apply(x + step, y[i - 1] + step * k3);

            double delta = step * (k1 + 2 * k2 + 2 * k3 + k4) / 6;
            x += step;
            y[i] = y[i - 1] + delta;
        }

        return y;
    }

    /**
     * computes a set of points for a function
     * @param function
     * @return
     */
    public double[] ComputeExactFunction(Function<Double, Double> function) {
        double[] y = new double[steps + 1];

        double x = XInitial;
        y[0] = YInitial;

        for (int i = 1; i <= steps; i++) {
            x += step;
            y[i] = function.apply(x);
        }

        return y;
    }
}
