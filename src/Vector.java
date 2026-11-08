import java.util.ArrayList;
import java.util.List;

public class Vector {
    List<Double> values = new ArrayList<>();

    Vector(double... vals){
        for (double val : vals){
            values.add(val);
        }
    }

    public double sum(){
        return values.stream().reduce(0.0, Double::sum);
    }

    public double magnitude(){
        return Math.sqrt(this.sum());
    }
}
