package aowe.utils;

import aowe.model.Template;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Igor Farszky on 16.2.2018..
 */
public class GameUtil {

    private static Map<String, Mat> templates = new HashMap<>();

    static void initTemplates() {

        for (String hydra_temp : Util.ASSETS) {
            String path = Util.AOWE_ASSETS + hydra_temp + Util.PNG_EXT;
            try {
                templates.put(hydra_temp, Imgcodecs.imread(path));
            } catch (Exception e) {}
        }

    }

    public static List<Template> removeSameTemplates(List<Template> templates) {

        List<Template> clearedTemplates = new ArrayList<>();

        for (int i = 0; i < templates.size() - 1; i++) {
            if (!clearedTemplates.contains(templates.get(i))) {
                clearedTemplates.add(templates.get(i));
            }
        }

        return clearedTemplates;

    }

    public static Template findClosestTemplate(List<Template> templates, Template me) {

        Template closestTemplate = new Template();
        int min = 9999;
        for (Template t : templates) {
            int distance = cityBlockDistance(me, t);
            if (distance < min) {
                min = distance;
                closestTemplate = t;
            }
        }

        return closestTemplate;

    }

    public static int cityBlockDistance(Template me, Template b) {

        return Math.abs(me.getX() - b.getX()) + Math.abs(me.getY() - b.getY());

    }

    public static boolean SearchAStar(Template start, Template goal, List<Template> searchTemplates) {
        List<Template> closedSet = new ArrayList<>(), openSet = new ArrayList<>();
        Map<Template, Integer> gScore = new HashMap<>(), fScore = new HashMap<>();

        openSet.add(start);
        gScore.put(start, 0);
        fScore.put(start, cityBlockDistance(start, goal));

        while (!openSet.isEmpty()) {

            Template current = getLowestScore(openSet, fScore);
            if (current.equals(goal)) return true;
            openSet.remove(current);
            if (!closedSet.contains(current)) closedSet.add(current);

            for (Template neighbour : getNeightbours(current, searchTemplates)) {
                if (closedSet.contains(neighbour)) continue;
                if (!openSet.contains(neighbour)) openSet.add(neighbour);
                int distanceStartNeighbor = gScore.get(current) + cityBlockDistance(current, neighbour);
                if (gScore.containsKey(neighbour) && distanceStartNeighbor >= gScore.get(neighbour)) continue;

                gScore.put(neighbour, distanceStartNeighbor);
                fScore.put(neighbour, cityBlockDistance(neighbour, goal));
            }
        }
        return false;
    }

    private static List<Template> getNeightbours(Template current, List<Template> searchNeighbors) {

        List<Template> neighbors = new ArrayList<>();
        for (Template b : searchNeighbors) if (current.neigborWith(b)) neighbors.add(b);

        return neighbors;
    }

    public static Template getLowestScore(List<Template> nodes, Map<Template, Integer> scores) {

        Template lowestScoreBattle = new Template();
        Integer minScore = 9999;
        for (Template b : nodes) {
            if (!scores.containsKey(b)) continue;

            if (scores.get(b) < minScore) {
                lowestScoreBattle = b;
                minScore = scores.get(b);
            }
        }

        return lowestScoreBattle;
    }

    public static void clickWhileSee(String templateToClick, boolean searchOnce, boolean singleResult, double precision) {

        boolean tempShowed = searchOnce;
        int limit = 20;
        Mat screenFrame = null;

        while (limit > 0) {

            screenFrame = CV.ScreenShot();
            List<Template> tempMatching = CV.matchingTemplate(screenFrame, templates.get(templateToClick), singleResult, precision);
            limit--;

            if (!tempMatching.isEmpty()) {
                tempShowed = true;
                InputUtil.mouseMoveAndClick(tempMatching.get(0).getX(), tempMatching.get(0).getY());
            } else {
                if (tempShowed) break;
            }
        }
    }

    public static void clickUntilSee(Template clickTemplate, String seeAsset, boolean singleResult, double precision) {

        Mat screen = null;

        while (true) {
            InputUtil.mouseMoveAndClick(clickTemplate.getX(), clickTemplate.getY());
            sleep(500);

            screen = CV.ScreenShot();
            List<Template> see_list = CV.matchingTemplate(screen, templates.get(seeAsset), singleResult, precision);
            if (!see_list.isEmpty()) break;
        }
    }

    public static void clickUntilSee(String clickAsset, String seeAsset, boolean singleResult, double precision) {

        Mat screen = null;

        while (true) {

            screen = CV.ScreenShot();
            List<Template> click_list = CV.matchingTemplate(screen, templates.get(clickAsset), singleResult, precision);
            if (!click_list.isEmpty()) {
                Template click = click_list.get(0);
                InputUtil.mouseMoveAndClick(click.getX(), click.getY());
                sleep(500);
            }

            screen = CV.ScreenShot();
            List<Template> see_list = CV.matchingTemplate(screen, templates.get(seeAsset), singleResult, precision);
            if (!see_list.isEmpty()) {
                break;
            }

        }

    }

    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
