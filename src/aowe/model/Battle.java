package aowe.model;

/**
 * Created by Igor Farszky on 1.7.2017..
 */
public class Battle {

    private int x;
    private int y;

    public Battle(int x, int y) {
        this.x = x;
        this.y = y;
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

    public boolean similiar(Battle b){
        if (Math.abs(x - b.getX()) > 20) return false;
        return Math.abs(y - b.getY()) < 20;
    }

    public boolean neighborWith(Battle b){

        int distance = Math.abs(this.x - b.getX()) + Math.abs(this.y - b.getY());

        if (distance < 160 && distance > 100){
            return true;
        }else{
            return false;
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Battle battle = (Battle) o;

        if (x != battle.getX()) return false;
        return y == battle.getY();

    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public String toString() {
        return "Battle{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}

