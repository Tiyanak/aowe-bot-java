package aowe.game;

/**
 * Created by Igor Farszky on 1.7.2017..
 */
public interface Game {

    void stop();
    void start();
    boolean isPlaying();
    void setIsPlaying(boolean isPlaying);

}