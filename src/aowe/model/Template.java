package aowe.model;

import org.opencv.core.Mat;

/**
 * Created by Igor Farszky on 16.2.2018..
 */
public class Template {

    private int x;
    private int y;
    private int width;
    private int height;
    private Mat mat;

    public Template() {}

    public Template(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Template template = (Template) o;

        return Math.abs(x - template.getX()) <= (width / 2) && Math.abs(y - template.getY()) <= (height / 2);

    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    public boolean neigborWith(Template t) {
        return Math.abs(x - t.getX()) <= 2 * width && Math.abs(y - t.getY()) <= 2 * height;
    }

    @Override
    public String toString() {
        return "Template{" +
                "x=" + x +
                ", y=" + y +
                '}';
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

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
