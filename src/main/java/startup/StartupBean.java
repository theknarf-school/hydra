package startup;

import helpers.SimulationHelper;
import models.*;
import presets.OSLPreset;
import presets.SimplePreset;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * This class contains all the data that is to be persisted at deployment
 */
@Singleton
@Startup
public class StartupBean {

    // EntityManager for communications with the database.

    @PersistenceContext(unitName = "manager")
    private EntityManager entityManager;

    @EJB
    private dao.Timetable timetableDao;

    /**
     * This method is run on every deployment
     */
    @PostConstruct
    public void startup() {

        // Timetables
        setupTimetables();

        List<Timetable> timetables = timetableDao.list();

        // Simple preset
        Simulation simplePreset = new SimplePreset().createPreset(timetables.get(0));
        entityManager.persist(simplePreset);
    }

    private void setupTimetables() {
        List<TmpFileListItem> timetables = new LinkedList<TmpFileListItem>() {{
            add(new TmpFileListItem("timetables/flybussekspressen/F1/monday-friday.csv", "Flybussekspressen: F1 Monday - Friday"));
            add(new TmpFileListItem("timetables/flybussekspressen/F1/saturday.csv", "Flybussekspressen: F1 Saturday"));
            add(new TmpFileListItem("timetables/flybussekspressen/F1/sunday.csv", "Flybussekspressen: F1 Sunday"));

            add(new TmpFileListItem("timetables/flybussekspressen/F3/monday-friday.csv", "Flybussekspressen: F3 Monday - Friday"));
            add(new TmpFileListItem("timetables/flybussekspressen/F3/saturday.csv", "Flybussekspressen: F3 Saturday"));
            add(new TmpFileListItem("timetables/flybussekspressen/F3/sunday.csv", "Flybussekspressen: F3 Sunday"));

            add(new TmpFileListItem("timetables/flybussekspressen/F4/monday-friday.csv", "Flybussekspressen: F4 Monday - Friday"));
            add(new TmpFileListItem("timetables/flybussekspressen/F4/saturday-sunday.csv", "Flybussekspressen: F4 Saturday - Sunday"));

            add(new TmpFileListItem("timetables/flybussekspressen/F11/monday-friday.csv", "Flybussekspressen: F11 Monday - Friday"));
            add(new TmpFileListItem("timetables/flybussekspressen/F11/saturday.csv", "Flybussekspressen: F11 Saturday"));
            add(new TmpFileListItem("timetables/flybussekspressen/F11/sunday.csv", "Flybussekspressen: F11 Sunday"));

            add(new TmpFileListItem("timetables/flytoget/monday-friday.csv", "Flytoget: Monday - Friday"));
            add(new TmpFileListItem("timetables/flytoget/sunday.csv", "Flytoget: Sunday"));

            add(new TmpFileListItem("timetables/nettbuss/timesekspressen/monday-friday.csv", "Nettbuss Timesekspress: TE15 Monday - Friday"));
            add(new TmpFileListItem("timetables/nettbuss/timesekspressen/saturday.csv", "Nettbuss Timesekspress: TE15 Saturday"));
            add(new TmpFileListItem("timetables/nettbuss/timesekspressen/sunday.csv", "Nettbuss Timesekspress: TE15 Sunday"));

            add(new TmpFileListItem("timetables/nettbuss/shuttlebus/S22/monday-friday.csv", "Nettbuss Shuttle: S22 Monday - Friday"));
            add(new TmpFileListItem("timetables/nettbuss/shuttlebus/S22/saturday.csv", "Nettbuss Shuttle: S22 Saturday"));
            add(new TmpFileListItem("timetables/nettbuss/shuttlebus/S22/sunday.csv", "Nettbuss Shuttle: S22 Sunday"));
            add(new TmpFileListItem("timetables/nettbuss/shuttlebus/S33/monday-sunday.csv", "Nettbuss Shuttle: S33"));
            add(new TmpFileListItem("timetables/nettbuss/shuttlebus/S44/monday-friday.csv", "Nettbuss Shuttle S44: Monday - Friday"));
            add(new TmpFileListItem("timetables/nettbuss/shuttlebus/S44/saturday-sunday.csv", "Nettbuss Shuttle S44: Saturday - Sunday"));
            add(new TmpFileListItem("timetables/nettbuss/shuttlebus/S55/monday-sunday.csv", "Nettbuss Shuttle S55"));

            add(new TmpFileListItem("timetables/nettbuss/express/NX170/monday-sunday.csv", "Nettbuss Express NX170"));
            add(new TmpFileListItem("timetables/nettbuss/express/NX145/monday-sunday.csv", "Nettbuss Express NX145"));
            add(new TmpFileListItem("timetables/nettbuss/express/NX147/monday-sunday.csv", "Nettbuss Express NX147"));

            add(new TmpFileListItem("timetables/nsb/eidsvoll-kongsberg/monday-friday.csv", "NSB Eidsvoll - Kongsberg: Monday - Friday"));
            add(new TmpFileListItem("timetables/nsb/eidsvoll-kongsberg/saturday-sunday.csv", "NSB Eidsvoll - Kongsberg: Monday - Friday"));
            add(new TmpFileListItem("timetables/nsb/kongsberg-eidsvoll/monday-sunday.csv", "NSB Kongsberg - Eidsvoll"));

            add(new TmpFileListItem("timetables/nsb/lillehammer-skien/monday-friday.csv", "NSB Lillehamer - Skien: Monday - Friday"));
            add(new TmpFileListItem("timetables/nsb/lillehammer-skien/saturday.csv", "NSB Lillehamer - Skien: Saturday"));
            add(new TmpFileListItem("timetables/nsb/lillehammer-skien/sunday.csv", "NSB Lillehamer - Skien: Sunday"));
            add(new TmpFileListItem("timetables/nsb/skien-lillehammer/monday-friday.csv", "NSB Skien - Lillehamer: Monday - Friday"));
            add(new TmpFileListItem("timetables/nsb/skien-lillehammer/saturday.csv", "NSB Skien - Lillehamer: Saturday"));
            add(new TmpFileListItem("timetables/nsb/skien-lillehammer/sunday.csv", "NSB Skien - Lillehamer: Sunday"));

            add(new TmpFileListItem("timetables/nsb/trondheim-oslo/monday-friday.csv", "NSB Trondheim - Oslo: Monday - Friday"));
            add(new TmpFileListItem("timetables/nsb/trondheim-oslo/saturday.csv", "NSB Trondheim - Oslo: Saturday"));
            add(new TmpFileListItem("timetables/nsb/trondheim-oslo/sunday.csv", "NSB Trondheim - Oslo: Sunday"));

            add(new TmpFileListItem("timetables/sasflybussen/monday-friday.csv", "SAS Flybussen: Monday - Friday"));
            add(new TmpFileListItem("timetables/sasflybussen/saturday.csv", "SAS Flybussen: Saturday"));
            add(new TmpFileListItem("timetables/sasflybussen/sunday.csv", "SAS Flybussen: Sunday"));

        }};


        for(TmpFileListItem item : timetables) {
            InputStream is = StartupBean.class.getResourceAsStream(item.getFilename());
            Timetable t = Timetable.getTimetableFromCsv(is, item.getName());

            entityManager.persist(t);
        }
    }
}
