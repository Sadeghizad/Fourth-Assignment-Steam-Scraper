import java.io.IOException;


public class Main {

    public static void main(String[] args) throws IOException {

        System.out.println("hi");
        Parser parser = new Parser();
        parser.setUp();
        System.out.println("bye");

        System.out.println("=== Alphabetical ===");
        parser.sortByName().forEach(System.out::println);

        System.out.println("\n=== Highest rating ===");
        parser.sortByRating().forEach(System.out::println);

        System.out.println("\n=== Most expensive first ===");
        parser.sortByPrice().forEach(System.out::println);
    }
}
