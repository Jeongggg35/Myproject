package healthDankook;

class Food {
    double kcal, carb, protein, fat;
    int gramPerUnit; // 1개당 g

    public Food(double kcal, double carb, double protein, double fat, int gramPerUnit) {
        this.kcal = kcal;
        this.carb = carb;
        this.protein = protein;
        this.fat = fat;
        this.gramPerUnit = gramPerUnit;
    }
}