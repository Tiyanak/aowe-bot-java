package aowe.helper;

import com.sun.org.apache.bcel.internal.util.ClassPath;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Igor Farszky on 1.7.2017..
 */
public class Constants {

    public static final Double MATCHING_PRECISION = 0.8;
    public static final Double MATCHING_ZERO = 0.0;
    public static final double BLOCKED_PRECISION = 0.7;
    public static final String PNG_EXT = ".PNG";
    public static final String AOWE_ASSETS = "aowe_assets/";
    public static final String CV_LIB = "lib/";
    public static final double MATCHING_EMPTY = 0.65;
    public static final double MATCHING_HEARTS = 0.9;

    public static final List<String> HYDRA_TEMPLATES = Arrays.asList("hydra_battle", "hydra_boss", "hydra_empty", "hydra_fight",
            "hydra_lost", "hydra_me", "hydra_x", "hydra_block", "hydra_lost", "hydra_replay", "hydra_forward", "hydra_forward_shade",
            "aowe_hero_tavern", "hydra_battle_report", "hydra_buff", "hydra_heart", "hydra_empty_troop");

    public static final List<String> MYSTIC_TOWER_TEMPLATES = Arrays.asList("tower_big_city", "tower_big_city_burning",
            "tower_boss", "tower_chest_battle", "tower_defeat", "tower_finish_chest", "tower_small_city", "tower_small_city",
            "tower_small_city_burning", "tower_victory", "hydra_forward", "hydra_x", "tower_me_to_right", "tower_me_to_left", "hydra_fight", "tower_battle");

    public static final List<String> GEM_SEARCH_TEMPLATES = Arrays.asList("gem_1", "synth_btn", "gem_exp_btn", "gem_full_confirm",
            "red_apple_gem", "select_all_btn", "synth_gem_btn");

    public static final String HYDRA_ME = "hydra_me";
    public static final String HYDRA_BUFF = "hydra_buff";
    public static final String HYDRA_BATTLE = "hydra_battle";
    public static final String HYDRA_BOSS = "hydra_boss";
    public static final String HYDRA_X = "hydra_x";
    public static final String HYDRA_BLOCKED = "hydra_block";
    public static final String HYDRA_REPLAY = "hydra_replay";
    public static final String HYDRA_FIGHT = "hydra_fight";
    public static final String HYDRA_FORWARD_SHADE = "hydra_forward_shade";
    public static final String HYDRA_FORWARD = "hydra_forward";
    public static final String HYDRA_BATTLE_REPORT = "hydra_battle_report";
    public static final String HYDRA_EMPTY = "hydra_empty";
    public static final String HYDRA_HEART = "hydra_heart";
    public static final String HYDRA_EMPTY_TROOP = "hydra_empty_troop";

    public static final String AOWE_HERO_TAVERN = "aowe_hero_tavern";

    public static final String SIGHT_HERO_HIGHLIGHT = "sight_hero_highlight";
    public static final String SIGHT_START = "sight_start";
    public static final String SIGHT_HERO_GUESS = "sight_hero_guess";

    public static final String TOWER_ME_RIGHT = "tower_me_to_right";
    public static final String TOWER_ME_LEFT = "tower_me_to_left";
    public static final String TOWER_BIG_CITY = "tower_big_city";
    public static final String TOWER_BIG_CITY_BURNING = "tower_big_city_burning";
    public static final String TOWER_BOSS = "tower_boss";
    public static final String TOWER_CHEST_BATTLE = "tower_chest_battle";
    public static final String TOWER_DEFEAT = "tower_defeat";
    public static final String TOWER_FINISH_CHEST = "tower_finish_chest";
    public static final String TOWER_SMALL_CITY = "tower_small_city";
    public static final String TOWER_SMALL_CITY_BURNING = "tower_small_city_burning";
    public static final String TOWER_VICTORY = "tower_victory";
    public static final String TOWER_BATTLE = "tower_battle";

    public static final String GEM_1 = "gem_1";
    public static final String SYNTH_BTN = "synth_btn";
    public static final String GEM_EXP_BTN = "gem_exp_btn";
    public static final String GEM_FULL_CONFIRM = "gem_full_confirm";
    public static final String RED_APPLE_GEM = "red_apple_gem";
    public static final String SELECT_ALL_BTN = "select_all_btn";
    public static final String SYNTH_GEM_BTN = "synth_gem_btn";

    public static final String HYDRA_BACKGROUND = AOWE_ASSETS + "hydra_background" + PNG_EXT;
    public static final String FIRST_SIGHT_BACKGROUND = AOWE_ASSETS + "first_sight_background" + PNG_EXT;
    public static final String MYSTIC_TOWER_BACKGROUND = AOWE_ASSETS + "mystic_tower_background" + PNG_EXT;
    public static final String AOWE_BACKGROUND = AOWE_ASSETS + "aowe_background" + PNG_EXT;
    public static final String GEM_SEARCH_BACKGROUND = AOWE_ASSETS + "gem_search_background" + PNG_EXT;

}
