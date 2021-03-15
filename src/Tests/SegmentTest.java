package Tests;

import GeneticAlg.Point;
import GeneticAlg.Segment;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.jupiter.api.DisplayName;

public class SegmentTest {
    private Segment segment;

    @Test
    @DisplayName("Creation should work")
    public void testCreation(){
        segment = new Segment(11,11,20, Segment.Direction.UP);
        assertEquals("UP Constructor should work", new Point(11,-9), segment.getEnd());

        segment = new Segment(11,11,20, Segment.Direction.DOWN);
        assertEquals("DOWN Constructor should work", new Point(11, 31), segment.getEnd());

        segment = new Segment(11,11,20, Segment.Direction.LEFT);
        assertEquals("LEFT Constructor should work", new Point(-9, 11), segment.getEnd());

        segment = new Segment(11,11,20, Segment.Direction.RIGHT);
        assertEquals("RIGHT Constructor should work", new Point(31, 11), segment.getEnd());

    }

    @Test
    @DisplayName("Shortening to point should work")
    public void testShorteningToPoint(){
        Point point = new Point(11, 10);
        segment = new Segment(11, 11, 20, Segment.Direction.UP);
        segment.shortenToPoint(point);
        assertEquals("Shortening UP", point, segment.getEnd());
        assertEquals("Shortening UP LENGTH", 1, segment.getLength());

        point = new Point(11, 13);
        segment = new Segment(11, 11, 20, Segment.Direction.DOWN);
        segment.shortenToPoint(point);
        assertEquals("Shortening DOWN", point, segment.getEnd());
        assertEquals("Shortening DOWN LENGTH", 2, segment.getLength());

        point = new Point(-2, 11);
        segment = new Segment(11, 11, 20, Segment.Direction.LEFT);
        segment.shortenToPoint(point);
        assertEquals("Shortening LEFT", point, segment.getEnd());
        assertEquals("Shortening LEFT LENGTH", 13, segment.getLength());

        point = new Point(14, 11);
        segment = new Segment(11, 11, 20, Segment.Direction.RIGHT);
        segment.shortenToPoint(point);
        assertEquals("Shortening RIGHT", point, segment.getEnd());
        assertEquals("Shortening RIGHT LENGTH", 3, segment.getLength());

    }

    @Test
    @DisplayName("Expanding to point should work")
    public void testExpandingToPoint(){
        Point point = new Point(11, -19);
        segment = new Segment(11, 11, 20, Segment.Direction.UP);
        segment.expandToPoint(point);
        assertEquals("Expanding UP", point, segment.getEnd());
        assertEquals("Expanding UP LENGTH", 30, segment.getLength());

        point = new Point(11, 41);
        segment = new Segment(11, 11, 20, Segment.Direction.DOWN);
        segment.expandToPoint(point);
        assertEquals("Expanding DOWN", point, segment.getEnd());
        assertEquals("Expanding DOWN LENGTH", 30, segment.getLength());

        point = new Point(-19, 11);
        segment = new Segment(11, 11, 20, Segment.Direction.LEFT);
        segment.expandToPoint(point);
        assertEquals("Expanding LEFT", point, segment.getEnd());
        assertEquals("Expanding LEFT LENGTH", 30, segment.getLength());

        point = new Point(41, 11);
        segment = new Segment(11, 11, 20, Segment.Direction.RIGHT);
        segment.expandToPoint(point);
        assertEquals("Expanding RIGHT", segment.getEnd(), point);
        assertEquals("Expanding RIGHT LENGTH", 30, segment.getLength());
    }

