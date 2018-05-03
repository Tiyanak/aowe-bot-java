package aowe.utils;

import aowe.model.Template;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Igor Farszky on 16.2.2018..
 */
public class CV {

    private static Robot robot;

    static {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public static List<Template> matchingTemplate(Mat img, Mat template, boolean singleResult, double precision) {

        Mat result = new Mat(img.rows(), img.cols(), img.type());

        Imgproc.matchTemplate(img, template, result, Imgproc.TM_CCOEFF_NORMED);

        if (singleResult) {
            Core.normalize(result, result, 0, 1, Core.NORM_MINMAX, -1, new Mat());
        }

        Core.MinMaxLocResult mmr;
        Point matchLoc;

        List<Template> templates = new ArrayList<>();

        while (true) {
            mmr = Core.minMaxLoc(result);
            matchLoc = mmr.maxLoc;
            if (mmr.maxVal >= precision) {

                templates.add(new Template((int) matchLoc.x + template.cols() / 2, (int) matchLoc.y + template.rows() / 2,
                        template.cols(), template.rows()));

                if (singleResult) {
                    break;
                }

                Imgproc.rectangle(result, matchLoc,
                        new Point(matchLoc.x + template.cols(), matchLoc.y + template.rows()),
                        new Scalar(0, 255, 0), -1);

            } else {
                break; //No more results within tolerance, break search
            }
        }

        return templates;


    }

    public static Mat ScreenShot() {

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        return img2Mat(robot.createScreenCapture(new Rectangle(dim)));

    }

    public static void saveImage(Mat img, String imgName) {

        Imgcodecs.imwrite(Util.DESKTOP_PATH + imgName + Util.PNG_EXT, img);

    }

    public static void displayImage(BufferedImage img2) {

        JFrame frame = new JFrame();
        frame.getContentPane().add(new JLabel(new ImageIcon(img2.getScaledInstance(500, 500, Image.SCALE_SMOOTH))));
        frame.setSize(500, 500);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public static BufferedImage createBufferedImage(Mat mat) {
        BufferedImage image = new BufferedImage(mat.width(), mat.height(), BufferedImage.TYPE_3BYTE_BGR);
        mat.get(0, 0, ((DataBufferByte) image.getRaster().getDataBuffer()).getData());

        return image;
    }

    private static Mat img2Mat(BufferedImage in) {
        Mat out;
        byte[] data;
        int r, g, b;

        if (in.getType() == BufferedImage.TYPE_INT_RGB) {
            out = new Mat(in.getHeight(), in.getWidth(), CvType.CV_8UC3);
            data = new byte[in.getWidth() * in.getHeight() * (int) out.elemSize()];
            int[] dataBuff = in.getRGB(0, 0, in.getWidth(), in.getHeight(), null, 0, in.getWidth());
            for (int i = 0; i < dataBuff.length; i++) {
                data[i * 3] = (byte) ((dataBuff[i] >> 0) & 0xFF);
                data[i * 3 + 1] = (byte) ((dataBuff[i] >> 8) & 0xFF);
                data[i * 3 + 2] = (byte) ((dataBuff[i] >> 16) & 0xFF);
            }
        } else {
            out = new Mat(in.getHeight(), in.getWidth(), CvType.CV_8UC1);
            data = new byte[in.getWidth() * in.getHeight() * (int) out.elemSize()];
            int[] dataBuff = in.getRGB(0, 0, in.getWidth(), in.getHeight(), null, 0, in.getWidth());
            for (int i = 0; i < dataBuff.length; i++) {
                r = (byte) ((dataBuff[i] >> 0) & 0xFF);
                g = (byte) ((dataBuff[i] >> 8) & 0xFF);
                b = (byte) ((dataBuff[i] >> 16) & 0xFF);
                data[i] = (byte) ((0.21 * r) + (0.71 * g) + (0.07 * b));
            }
        }
        out.put(0, 0, data);
        return out;
    }

}
