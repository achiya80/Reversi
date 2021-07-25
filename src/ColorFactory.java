import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ColorFactory {



    public static List<Supplier<Color>> colors= new LinkedList<>(){
        {
            add(() -> Color.gray);
            add(() -> Color.red);
            add(() -> Color.blue);
        }
    };

    public static List<Supplier<String>> colorNames= new LinkedList<>(){
        {
            add(() -> "Gray");
            add(() -> "Red");
            add(() -> "Blue");
        }
    };


    public static Color produceColor(int idx){
        return colors.get(idx).get();
    }

    public static String produceColorName(int idx) { return colorNames.get(idx).get(); }
}
