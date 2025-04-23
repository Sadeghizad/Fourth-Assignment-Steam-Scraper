import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) throws IOException, SQLException {

        Parser parser = new Parser();
        parser.setUp("");
        Scanner input = new Scanner(System.in);
        System.out.println("Sort by: (1) Name (2) Rating (3) Price (4) Bonus wiki");
        int choice = input.nextInt();
        boolean isGame = true;
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
                isGame = false;
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
        if (isGame) {
            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:data.db")) {
                String sql = "INSERT INTO Games (name, rating, price) VALUES (?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    for (Game game : result) {
                        stmt.setString(1, game.getName());
                        stmt.setDouble(2, game.getRating());
                        stmt.setInt(3, game.getPrice());
                        stmt.addBatch();
                    }
                    stmt.executeBatch();
                } catch (SQLException e) {
                    sql = """
                              CREATE TABLE IF NOT EXISTS Games (
                                  id INTEGER PRIMARY KEY AUTOINCREMENT,
                                  name TEXT,
                                  rating REAL,
                                  price INTEGER
                              );
                          """;

                    try (Statement stmt = conn.createStatement()) {
                        stmt.execute(sql);
                        try (PreparedStatement stmt2 = conn.prepareStatement(sql)) {
                            for (Game game : result) {
                                stmt2.setString(1, game.getName());
                                stmt2.setDouble(2, game.getRating());
                                stmt2.setInt(3, game.getPrice());
                                stmt2.addBatch();
                            }
                            stmt.executeBatch();
                        } catch (SQLException er){
                            er.printStackTrace();
                        }
                    }
                }
            }

        }

    }
}
