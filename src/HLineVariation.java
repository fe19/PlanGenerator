public class HLineVariation {

    public static int getVariationLine(int heightInMeter) {
        int minimum = 5;
        return (int) (Math.random() * (heightInMeter - 2 * minimum)) + minimum;
    }
}
