package aowe.model;

import org.opencv.core.Mat;

/**
 * Created by Igor Farszky on 1.7.2017..
 */
public class Sight {

    private int x;
    private int y;
    private Mat mat;
    private double precision;

    public Sight(int x, int y, Mat mat, double precision) {
        this.x = x;
        this.y = y;
        this.mat = mat;
        this.precision = precision;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Mat getMat() {
        return mat;
    }

    public void setMat(Mat mat) {
        this.mat = mat;
    }

    public double getPrecision() {
        return precision;
    }

    public void setPrecision(double precision) {
        this.precision = precision;
    }
}

