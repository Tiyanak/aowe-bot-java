package aowe.game;

import aowe.helper.CV;
import aowe.helper.Constants;
import aowe.helper.KeyPresser;
import aowe.helper.ScreenHelper;
import aowe.model.Battle;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Igor Farszky on 15.10.2017..
 */
public class GemSearch implements Game {

    private boolean shouldPlay;
    private boolean isPlaying;
    private KeyPresser keyPresser;
    private Map<String, Mat> templates;

    public GemSearch() {
        this.keyPresser = new KeyPresser();
        this.isPlaying = false;
        this.shouldPlay = true;
        initTemplates();
    }

    public void initTemplates() {
        this.templates = new HashMap<>();

        for (String gem_temp : Constants.GEM_SEARCH_TEMPLATES) {
            String path = Constants.AOWE_ASSETS + gem_temp + Constants.PNG_EXT;
            try {
                this.templates.put(gem_temp, Imgcodecs.imread(path));
            } catch (Exception e) {
            }
        }
    }

    public void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void play() {

        this.isPlaying = true;
        Random r = new Random();

        Mat screenFrame = ScreenHelper.GetCurrentScreenImage();

        List<Battle> gem_1_list = CV.matchingHydraTemplates(screenFrame, this.templates.get(Constants.GEM_1), true, false, true, Constants.MATCHING_PRECISION);
        List<Battle> synth_btn_list = CV.matchingHydraTemplates(screenFrame, this.templates.get(Constants.SYNTH_BTN), true, false, true, Constants.MATCHING_PRECISION);
        List<Battle> gem_exp_btn_list = CV.matchingHydraTemplates(screenFrame, this.templates.get(Constants.GEM_EXP_BTN), true, false, true, Constants.MATCHING_PRECISION);

        if (gem_1_list.isEmpty() || synth_btn_list.isEmpty() || gem_exp_btn_list.isEmpty()){
            return;
        }

        Battle gem_1 = gem_1_list.get(0);
        Battle synth_btn = synth_btn_list.get(0);
        Battle gem_exp_btn = gem_exp_btn_list.get(0);
        Battle select_all_btn = null;
        Battle synth_gem_btn = null;
        keyPresser.move(gem_1.getX(), gem_1.getY());

        while (true) {

            for (int i=0; i<600; i++) {
                keyPresser.click();
            }

            sleep(300);

            screenFrame = ScreenHelper.GetCurrentScreenImage();
            List<Battle> gem_full_confirm_list = CV.matchingHydraTemplates(screenFrame, this.templates.get(Constants.GEM_FULL_CONFIRM), true, false, false, Constants.MATCHING_PRECISION);

            if (!gem_full_confirm_list.isEmpty()) {
                // stisni confirm i odi na synt ekran
                Battle gem_full_confirm_btn = gem_full_confirm_list.get(0);
                keyPresser.moveAndclick(gem_full_confirm_btn.getX(), gem_full_confirm_btn.getY());
                sleep(200);

                // izaberi random crveni apple gem
                screenFrame = ScreenHelper.GetCurrentScreenImage();
                List<Battle> red_apple_list = CV.matchingHydraTemplates(screenFrame, this.templates.get(Constants.RED_APPLE_GEM), false, false, false, Constants.MATCHING_PRECISION);
                Battle chosen_red_apple = red_apple_list.get(r.nextInt(red_apple_list.size()));
                keyPresser.moveAndclick(chosen_red_apple.getX(), chosen_red_apple.getY());
                sleep(200);

                if (select_all_btn == null || synth_gem_btn == null) {
                    screenFrame = ScreenHelper.GetCurrentScreenImage();
                    List<Battle> select_all_list = CV.matchingHydraTemplates(screenFrame, this.templates.get(Constants.SELECT_ALL_BTN), false, false, false, Constants.MATCHING_PRECISION);
                    List<Battle> synth_gem_list = CV.matchingHydraTemplates(screenFrame, this.templates.get(Constants.SYNTH_GEM_BTN), false, false, false, Constants.MATCHING_PRECISION);

                    select_all_btn = select_all_list.get(0);
                    synth_gem_btn = synth_gem_list.get(0);
                }

                for (int i=0; i<200; i++) {
                    keyPresser.moveAndclick(select_all_btn.getX(), select_all_btn.getY());
                    sleep(100);
                    keyPresser.moveAndclick(synth_gem_btn.getX(), synth_gem_btn.getY());
                    sleep(100);
                }

                keyPresser.moveAndclick(gem_exp_btn.getX(), gem_exp_btn.getY());
                sleep(200);
                keyPresser.moveAndclick(gem_1.getX(), gem_1.getY());
                sleep(200);

            } else {

                keyPresser.moveAndclick(synth_btn.getX(), synth_btn.getY());
                sleep(200);
                keyPresser.moveAndclick(gem_exp_btn.getX(), gem_exp_btn.getY());
                sleep(200);
                keyPresser.move(gem_1.getX(), gem_1.getY());
                sleep(200);

            }
        }

    }

    @Override
    public void stop() {
        System.out.println("PAUSING FIRST SIGHT");
        this.shouldPlay = false;
    }

    @Override
    public void start() {
        System.out.println("STARTING FIRST SIGHT AGAIN");
        this.shouldPlay = true;
    }

    @Override
    public boolean isPlaying() {
        return this.isPlaying;
    }

    @Override
    public void setIsPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

}
