import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import com.example.decorator.BasketDecorator;
import com.example.decorator.Flower;
import com.example.decorator.PaperDecorator;
import com.example.decorator.RibbonDecorator;
import com.example.decorator.Item;


public class DecoratorTests {


    private static final double FLOWER_PRICE = 10.0;
    private static final double BASKET_PRICE = 4.0;
    private static final double PAPER_PRICE = 13.0;
    private static final double RIBBON_PRICE = 40.0;

    private Flower flower;
    private BasketDecorator basketDecorator;
    private PaperDecorator paperDecorator;
    private RibbonDecorator ribbonDecorator;

    @BeforeEach
    public void setUp() {

        flower = new Flower(FLOWER_PRICE);
        basketDecorator = new BasketDecorator(flower);
        paperDecorator = new PaperDecorator(flower);
        ribbonDecorator = new RibbonDecorator(flower);
    }

    @Test
    public void testBasketDecorator() {

        String description = basketDecorator.getDescription();
        Assertions.assertEquals("A beautiful flower in a basket wrapper!",
                     description);


        double price = basketDecorator.price();
        Assertions.assertEquals(FLOWER_PRICE + BASKET_PRICE, price);
    }

    @Test
    public void testPaperDecorator() {

        String description = paperDecorator.getDescription();
        Assertions.assertEquals("A beautiful flower in a paper wrapper!",
                     description);


        double price = paperDecorator.price();
        Assertions.assertEquals(FLOWER_PRICE + PAPER_PRICE, price);
    }

    @Test
    public void testRibbonDecorator() {

        String description = ribbonDecorator.getDescription();
        Assertions.assertEquals("A beautiful flower in a ribbon wrapper!",
                     description);


        double price = ribbonDecorator.price();
        Assertions.assertEquals(FLOWER_PRICE + RIBBON_PRICE, price);
    }

    @Test
    public void testMultipleDecorators() {

        Item decoratedFlower = new BasketDecorator(
                                  new PaperDecorator(
                                  new RibbonDecorator(flower))
                              );


        String description = decoratedFlower.getDescription();
        Assertions.assertEquals(
            "A beautiful flower in a ribbon wrapper! in a paper wrapper! "
            + "in a basket wrapper!",
            description
        );


        double expectedPrice = FLOWER_PRICE + RIBBON_PRICE
                               + PAPER_PRICE + BASKET_PRICE;
        double actualPrice = decoratedFlower.price();
        Assertions.assertEquals(expectedPrice, actualPrice);
    }

    @Test
    public void testChainedDecorators() {

        Item decoratedFlower = new RibbonDecorator(flower);
        decoratedFlower = new PaperDecorator(decoratedFlower);
        decoratedFlower = new BasketDecorator(decoratedFlower);


        String description = decoratedFlower.getDescription();
        Assertions.assertEquals(
            "A beautiful flower in a ribbon wrapper! in a paper wrapper! "
            + "in a basket wrapper!",
            description
        );


        double expectedPrice = FLOWER_PRICE + RIBBON_PRICE
                               + PAPER_PRICE + BASKET_PRICE;
        double actualPrice = decoratedFlower.price();
        Assertions.assertEquals(expectedPrice, actualPrice);
    }
}
