package cat.uab.ds.core.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import cat.uab.ds.core.entity.Activity;
import cat.uab.ds.core.entity.Interval;
import cat.uab.ds.core.entity.Project;
import cat.uab.ds.core.entity.Task;

/**
 * Visitor in charge of read an Activity (Task or Project) and print it in
 * console.
 */
public class ShortReportAscii extends ReportAsciiVisitor {

    private StringBuilder header;
    private StringBuilder rootProjects;

    private Collection<Interval> intervals;
    private Collection<Interval> intervalsProject;

    /**
     * Initialize basic menu info with table header.
     * @param newStartDate Start period
     * @param newEndDate End period
     */
    public ShortReportAscii(final Date newStartDate, final Date newEndDate) {
        super(newStartDate, newEndDate);

        this.header = new StringBuilder();
        this.rootProjects = new StringBuilder();

        headersReport();
    }

    private void headersReport() {
        setActiveStringBuilder(header);
        writeLine();
        writeTxt("Detailed report");
        writeLine();
        writeTxt("Period");
        writeTxt("Date");
        writeTxt("From " + getDateFormated(getStartDate()));
        writeTxt("To " + getDateFormated(getEndDate()));
        writeTxt("Date from generation of report "
                + getDateFormated(new Date()));

        setActiveStringBuilder(rootProjects);
        writeLine();
        writeTxt("Root projects");
        writeTxt("No. Project Start Date End Date Total time");
    }

    @Override
    public final void visit(final Project project) {

        if (project.getLevel() == 0) {
            for (Activity activity: project.getActivities()) {
                activity.accept(this);
            }
        } else if (project.getLevel() == 1) {

            intervalsProject = new ArrayList<>();

            for (Activity activity: project.getActivities()) {
                activity.accept(this);
            }

            if (intervalsProject.size() > 0) {
                setActiveStringBuilder(rootProjects);

                ReportInterval report = getDurationByIntervals(
                        intervalsProject);

                writeTxt(project.getName() + " "
                        + getDateFormated(report.getStart())
                        + " " + getDateFormated(report.getEnd()) + " "
                        + durationToStr(report.getDuration()));
            }

        } else if (project.getLevel() == 2) {

            for (Activity activity: project.getActivities()) {
                if (activity instanceof Task) {
                    activity.accept(this);
                }
            }
        }
    }

    @Override
    public final void visit(final Task task) {
        intervals = new ArrayList<>();
        ArrayList<Interval> intervalsTask = task.getIntervals();

        for (int i = 0; i < intervalsTask.size(); i++) {
            intervalsTask.get(i).accept(this);
        }

        intervalsProject.addAll(intervals);
    }

    @Override
    public final void visit(final Interval interval) {
        Date taskStartDate = interval.getStart();
        Date taskEndDate = interval.getEnd();
        Date startDate = getStartDate();
        Date endDate = getEndDate();
        int duration = 0;

        Date startInside = null;
        Date endInside = null;

        if (taskStartDate.compareTo(startDate) >= 0
                && taskEndDate.compareTo(endDate) <= 0) {
            duration = (int) interval.getDuration();

        } else if (taskStartDate.compareTo(startDate) < 0
                && taskEndDate.compareTo(startDate) > 0
                && taskEndDate.compareTo(endDate) < 0) {
            startInside = startDate;
            endInside = taskEndDate;
            duration = Interval.getDuration(startInside, endInside);
        }

        //TODO Faltan casos

        if (duration > 0) {
            intervals.add(interval);
        }
    }

    @Override
    public final String getResult() {
        return header.toString() + rootProjects.toString();
    }
}
