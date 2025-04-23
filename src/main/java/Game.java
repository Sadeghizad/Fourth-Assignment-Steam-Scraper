import java.util.Objects;


 //Immutable value object that represents a Steam game record.

public class Game implements Comparable<Game> {

    private final String name;
    private final double rating;   // e.g. 4.5  (out of 5)
    private final int price;       // store as whole-currency cents, or euros if that’s what the HTML gives you

    //constructor

    public Game(String name, double rating, int price) {

        this.name   = Objects.requireNonNull(name, "name cannot be null").trim();
        this.rating = rating;
        this.price  = price;

        // quick sanity checks (optional, tweak as needed)
        if (rating < 0.0 || rating > 5.0)
            throw new IllegalArgumentException("rating must be between 0 and 5");

        if (price < 0)
            throw new IllegalArgumentException("price cannot be negative");
    }

    //getters

    public String getName()   { return name;   }
    public double getRating() { return rating; }
    public int    getPrice()  { return price;  }

    //printable representation

    @Override
    public String toString() {
        return String.format("%-35s | stars: %.1f | dollars %d",
                name, rating, price);
    }

    //natural ordering (name A-Z)

    @Override
    public int compareTo(Game other) {
        return this.name.compareToIgnoreCase(other.name);
    }

    //extra comparators

    //Highest rating first
    public static final java.util.Comparator<Game> BY_RATING_DESC =
            java.util.Comparator.comparingDouble(Game::getRating).reversed();

    //Lowest price first
    public static final java.util.Comparator<Game> BY_PRICE_ASC =
            java.util.Comparator.comparingInt(Game::getPrice);

    //equality & hashing

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Game)) return false;
        Game game = (Game) o;
        return Double.compare(game.rating, rating) == 0 &&
                Integer.compare(game.price, price) == 0 &&
                Objects.equals(name, game.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, rating, price);
    }
}
