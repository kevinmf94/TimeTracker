package cat.uab.ds.core;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import cat.uab.ds.core.entity.Project;
import cat.uab.ds.core.utils.Clock;
import cat.uab.ds.core.utils.DetailedReportAscii;
import cat.uab.ds.core.utils.DetailedReportHTML;
import cat.uab.ds.core.utils.DetailedReportVisitor;
import cat.uab.ds.core.utils.PrintVisitor;
import cat.uab.ds.core.utils.ShortReportAscii;
import cat.uab.ds.core.utils.ShortReportHTML;
import cat.uab.ds.core.utils.ShortReportVisitor;

/**
 * Main class of TimeTracker API.
 */
public class TimeTracker implements Observer {

    private Project root = new Project("root");

    /**
     * TimeTracker: Initialize clock and root project.
     */
    public TimeTracker() {
        root.setRoot(true);
        Clock clock = Clock.getInstance();
        clock.addObserver(this);
    }

    public final void addProject(final Project project) {
        this.root.addActivity(project);
    }

    @Override
    public final void update(final Observable observable, final Object o) {
        printMenu();
    }

    /**
     * Print tree of projects ands tasks.
     *  using Visitor pattern.
     */
    private void printMenu() {
        PrintVisitor print = new PrintVisitor();
        this.root.accept(print);
        System.out.println(print.getResult());
    }

    /**
     * Save projects structure in a file.
     * @param fileName Name of file
     */
    public void save(final String fileName) {
        try {
            FileOutputStream out = new FileOutputStream(fileName);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
            objectOutputStream.writeObject(this.root);
            out.close();
            objectOutputStream.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Load project structure from a file.
     * @param fileName File to read
     */
    public void load(final String fileName) {
        try {
            FileInputStream in = new FileInputStream(fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(in);
            this.root = (Project) objectInputStream.readObject();
        } catch (java.io.IOException | ClassNotFoundException e) {
            System.out.println("Error reading the data file");
            this.root = new Project("root");
            this.root.setRoot(true);
        }
    }

    public final void generateDetailedReportAscii(
            final Date startDate, final Date endDate) {
        DetailedReportVisitor detailedReportAscii =
                new DetailedReportAscii(startDate, endDate);
        this.root.accept(detailedReportAscii);
        System.out.println(detailedReportAscii.getResult());
    }

    public final void generateShortReportAscii(
            final Date startDate, final Date endDate) {
        ShortReportVisitor shortReportAscii =
                new ShortReportAscii(startDate, endDate);
        this.root.accept(shortReportAscii);
        System.out.println(shortReportAscii.getResult());
    }

    public void generateDetailedReportHTML(
            final Date startDate, final Date endDate) {
        DetailedReportVisitor detailedReportHTML =
                new DetailedReportHTML(startDate, endDate);
        this.root.accept(detailedReportHTML);
        detailedReportHTML.getResult();
    }

    public void generateShortReportHTML(
            final Date startDate, final Date endDate) {
        ShortReportVisitor shortReportHTML =
                new ShortReportHTML(startDate, endDate);
        this.root.accept(shortReportHTML);
        shortReportHTML.getResult();

    }
}
