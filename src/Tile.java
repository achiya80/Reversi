import java.awt.Color;
public class Tile {

    private Position position;

    private ColorBackgroundCallback backgroundCallback;

    private Color color;

    public Tile(Position position, Color color) {
        this.position = position;
        this.color = color;
    }

    public void initialize(Position position) {
        this.position = position;
    }

    public Position getPosition(){
        return position;
    }

    public Color getColor(){
        return color;
    }

    public void setBackgroundCallback(ColorBackgroundCallback backgroundCallback){
        this.backgroundCallback = backgroundCallback;
    }
}
