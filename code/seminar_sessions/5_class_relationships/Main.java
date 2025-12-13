public class Main {
    public static void main(String[] args) {
        // Create a manufacturer
        Manufacturer techCorp = new Manufacturer("Vintage Computing Co.", "UK");

        // Create a product
        Product differenceEngine = new Product("Difference Engine Replica", 1200.00);
        differenceEngine.setManufacturer(techCorp);

        // Ada Lovelace writes a review
        Review adaReview = new Review("Ada Lovelace", "A remarkable device! It has the potential to analyze and act on information, not just process numbers.", 5);
        differenceEngine.addReview(adaReview);

        // Alan Turing also writes a review
        Review turingReview = new Review("Alan Turing", "A foundational concept in automated computation. This replica is a beautiful piece of history.", 5);
        differenceEngine.addReview(turingReview);

        // Create categories
        Category computingHistory = new Category("Computing History");
        Category mechanical = new Category("Mechanical Devices");

        // Add the product to categories
        differenceEngine.addCategory(computingHistory);
        differenceEngine.addCategory(mechanical);

        // --- Output to demonstrate the relationships ---
        System.out.println("Product: " + differenceEngine.getName());
        System.out.println("Price: $" + differenceEngine.getPrice());
        System.out.println("Manufacturer: " + differenceEngine.getManufacturer().getName() + " from " + differenceEngine.getManufacturer().getCountry());

        System.out.println("\n--- Reviews ---");
        for (Review review : differenceEngine.getReviews()) {
            System.out.println(review.getUser() + " says: \"" + review.getComment() + "\" (" + review.getRating() + "/5 stars)");
        }

        System.out.println("\n--- Categories ---");
        for (Category category : differenceEngine.getCategories()) {
            System.out.println("- " + category.getName());
        }

        System.out.println("\n--- Products in 'Computing History' ---");
        for (Product product : computingHistory.getProducts()) {
            System.out.println("- " + product.getName());
        }
    }
}
