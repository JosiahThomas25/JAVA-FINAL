//imports important files/data for project that we need
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;

//** AI Disclosure & Collaboration This project was developed with the assistance of Gemini (Google AI). **

// uses the 'implements' keyword to follow the ServiceItem interface contract. By using 'private' variables for name and price, we ensure data  +integrity,
class Product implements ServiceItem {
    private String name;
    private double price;
    //constructor for initalizing
    public Product(String name, double price) {
    //takes the data from the constructor parameter and saves it into the private member variable for this specific object/named 'this' to avoid confusion
        this.name = name;
        this.price = price;
    }

    // Controlled access via get methods
    public double getPrice() { return this.price; }
    public String getDescription() { return this.name; }
}

// by using extends we allow this class to access previous methods in  the super class such as getPrice
class RepairService extends Product {
    private int difficulty; // 1 = Easy, 2 = Hard

    public RepairService(String name, double basePrice, int difficulty) {
        //has to ask the Product constructor to call  them using this super call.
        super(name, basePrice); 
        this.difficulty = difficulty;
    }

    // Uses method overriding in order to update the price with added labor fees
    @Override
    public double getPrice() {
        double laborFee = (difficulty == 2) ? 100.00 : 50.00;
        return super.getPrice() + laborFee;
    }
}

public class TechShiftBusiness {
    //main program function
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        
        // Catalog items describing what Im selling
        ServiceItem[] catalog = new ServiceItem[4];
        catalog[0] = new Product("RTX 5090 GPU", 1599.99);
        catalog[1] = new Product("32GB DDR5 RAM", 120.00);
        catalog[2] = new RepairService("GPU Installation", 0.00, 1);
        catalog[3] = new RepairService("Custom Water Cooling", 200.00, 2);

        // State variables for navigating the shop and managing 
        ServiceItem[] cart = new ServiceItem[10]; 
        int cartCount = 0;
        boolean shopping = true;

        System.out.println("=== Welcome to Josiah's PC Parts ===");

        // while loop for managing the menu as long as the user is using it
        while (shopping && cartCount < 10) {
            System.out.println("\n--- Services Menu ---");
            for (int i = 0; i < catalog.length; i++) {
                System.out.println((i + 1) + ". " + catalog[i].getDescription() + " ($" + catalog[i].getPrice() + ")");
            }
            System.out.println("5. Checkout");
            System.out.print("Select an option: ");

            int choice = input.nextInt();

            // switch statement to alternate between what the user wants
            switch (choice) {
                //add whatever service they want into the cart
                case 1: case 2: case 3: case 4:
                    cart[cartCount] = catalog[choice - 1];
                    cartCount++;
                    System.out.println("Added to cart.");
                    break;
                case 5:
                    shopping = false;
                    break;
                //if none of the numbers are recognized, code won't break because of a default system
                default:
                    System.out.println("Invalid selection.");
            }
        }

        //  Tells the user appointment times and input
        System.out.print("\nEnter delivery hour (9-17): ");
        int hour = input.nextInt();
        
        // checks if the hours actually match within that time frame, 
        //IMPROVEMENT: make them actually reschedule
        if (hour < 9 || hour > 17) {
            System.out.println("Outside hours. Defaulting to 9 AM.");
            hour = 9;
        }

        // math for calculating the totals
        double subtotal = 0;
        String orderSummary = "--- Order Receipt ---\n";
        //for loop to compile all the info needed that the user ordered and later on to calculate the prices
        for (int i = 0; i < cartCount; i++) {
            subtotal += cart[i].getPrice(); // Arithmetic Operator
            orderSummary += "- " + cart[i].getDescription() + "\n";
        }

        double totalWithTax = subtotal * 1.08; // Precedence
        orderSummary += "Total (inc. 8% tax): $" + String.format("%.2f", totalWithTax);
        orderSummary += "\nAppointment: " + hour + ":00";

        // outputs summary to user
        System.out.println("\n" + orderSummary);

        // final output to txt
        try {
            FileWriter writer = new FileWriter("OrderReceipt.txt");
            writer.write(orderSummary);
            writer.close();
            System.out.println("\nReceipt saved to OrderReceipt.txt");
        } catch (IOException e) {
            System.out.println("Error saving file.");
        }

        input.close();
    }
}