    @Test
    @DisplayName("Count length not on board")
    public void testNotOnBoard(){
        int maxX = 10, maxY = 20;
        Segment[] segments = new Segment[]{
                new Segment(3, 4, 9, Segment.Direction.UP), //0
                new Segment(3, 4, 9, Segment.Direction.LEFT), //1
                new Segment(3, 4, 20, Segment.Direction.DOWN), //2
                new Segment(3, 4, 11, Segment.Direction.RIGHT), //3
                new Segment(3, 4, 4, Segment.Direction.UP), // 4
                new Segment(3, 4, 3, Segment.Direction.LEFT), //5
                new Segment(3, 4, 16, Segment.Direction.DOWN), //6
                new Segment(3, 4, 6, Segment.Direction.RIGHT), //7

                new Segment(-4, 9, 2, Segment.Direction.UP), //8
                new Segment(-4, 9, 3, Segment.Direction.LEFT), //9
                new Segment(-4, 9, 4, Segment.Direction.DOWN), //10
                new Segment(-4, 9, 4, Segment.Direction.RIGHT), //11
                new Segment(-4, 9, 6, Segment.Direction.RIGHT), //12
                new Segment(-4, 9, 14, Segment.Direction.RIGHT), //13
                new Segment(-4, 9, 18, Segment.Direction.RIGHT), //14
                new Segment(-4, 9, 3, Segment.Direction.RIGHT), //15

                new Segment(6, -10, 2, Segment.Direction.UP), //16
                new Segment(6, -10, 3, Segment.Direction.LEFT), //17
                new Segment(6, -10, 4, Segment.Direction.RIGHT), //18
                new Segment(6, -10, 10, Segment.Direction.DOWN), //19
                new Segment(6, -10, 14, Segment.Direction.DOWN), //20
                new Segment(6, -10, 30, Segment.Direction.DOWN), //21
                new Segment(6, -10, 40, Segment.Direction.DOWN), //22
                new Segment(6, -10, 6, Segment.Direction.DOWN), //23

                new Segment(6, 30, 2, Segment.Direction.DOWN), //24
                new Segment(6, 30, 3, Segment.Direction.LEFT), //25
                new Segment(6, 30, 4, Segment.Direction.RIGHT), //26
                new Segment(6, 30, 10, Segment.Direction.UP), //27
                new Segment(6, 30, 14, Segment.Direction.UP), //28
                new Segment(6, 30, 30, Segment.Direction.UP), //29
                new Segment(6, 30, 40, Segment.Direction.UP), //30
                new Segment(6, 30, 6, Segment.Direction.UP), //31

                new Segment(20, 9, 2, Segment.Direction.UP), //32
                new Segment(20, 9, 3, Segment.Direction.RIGHT), //33
                new Segment(20, 9, 4, Segment.Direction.DOWN), //34
                new Segment(20, 9, 10, Segment.Direction.LEFT), //35
                new Segment(20, 9, 14, Segment.Direction.LEFT), //36
                new Segment(20, 9, 20, Segment.Direction.LEFT), //37
                new Segment(20, 9, 30, Segment.Direction.LEFT), //38
                new Segment(20, 9, 6, Segment.Direction.LEFT), //39

                new Segment(-10, -10, 10, Segment.Direction.UP), //40
                new Segment(-10, -10, 10, Segment.Direction.LEFT), //41
                new Segment(-10, -10, 5, Segment.Direction.DOWN), //42
                new Segment(-10, -10, 10, Segment.Direction.DOWN), //43
                new Segment(-10, -10, 15, Segment.Direction.DOWN), //44
                new Segment(-10, -10, 30, Segment.Direction.DOWN), //45
                new Segment(-10, -10, 40, Segment.Direction.DOWN), //46
                new Segment(-10, -10, 5, Segment.Direction.RIGHT), //47
                new Segment(-10, -10, 10, Segment.Direction.RIGHT), //48
                new Segment(-10, -10, 15, Segment.Direction.RIGHT), //49
                new Segment(-10, -10, 20, Segment.Direction.RIGHT), //50
                new Segment(-10, -10, 30, Segment.Direction.RIGHT), //51


        };
        int[] results = new int[]{
                5, 6, 4, 4, 0, 0, 0, 0,
                2, 3, 4, 4, 4, 4, 8, 3,
                2, 3, 4, 10, 10, 10, 20, 6,
                2, 3, 4, 10, 10, 10, 20, 6,
                2, 3, 4, 10, 10, 10, 20, 6,
                10, 10, 5, 10, 15, 30, 40, 5, 10, 15, 20, 30

        };
        for(int i = 0; i < segments.length; i++){
            assertEquals("Dlugosc poza boardem dla: " + i, results[i], segments[i].notOnBoard(maxX, maxY));
        }
    }

}
