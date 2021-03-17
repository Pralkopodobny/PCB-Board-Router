package Tests;

import GeneticAlg.Route;
import GeneticAlg.Segment;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RouteTest {

    @Test
    @DisplayName("Fixing routes should work")
    public void fixRouteTest(){
        ArrayList<Segment> segments = new ArrayList<>(List.of(
                new Segment(5, 5, 10, Segment.Direction.RIGHT),
                new Segment(15, 5, 10, Segment.Direction.RIGHT),
                new Segment(25, 5, 10, Segment.Direction.RIGHT)
        ));
        ArrayList<Segment> actual = new ArrayList<>(List.of(
                new Segment(5, 5, 30, Segment.Direction.RIGHT)
        ));
        Route r = Route.createTestRoute(segments);
        r.fix();
        assertEquals("Simple route fix should work", actual, r.segments);

        segments = new ArrayList<>(List.of(
                new Segment(25, 5, 10, Segment.Direction.LEFT),
                new Segment(15, 5, 10, Segment.Direction.LEFT),
                new Segment(5, 5, 10, Segment.Direction.LEFT)
        ));

        actual = new ArrayList<>(List.of(
                new Segment(25, 5, 30, Segment.Direction.LEFT)
        ));

        r = Route.createTestRoute(segments);
        r.fix();
        assertEquals("Simple route fix should work #2", actual, r.segments);

        segments = new ArrayList<>(List.of(
                new Segment(5, 10, 5, Segment.Direction.UP),
                new Segment(5, 5, 10, Segment.Direction.RIGHT),
                new Segment(15, 5, 10, Segment.Direction.RIGHT),
                new Segment(25, 5, 10, Segment.Direction.RIGHT)
        ));
        actual = new ArrayList<>(List.of(
                new Segment(5, 10, 5, Segment.Direction.UP),
                new Segment(5, 5, 30, Segment.Direction.RIGHT)
        ));
        r = Route.createTestRoute(segments);
        r.fix();
        assertEquals("Simple route fix should work #3", actual, r.segments);

        segments = new ArrayList<>(List.of(
                new Segment(5, 5, 10, Segment.Direction.RIGHT),
                new Segment(15, 5, 15, Segment.Direction.UP),
                new Segment(15, -10, 10, Segment.Direction.RIGHT),
                new Segment(25, -10, 10, Segment.Direction.DOWN),
                new Segment(25, 0, 10, Segment.Direction.UP),
                new Segment(25, -10, 5, Segment.Direction.UP),
                new Segment(25, -15, 20, Segment.Direction.LEFT),
                new Segment(5, -15, 20, Segment.Direction.DOWN),
                new Segment(5, 5, 5, Segment.Direction.UP),
                new Segment(5, 0, 5, Segment.Direction.LEFT),
                new Segment(0, 0, 3, Segment.Direction.RIGHT),
                new Segment(3, 0, 5, Segment.Direction.RIGHT)
        ));
        actual = new ArrayList<>(List.of(
                new Segment(5, 5, 10, Segment.Direction.RIGHT),
                new Segment(15, 5, 15, Segment.Direction.UP),
                new Segment(15, -10, 10, Segment.Direction.RIGHT),
                new Segment(25, -10, 5, Segment.Direction.UP),
                new Segment(25, -15, 20, Segment.Direction.LEFT),
                new Segment(5, -15, 15, Segment.Direction.DOWN),
                new Segment(5, 0, 3, Segment.Direction.RIGHT)
        ));
        r = Route.createTestRoute(segments);
        r.fix();
        assertEquals("Big test route fix should work", actual, r.segments);

    }

    @Test
    @DisplayName("Simple mutation operator should work")
    public void testSimpleMutation(){
        ArrayList<Segment> segments = new ArrayList<>(List.of(
                new Segment(5, 5, 10, Segment.Direction.RIGHT),
                new Segment(15, 5, 15, Segment.Direction.UP),
                new Segment(15, -10, 10, Segment.Direction.RIGHT)
        ));
        int index = 1;
        int force = 3;
        ArrayList<Segment> actual = new ArrayList<>(List.of(
                new Segment(5, 5, 13, Segment.Direction.RIGHT),
                new Segment(18, 5, 15, Segment.Direction.UP),
                new Segment(18, -10, 7, Segment.Direction.RIGHT)
        ));
        Route r = Route.createTestRoute(segments);
        r.simpleMutation(index, force);
        assertEquals("Simple mutation test should work #1", actual, r.segments);

        segments = new ArrayList<>(List.of(
                new Segment(5, 5, 10, Segment.Direction.RIGHT),
                new Segment(15, 5, 15, Segment.Direction.UP),
                new Segment(15, -10, 10, Segment.Direction.RIGHT)
        ));
        index = 1;
        force = -3;
        actual = new ArrayList<>(List.of(
                new Segment(5, 5, 7, Segment.Direction.RIGHT),
                new Segment(12, 5, 15, Segment.Direction.UP),
                new Segment(12, -10, 13, Segment.Direction.RIGHT)
        ));
        r = Route.createTestRoute(segments);
        r.simpleMutation(index, force);
        assertEquals("Simple mutation test should work #2", actual, r.segments);

        segments = new ArrayList<>(List.of(
                new Segment(5, 5, 10, Segment.Direction.RIGHT),
                new Segment(15, 5, 15, Segment.Direction.UP),
                new Segment(15, -10, 5, Segment.Direction.LEFT)
        ));

        index = 1;
        force = 3;
        actual = new ArrayList<>(List.of(
                new Segment(5, 5, 13, Segment.Direction.RIGHT),
                new Segment(18, 5, 15, Segment.Direction.UP),
                new Segment(18, -10, 8, Segment.Direction.LEFT)
        ));
        r = Route.createTestRoute(segments);
        r.simpleMutation(index, force);
        assertEquals("Simple mutation test should work #3", actual, r.segments);

        segments = new ArrayList<>(List.of(
                new Segment(5, 5, 10, Segment.Direction.RIGHT),
                new Segment(15, 5, 15, Segment.Direction.UP),
                new Segment(15, -10, 5, Segment.Direction.LEFT)
        ));

        index = 1;
        force = -3;
        actual = new ArrayList<>(List.of(
                new Segment(5, 5, 7, Segment.Direction.RIGHT),
                new Segment(12, 5, 15, Segment.Direction.UP),
                new Segment(12, -10, 2, Segment.Direction.LEFT)
        ));
        r = Route.createTestRoute(segments);
        r.simpleMutation(index, force);
        assertEquals("Simple mutation test should work #4", actual, r.segments);

        segments = new ArrayList<>(List.of(
                new Segment(5, 5, 10, Segment.Direction.RIGHT),
                new Segment(15, 5, 15, Segment.Direction.UP),
                new Segment(15, -10, 5, Segment.Direction.LEFT)
        ));

        index = 1;
        force = -5;
        actual = new ArrayList<>(List.of(
                new Segment(5, 5, 5, Segment.Direction.RIGHT),
                new Segment(10, 5, 15, Segment.Direction.UP)
        ));
        r = Route.createTestRoute(segments);
        r.simpleMutation(index, force);
        assertEquals("Simple mutation test should work #5", actual, r.segments);

        segments = new ArrayList<>(List.of(
                new Segment(5, 5, 10, Segment.Direction.RIGHT),
                new Segment(15, 5, 15, Segment.Direction.UP),
                new Segment(15, -10, 6, Segment.Direction.LEFT)
        ));

        index = 1;
        force = -10;
        actual = new ArrayList<>(List.of(
                new Segment(5, 5, 15, Segment.Direction.UP),
                new Segment(5, -10, 4, Segment.Direction.RIGHT)
        ));
        r = Route.createTestRoute(segments);
        r.simpleMutation(index, force);
        assertEquals("Simple mutation test should work #6", actual, r.segments);

        segments = new ArrayList<>(List.of(
                new Segment(5, 5, 10, Segment.Direction.RIGHT),
                new Segment(15, 5, 15, Segment.Direction.UP),
                new Segment(15, -10, 10, Segment.Direction.LEFT)
        ));

        index = 1;
        force = -10;
        actual = new ArrayList<>(List.of(
                new Segment(5, 5, 15, Segment.Direction.UP)
        ));
        r = Route.createTestRoute(segments);
        r.simpleMutation(index, force);
        assertEquals("Simple mutation test should work #7", actual, r.segments);

        segments = new ArrayList<>(List.of(
                new Segment(5, 5, 10, Segment.Direction.RIGHT),
                new Segment(15, 5, 15, Segment.Direction.UP),
                new Segment(15, -10, 5, Segment.Direction.LEFT)
        ));

        index = 0;
        force = 3;
        actual = new ArrayList<>(List.of(
                new Segment(5, 5, 3, Segment.Direction.DOWN),
                new Segment(5, 8, 10, Segment.Direction.RIGHT),
                new Segment(15, 8, 18, Segment.Direction.UP),
                new Segment(15, -10, 5, Segment.Direction.LEFT)
        ));
        r = Route.createTestRoute(segments);
        r.simpleMutation(index, force);
        assertEquals("Simple mutation test should work #8", actual, r.segments);

        segments = new ArrayList<>(List.of(
                new Segment(5, 5, 10, Segment.Direction.RIGHT),
                new Segment(15, 5, 15, Segment.Direction.UP),
                new Segment(15, -10, 5, Segment.Direction.LEFT)
        ));

        index = 0;
        force = -3;
        actual = new ArrayList<>(List.of(
                new Segment(5, 5, 3, Segment.Direction.UP),
                new Segment(5, 2, 10, Segment.Direction.RIGHT),
                new Segment(15, 2, 12, Segment.Direction.UP),
                new Segment(15, -10, 5, Segment.Direction.LEFT)
        ));
        r = Route.createTestRoute(segments);
        r.simpleMutation(index, force);
        assertEquals("Simple mutation test should work #9", actual, r.segments);

        segments = new ArrayList<>(List.of(
                new Segment(5, 5, 10, Segment.Direction.RIGHT),
                new Segment(15, 5, 15, Segment.Direction.UP),
                new Segment(15, -10, 5, Segment.Direction.LEFT)
        ));

        index = 2;
        force = 3;
        actual = new ArrayList<>(List.of(
                new Segment(5, 5, 10, Segment.Direction.RIGHT),
                new Segment(15, 5, 12, Segment.Direction.UP),
                new Segment(15, -7, 5, Segment.Direction.LEFT),
                new Segment(10, -7, 3, Segment.Direction.UP)
        ));
        r = Route.createTestRoute(segments);
        r.simpleMutation(index, force);
        assertEquals("Simple mutation test should work #10", actual, r.segments);

        segments = new ArrayList<>(List.of(
                new Segment(5, 5, 10, Segment.Direction.RIGHT),
                new Segment(15, 5, 15, Segment.Direction.UP),
                new Segment(15, -10, 5, Segment.Direction.LEFT)
        ));

        index = 2;
        force = -3;
        actual = new ArrayList<>(List.of(
                new Segment(5, 5, 10, Segment.Direction.RIGHT),
                new Segment(15, 5, 18, Segment.Direction.UP),
                new Segment(15, -13, 5, Segment.Direction.LEFT),
                new Segment(10, -13, 3, Segment.Direction.DOWN)
        ));
        r = Route.createTestRoute(segments);
        r.simpleMutation(index, force);
        assertEquals("Simple mutation test should work #11", actual, r.segments);

        segments = new ArrayList<>(List.of(
                new Segment(5, 5, 10, Segment.Direction.RIGHT)
        ));
        index = 0;
        force = 3;
        actual = new ArrayList<>(List.of(
                new Segment(5, 5, 3, Segment.Direction.DOWN),
                new Segment(5, 8, 10, Segment.Direction.RIGHT),
                new Segment(15, 8, 3, Segment.Direction.UP)
        ));

        r = Route.createTestRoute(segments);
        r.simpleMutation(index, force);
        assertEquals("Simple mutation test should work #12", actual, r.segments);

        segments = new ArrayList<>(List.of(
                new Segment(5, 5, 10, Segment.Direction.RIGHT)
        ));
        index = 0;
        force = -3;
        actual = new ArrayList<>(List.of(
                new Segment(5, 5, 3, Segment.Direction.UP),
                new Segment(5, 2, 10, Segment.Direction.RIGHT),
                new Segment(15, 2, 3, Segment.Direction.DOWN)
        ));

        r = Route.createTestRoute(segments);
        r.simpleMutation(index, force);
        assertEquals("Simple mutation test should work #13", actual, r.segments);
    }

    @Test
    @DisplayName("Advanced mutation should work")
    public void testAdvancedMutation(){
        ArrayList<Segment> segments = new ArrayList<>(List.of(
                new Segment(5, 5, 10, Segment.Direction.RIGHT),
                new Segment(15, 5, 15, Segment.Direction.UP),
                new Segment(15, -10, 10, Segment.Direction.RIGHT)
        ));
        int index = 1;
        int force = 3;
        int cut = 5;
        ArrayList<Segment> actual = new ArrayList<>(List.of(
                new Segment(5, 5, 10, Segment.Direction.RIGHT),
                new Segment(15, 5, 5, Segment.Direction.UP),
                new Segment(15, 0, 3, Segment.Direction.RIGHT),
                new Segment(18, 0, 10, Segment.Direction.UP),
                new Segment(18, -10, 7, Segment.Direction.RIGHT)
        ));
        Route r = Route.createTestRoute(segments);
        r.advancedMutation(index, force, cut);
        assertEquals("Advanced mutation test should work #1", actual, r.segments);

        segments = new ArrayList<>(List.of(
                new Segment(5, 5, 10, Segment.Direction.RIGHT),
                new Segment(15, 5, 15, Segment.Direction.UP),
                new Segment(15, -10, 10, Segment.Direction.RIGHT)
        ));
        index = 1;
        force = -3;
        cut = 5;
        actual = new ArrayList<>(List.of(
                new Segment(5, 5, 10, Segment.Direction.RIGHT),
                new Segment(15, 5, 5, Segment.Direction.UP),
                new Segment(15, 0, 3, Segment.Direction.LEFT),
                new Segment(12, 0, 10, Segment.Direction.UP),
                new Segment(12, -10, 13, Segment.Direction.RIGHT)
        ));
        r = Route.createTestRoute(segments);
        r.advancedMutation(index, force, cut);
        assertEquals("Advanced mutation test should work #2", actual, r.segments);

        segments = new ArrayList<>(List.of(
                new Segment(5, 5, 10, Segment.Direction.RIGHT),
                new Segment(15, 5, 15, Segment.Direction.UP),
                new Segment(15, -10, 10, Segment.Direction.RIGHT)
        ));
        index = 1;
        force = 10;
        cut = 5;
        actual = new ArrayList<>(List.of(
                new Segment(5, 5, 10, Segment.Direction.RIGHT),
                new Segment(15, 5, 5, Segment.Direction.UP),
                new Segment(15, 0, 10, Segment.Direction.RIGHT),
                new Segment(25, 0, 10, Segment.Direction.UP)
        ));
        r = Route.createTestRoute(segments);
        r.advancedMutation(index, force, cut);
        assertEquals("Advanced mutation test should work #3", actual, r.segments);

        segments = new ArrayList<>(List.of(
                new Segment(5, 5, 10, Segment.Direction.RIGHT),
                new Segment(15, 5, 15, Segment.Direction.UP),
                new Segment(15, -10, 20, Segment.Direction.LEFT)
        ));
        index = 1;
        force = 10;
        cut = 5;
        actual = new ArrayList<>(List.of(
                new Segment(5, 5, 10, Segment.Direction.RIGHT),
                new Segment(15, 5, 5, Segment.Direction.UP),
                new Segment(15, 0, 10, Segment.Direction.RIGHT),
                new Segment(25, 0, 10, Segment.Direction.UP),
                new Segment(25, -10, 30, Segment.Direction.LEFT)
        ));
        r = Route.createTestRoute(segments);
        r.advancedMutation(index, force, cut);
        assertEquals("Advanced mutation test should work #4", actual, r.segments);

        segments = new ArrayList<>(List.of(
                new Segment(5, 5, 10, Segment.Direction.RIGHT),
                new Segment(15, 5, 15, Segment.Direction.UP),
                new Segment(15, -10, 20, Segment.Direction.LEFT)
        ));
        index = 1;
        force = -10;
        cut = 5;
        actual = new ArrayList<>(List.of(
                new Segment(5, 5, 10, Segment.Direction.RIGHT),
                new Segment(15, 5, 5, Segment.Direction.UP),
                new Segment(15, 0, 10, Segment.Direction.LEFT),
                new Segment(5, 0, 10, Segment.Direction.UP),
                new Segment(5, -10, 10, Segment.Direction.LEFT)
        ));
        r = Route.createTestRoute(segments);
        r.advancedMutation(index, force, cut);
        assertEquals("Advanced mutation test should work #5", actual, r.segments);

        segments = new ArrayList<>(List.of(
                new Segment(5, 5, 10, Segment.Direction.RIGHT),
                new Segment(15, 5, 15, Segment.Direction.UP),
                new Segment(15, -10, 10, Segment.Direction.LEFT)
        ));
        index = 1;
        force = -10;
        cut = 5;
        actual = new ArrayList<>(List.of(
                new Segment(5, 5, 10, Segment.Direction.RIGHT),
                new Segment(15, 5, 5, Segment.Direction.UP),
                new Segment(15, 0, 10, Segment.Direction.LEFT),
                new Segment(5, 0, 10, Segment.Direction.UP)
        ));
        r = Route.createTestRoute(segments);
        r.advancedMutation(index, force, cut);
        assertEquals("Advanced mutation test should work #6", actual, r.segments);

    }

}
