import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.HashMap;
import java.util.Map;

public class TestStatistic {
    private SetableClock clock;
    private EventsStatistic eventsStatistic;

    @Before
    public void setUp() {
        clock = new SetableClock(Instant.now());
        eventsStatistic = new EventsStatisticImpl(clock);
    }

    @Test
    public void notExistingName(){
        Assert.assertEquals(0, eventsStatistic.getEventStatisticByName("Event"), 1e-10);
    }
    @Test
    public void emptyStatistic() {
        Assert.assertTrue(eventsStatistic.getAllEventStatistic().isEmpty());
    }
    @Test
    public void oneHourOneEvent() {
        eventsStatistic.incEvent("Event");
        eventsStatistic.incEvent("Event");
        eventsStatistic.incEvent("Event");
        eventsStatistic.incEvent("Event");
        eventsStatistic.incEvent("Event");
        Assert.assertEquals(5./60, eventsStatistic.getEventStatisticByName("Event"), 1e-10);
    }
    @Test
    public void oneHourMultiEvent() {
        eventsStatistic.incEvent("Event1");
        eventsStatistic.incEvent("Event2");
        eventsStatistic.incEvent("Event3");
        Assert.assertEquals(1./60, eventsStatistic.getEventStatisticByName("Event1"), 1e-10);
        Assert.assertEquals(1./60, eventsStatistic.getEventStatisticByName("Event2"), 1e-10);
        Assert.assertEquals(1./60, eventsStatistic.getEventStatisticByName("Event3"), 1e-10);
        Map<String, Double> statistic = eventsStatistic.getAllEventStatistic();
        Assert.assertEquals(statistic.size(), 3);
    }
    @Test
    public void deleteAfterHour() {
        eventsStatistic.incEvent("Event");
        eventsStatistic.incEvent("Event");
        eventsStatistic.incEvent("Event");
        Assert.assertEquals(3./60, eventsStatistic.getEventStatisticByName("Event"), 1e-10);

        clock.setNow(clock.getNow().plus(60, ChronoUnit.MINUTES));

        Assert.assertEquals(0./60, eventsStatistic.getEventStatisticByName("Event"), 1e-10);
    }
    @Test
    public void MultiHour() {

        eventsStatistic.incEvent("Event");
        eventsStatistic.incEvent("Event");
        Assert.assertEquals(2./60, eventsStatistic.getEventStatisticByName("Event"), 1e-10);

        clock.setNow(clock.getNow().plus(30, ChronoUnit.MINUTES));
        eventsStatistic.incEvent("Event");
        Assert.assertEquals(3./60, eventsStatistic.getEventStatisticByName("Event"), 1e-10);
        clock.setNow(clock.getNow().plus(30, ChronoUnit.MINUTES));
        Assert.assertEquals(1./60, eventsStatistic.getEventStatisticByName("Event"), 1e-10);
        clock.setNow(clock.getNow().plus(30, ChronoUnit.MINUTES));
        Assert.assertEquals(0./60, eventsStatistic.getEventStatisticByName("Event"), 1e-10);

    }
}
