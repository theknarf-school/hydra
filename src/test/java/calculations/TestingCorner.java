package calculations;

import api.data.SimulationEdge;
import api.data.SimulationFormData;
import api.data.SimulationNode;
import factory.SimulationFactory;
import helpers.ProducerHelper;
import helpers.SimulationHelper;
import models.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestingCorner {

    SimulationHelper simulationHelper;

    @Before
    public void before() {

        simulationHelper = new SimulationHelper();
    }

    @Test
    public void testConsumeEntitiesWithConsumers() {

        Simulation simulation = new Simulation();
        List<Node> nodes = new ArrayList<>();

        Consumer consumer = new Consumer(1);

        List<Entity> entities = new ArrayList<>();
        entities.add(new Entity());
        entities.add(new Entity());
        entities.add(new Entity());
        entities.add(new Entity());
        entities.add(new Entity());

        consumer.setEntitesInQueue(entities);

        nodes.add(consumer);

        simulation.setNodes(nodes);

        simulationHelper.setSimulation(simulation);

        simulationHelper.consumeEntities(1);

        Consumer consumerResult = (Consumer) simulationHelper.getSimulation().getNodes().get(0);

        assertEquals(1, consumerResult.getEntitesConsumed().size());
        assertEquals(4, consumerResult.getEntitesInQueue().size());
    }

    @Test
    public void testConsumeEntitiesWithConsumerGroups() {

        Simulation simulation = new Simulation();
        List<Node> nodes = new ArrayList<>();

        List<Entity> entities = new ArrayList<>();
        entities.add(new Entity());
        entities.add(new Entity());
        entities.add(new Entity());
        entities.add(new Entity());
        entities.add(new Entity());

        ConsumerGroup consumerGroup = new ConsumerGroup(1, 1);
        consumerGroup.setEntitesInQueue(entities);

        nodes.add(consumerGroup);
        simulation.setNodes(nodes);


        simulationHelper.setSimulation(simulation);

        simulationHelper.consumeEntities(1);


        ConsumerGroup consumerGroupResult = (ConsumerGroup) simulationHelper.getSimulation().getNodes().get(0);

        assertEquals(1, consumerGroupResult.getConsumers().get(0).getEntitesConsumed().size());
        assertEquals(4, consumerGroupResult.getConsumers().get(0).getEntitesInQueue().size());
    }

    @Test
    public void testAddEntitesFromProducer() {

        Simulation simulation = new Simulation();

        List<Node> nodes = new ArrayList<>();

        Producer source = new Producer();
        new ProducerHelper().generateTimetable(source, 0, 1, 1, 10);
        nodes.add(source);

        Consumer target = new Consumer(0);
        nodes.add(target);

        List<Relationship> relationships = new ArrayList<>();
        relationships.add(new Relationship(source, target, 1.0));

        simulation.setNodes(nodes);
        simulation.setRelationships(relationships);

        simulationHelper.setSimulation(simulation);
        simulationHelper.addEntitiesFromProducer(0);

        Producer sourceResult = (Producer) simulationHelper.getSimulation().getNodes().get(0);
        Consumer targetResult = (Consumer) simulationHelper.getSimulation().getNodes().get(1);

        assertEquals(10, sourceResult.getEntitiesTransfered());
        assertEquals(10, targetResult.getEntitiesRecieved());
        assertEquals(10, targetResult.getEntitesInQueue().size());
    }

    @Test
    public void testAddEntitiesFromConsumers() {

        Simulation simulation = new Simulation();

        List<Node> nodes = new ArrayList<>();

        Consumer source = new Consumer(0);

        List<Entity> entities = new ArrayList<>();
        entities.add(new Entity());
        entities.add(new Entity());
        entities.add(new Entity());
        entities.add(new Entity());
        entities.add(new Entity());

        source.setEntitiesReady(entities);

        nodes.add(source);

        Consumer target = new Consumer(0);

        List<Relationship> relationships = new ArrayList<>();
        relationships.add(new Relationship(source, target, 1.0));

        nodes.add(target);

        simulation.setNodes(nodes);
        simulation.setRelationships(relationships);

        simulationHelper.setSimulation(simulation);

        simulationHelper.addEntitiesFromConsumers();

        Consumer sourceResult = (Consumer) simulationHelper.getSimulation().getNodes().get(0);
        Consumer targetResult = (Consumer) simulationHelper.getSimulation().getNodes().get(1);

        assertEquals(0, sourceResult.getEntitiesReady().size());
        assertEquals(5, targetResult.getEntitesInQueue().size());
    }

    @Test
    public void testSimulationWithBreakpoints() {

        List<Node> nodes = new ArrayList<>();

        Timetable timetable = new Timetable();
        timetable.getArrivals().add(new TimetableEntry(0, 50));
        timetable.getArrivals().add(new TimetableEntry(10, 50));
        timetable.getArrivals().add(new TimetableEntry(20, 50));

        Producer producer = new Producer(timetable);
        Consumer consumer = new Consumer(10);

        nodes.add(producer);
        nodes.add(consumer);

        List<Relationship> relationships = new ArrayList<>();
        relationships.add(new Relationship(producer, consumer, 1.0));

        Simulation simulation = new Simulation("Simulation with data which has been simulated",
                                                new Date(), nodes, relationships, 0, 100, 25);

        SimulationHelper simulationHelper = new SimulationHelper();
        simulationHelper.simulate(simulation);
        simulation = simulationHelper.getSimulation();

        // producer
        Producer producerResult = (Producer) simulation.getNodes().get(0);
        assertTrue(producerResult.getNodeDataList().size() > 0);
        assertTrue(producerResult.getProducerDataList().size() > 0);

        // consumer

        Consumer consumerrResult = (Consumer) simulation.getNodes().get(1);
        assertTrue(consumerrResult.getNodeDataList().size() > 0);
        assertTrue(consumerrResult.getConsumerDataList().size() > 0);

    }

    @Test
    public void testLessQueueWhenHigherConsumerValue() {

        List<TimetableEntry> timetableEntries = new ArrayList<TimetableEntry>() {{

            add(new TimetableEntry( 0, 5000 ));
            add(new TimetableEntry( 10, 5000 ));
            add(new TimetableEntry( 20, 5000 ));
        }};

        Timetable timetable = new Timetable(timetableEntries, "test");

        // FIRST SIM

        List<Node> n1 = new ArrayList<Node>() {{

            add(new Producer(timetable));
            add(new Consumer(10));
        }};

        List<Relationship> r1 = new ArrayList<Relationship>() {{

            add(new Relationship(n1.get(0), n1.get(1), 1.0));
        }};

        Simulation sim1 = new Simulation("Sim1", n1, r1, 1000);

        // SECOND SIM
        List<Node> n2 = new ArrayList<Node>() {{

            add(new Producer(timetable));
            add(new Consumer(600));
        }};

        List<Relationship> r2 = new ArrayList<Relationship>() {{

            add(new Relationship(n2.get(0), n2.get(1), 1.0));
        }};

        Simulation sim2 = new Simulation("Sim2", n2, r2, 1000);

        simulationHelper.simulate(sim1);
        sim1 = simulationHelper.getSimulation();

        simulationHelper.simulate(sim2);
        sim2 = simulationHelper.getSimulation();

        // waiting time (sim1 higher than sim1)
        assertTrue(sim1.getResult().getMaxWaitingTimeInTicks() == sim2.getResult().getMaxWaitingTimeInTicks());

        // entities in queue (sim1 higher than sim2)
        assertTrue(sim1.getResult().getEntitiesInQueue() < sim2.getResult().getEntitiesInQueue());

        // entities consumed (sim1 lower than sim2)
        assertTrue(sim1.getResult().getEntitiesConsumed() > sim2.getResult().getEntitiesConsumed());
    }

    @Test
    public void testSimulationFactoryWithBreakpoints() throws Exception {

        SimulationFormData formData = new SimulationFormData();

        formData.name = "Test";
        formData.startTick = 0;
        formData.ticks = 100;
        formData.breakpoints = 10;

        formData.nodes = new ArrayList<>();

        SimulationNode producer = new SimulationNode();
        producer.type = "train";
        producer.timetableId = 95;
        formData.nodes.add(producer);

        SimulationNode consumer = new SimulationNode();
        consumer.type = "door";
        consumer.ticksToConsumeEntity = 10;
        formData.nodes.add(consumer);

        formData.edges = new ArrayList<SimulationEdge>() {{

            SimulationEdge edge = new SimulationEdge();
            edge.source = formData.nodes.get(0);
            edge.target = formData.nodes.get(1);
            edge.weight = 1.0;
            add(edge);
        }};

        SimulationFactory simulationFactory = new SimulationFactory();

        Simulation simulation = simulationFactory.createSimulation(formData);

        simulationHelper.simulate(simulation);
        simulation = simulationHelper.getSimulation();

        assertTrue(simulation.getNodes().get(0).getNodeDataList().size() > 0);

    }
}
