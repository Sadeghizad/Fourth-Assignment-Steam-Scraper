import java.io.IOException;
import java.util.List;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) throws IOException {

        Parser parser = new Parser();
        parser.setUp("");
        Scanner input = new Scanner(System.in);
        System.out.println("Sort by: (1) Name (2) Rating (3) Price (4) Bonus wiki");
        int choice = input.nextInt();

        List<Game> result = switch (choice) {
            case 2 -> {
                System.out.println("\n=== Highest rating ===");
                yield parser.sortByRating();
            }
            case 3 -> {
                System.out.println("\n=== Most expensive first ===");
                yield parser.sortByPrice();
            }
            case 4 -> {

                System.out.println("\n=== wiki ===");
                System.out.printf("%-45s | %8s | %s\n","NAME","YEAR","STUDIO");
                Parser parser2 = new Parser();
//                parser2.setUp("https://en.wikipedia.org/wiki/Academy_Award_for_Best_Picture");
                parser2.setUp("oscar.html");
                List<OscarFilm> filmList = parser2.getOscarFilms();
                filmList.forEach(System.out::println);
                yield List.of(); // Return an empty Game list to keep the type

            }
            default -> {
                System.out.println("=== Alphabetical ===");
                yield parser.sortByName();
            }
        };
        result.forEach(System.out::println);

    }
}
