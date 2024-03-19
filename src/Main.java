public class Main {
    public static void main(String[] args) {

        PlanGenerator plan = new PlanGenerator();

        //plan.generateImage();

        ApartmentSegmentationGenerator apartmentSegmentationGenerator = new ApartmentSegmentationGenerator();

        apartmentSegmentationGenerator.generateApartments();

    }
}
