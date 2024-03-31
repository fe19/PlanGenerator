public class HLineVariation {

    public static int getVariationLine(int heightInMeter) {
        int minimum = 5;
        int height = (int) (Math.random() * (heightInMeter - 2 * minimum)) + minimum; //5...15
        if (height >= 14) {
            return 20;
        }
        return (int) (Math.random() * (heightInMeter - 2 * minimum)) + minimum;
    }
}
