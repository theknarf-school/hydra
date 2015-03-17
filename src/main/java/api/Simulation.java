package api;

import managers.ProducerManager;
import models.Consumer;
import models.Producer;
import models.Relationship;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by knarf on 04/02/15.
 */
@Path("simulation")
public class Simulation {

    //private static final Logger log = Logger.getLogger(Simulation.class.getName());

    @PersistenceContext(unitName = "manager")
    private EntityManager entityManager;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response list()
    {
        TypedQuery<models.Simulation> query = entityManager.createNamedQuery(
            "Simulation.findNotPreset",
            models.Simulation.class
        );

        return Response.ok( query.getResultList() ).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Response get(@PathParam("id") int id)
    {
        try {
            models.Simulation item = entityManager.find(models.Simulation.class, id);

            return Response.ok( item ).build();
        }
        catch (Exception e) {
            return Response.serverError().build();
        }
    }

    @Transactional
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Response delete(@PathParam("id") int id)
    {
        try {
            models.Simulation item = entityManager.find(models.Simulation.class, id);
            entityManager.remove(item);
        }
        catch (Exception e) {
            return Response.serverError().build();
        }

        return Response.ok().build();
    }

    @Transactional
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(InputValue input)
    {
        // Producer
        List<Producer> producers = new ArrayList<>();

        for(int i = 0; i < input.entitiesToProduceList.length; i++) {

            if(input.timeBetweenBusesList[i] < 1) input.timeBetweenBusesList[i] = 1;

            Producer producer = new Producer(input.entitiesToProduceList[i], null);
            new ProducerManager().generateTimetable(producer, input.startTickForProducerList[i],
                    input.timeBetweenBusesList[i], input.ticks / input.timeBetweenBusesList[i]);

            producers.add(producer);
        }

        // Consumers
        List<Relationship> relationships = new ArrayList<>();
        List<Consumer> consumers = new ArrayList<>();

        for(int i = 0; i < input.ticksToConsumeEntitiesList.length; i++) {
            Consumer consumer = new Consumer(input.ticksToConsumeEntitiesList[i]);
            Relationship relationship = new Relationship(consumer, 0.0);
            relationships.add(relationship);
            consumers.add(consumer);
        }

        for(int i = 0; i < producers.size(); i++) {
            producers.get(i).setRelationships(relationships);
        }

        // Create new object in database
        models.Simulation sim = new models.Simulation(input.name, consumers, producers, input.ticks);

        // Run and save to database
        sim.simulate();

        //sim.input = input.consumerList;
        entityManager.persist(sim);

        // Return stuff
        return Response.ok(sim.getResult()).build();

    }

}
