package aowe.helper;

import aowe.model.Battle;
import aowe.model.Sight;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Igor Farszky on 1.7.2017..
 */
public class CV {

    public static List<Battle> matchingHydraTemplates(Mat img, Mat template, boolean single, boolean drawImage, boolean normalize, double precision) {

        int result_cols = img.cols() - img.cols() + 1;
        int result_rows = img.rows() - img.rows() + 1;
        Mat result = new Mat(result_rows, result_cols, CvType.CV_32FC1);

        // Do the Matching and Normalize
        Imgproc.matchTemplate(img, template, result, Imgproc.TM_CCOEFF_NORMED);
        if (normalize){
            Core.normalize(result, result, 0, 1, Core.NORM_MINMAX, -1, new Mat());
        }

        // / Localizing the best match with minMaxLoc
        Core.MinMaxLocResult mmr;
        Point matchLoc;

        List<Battle> battles = new ArrayList<>();

        while (true) {
            mmr = Core.minMaxLoc(result);
            matchLoc = mmr.maxLoc;
            if (mmr.maxVal >= precision) {

                if (drawImage) {
                    Imgproc.rectangle(img, matchLoc,
                            new Point(matchLoc.x + template.cols(), matchLoc.y + template.rows()),
                            new Scalar(0, 255, 0));
                }

                battles.add(new Battle((int) matchLoc.x + template.cols() / 2, (int) matchLoc.y + template.rows() / 2));

                if (single) {
                    break;
                }

                Imgproc.rectangle(result, matchLoc,
                        new Point(matchLoc.x + template.cols(), matchLoc.y + template.rows()),
                        new Scalar(0, 255, 0), -1);

            } else {
                break; //No more results within tolerance, break search
            }
        }

        return battles;

    }

    public static List<Sight> matchingSightTemplates(Mat img, Mat template, int numItems, boolean drawImage) {

        int result_cols = img.cols() - img.cols() + 1;
        int result_rows = img.rows() - img.rows() + 1;
        Mat result = new Mat(result_rows, result_cols, CvType.CV_32FC1);

        // Do the Matching and Normalize
        Imgproc.matchTemplate(img, template, result, Imgproc.TM_CCOEFF_NORMED);
        Core.normalize(result, result, 0, 1, Core.NORM_MINMAX, -1, new Mat());

        // / Localizing the best match with minMaxLoc
        Core.MinMaxLocResult mmr;
        Point matchLoc;

        List<Sight> sights = new ArrayList<>();


        while (true) {

            if (numItems <= 0){
                break;
            }

            mmr = Core.minMaxLoc(result);
            matchLoc = mmr.maxLoc;
            if (mmr.maxVal >= Constants.MATCHING_PRECISION) {

                sights.add(new Sight((int) matchLoc.x + template.cols() / 2, (int) matchLoc.y + template.rows() / 2,
                        img.submat((int)matchLoc.y, (int)matchLoc.y+template.rows(), (int)matchLoc.x, (int)matchLoc.x+template.cols()).clone(), 0.0));
                if (drawImage) {
                    Imgproc.rectangle(img, matchLoc,
                            new Point(matchLoc.x + template.cols(), matchLoc.y + template.rows()),
                            new Scalar(0, 255, 0), -1);
                }
                Imgproc.rectangle(result, matchLoc,
                        new Point(matchLoc.x + template.cols(), matchLoc.y + template.rows()),
                        new Scalar(0, 255, 0), -1);

                numItems--;
            }
        }

        return sights;

    }

}
