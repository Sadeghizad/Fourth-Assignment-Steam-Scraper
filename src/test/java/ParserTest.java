import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParserTest {

    public static Parser handle;

    @BeforeAll
    static void setUp() throws IOException {
        handle = new Parser();
        handle.setUp();
    }

    @Test
    public void testSortByName() {
        List<Game> sortedGames = handle.sortByName();
        Game g1 = new Game("Bloodborne" ,4.8,88);
        Game g2 = new Game("Gran Turismo 2" ,4.6,90);
        Game g3 = new Game("Resident Evil Code: Veronica" ,4.6,85);
        Game g4 = new Game("Super Smash Bros. Brawl" ,4.6,88);
        Game g5 = new Game("The Last of Us" ,4.9,92);
        Game g6 = new Game("Wipeout XL" ,4.3,86);

        //Todo
        assertEquals(57, sortedGames.size());
        assertEquals(sortedGames.get(2),g1);
        assertEquals(sortedGames.get(13),g2);
        assertEquals(sortedGames.get(24),g3);
        assertEquals(sortedGames.get(35),g4);
        assertEquals(sortedGames.get(41),g5);
        assertEquals(sortedGames.get(55),g6);
    }

        @Test
        public void testSortByPrice() {
            List<Game> sortedGames = handle.sortByPrice();
            Game g1 = new Game("Persona 5 Royal" ,4.7,84);
            Game g2 = new Game("Resident Evil Code: Veronica" ,4.6,85);
            Game g3 = new Game("The Last of Us Part II" ,4.9,57);
            Game g4 = new Game("God of War II" ,4.9,90);
            Game g5 = new Game("Forza Motorsport" ,4.6,76);
            Game g6 = new Game("Super Mario Sunshine" ,4.5,84);

//            assertEquals(250, sortedGames.size());
//            assertEquals(sortedGames.get(18),g1);
//            assertEquals(sortedGames.get(148),g2);
//            assertEquals(sortedGames.get(74),g3);
//            assertEquals(sortedGames.get(12),g4);
//            assertEquals(sortedGames.get(66),g5);
//            assertEquals(sortedGames.get(35),g6);
        }

    @Test
    public void testSortByRating() {
        List<Game> sortedGames = handle.sortByRating();
        Game g1 = new Game("Persona 5 Royal" ,4.7,84);
        Game g2 = new Game("Resident Evil Code: Veronica" ,4.6,85);
        Game g3 = new Game("The Last of Us Part II" ,4.9,57);
        Game g4 = new Game("God of War II" ,4.9,90);
        Game g5 = new Game("Forza Motorsport" ,4.6,76);
        Game g6 = new Game("Super Mario Sunshine" ,4.5,84);

//        assertEquals(250, sortedGames.size());
//        assertEquals(sortedGames.get(11),g1);
//        assertEquals(sortedGames.get(166),g2);
//        assertEquals(sortedGames.get(97),g3);
//        assertEquals(sortedGames.get(67),g4);
//        assertEquals(sortedGames.get(24),g5);
//        assertEquals(sortedGames.get(2),g6);
    }
}
