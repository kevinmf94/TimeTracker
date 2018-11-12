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
public class DetailedReportAscii extends ReportAsciiVisitor {

    private StringBuilder header;
    private StringBuilder rootProjects;
    private StringBuilder subProjects;
    private StringBuilder tasks;
    private StringBuilder intervalsString;

    private Project actualProject;
    private Task actualTask;
    private int intervalId;
    private Collection<Interval> intervals;
    private Collection<Interval> intervalsProject;
    private Collection<Interval> intervalsSubProject;

    /**
     * Initialize basic menu info with table header.
     * @param newStartDate Start period
     * @param newEndDate End period
     */
    public DetailedReportAscii(final Date newStartDate, final Date newEndDate) {
        super(newStartDate, newEndDate);

        this.header = new StringBuilder();
        this.rootProjects = new StringBuilder();
        this.subProjects = new StringBuilder();
        this.tasks = new StringBuilder();
        this.intervalsString = new StringBuilder();
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

        setActiveStringBuilder(subProjects);
        writeLine();
        writeTxt("Subprojects");
        writeTxt("No. Project Start Date End Date Total time");

        setActiveStringBuilder(tasks);
        writeLine();
        writeTxt("Tasks");
        writeTxt("No.Project Task Start Date End Date Total time");

        setActiveStringBuilder(intervalsString);
        writeLine();
        writeTxt("Intervals");
        writeTxt("No.Project Task Interval Start Date End Date Total time");
    }

    @Override
    public final void visit(final Project project) {
        actualProject = project;

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

            intervalsSubProject = new ArrayList<>();
            for (Activity activity: project.getActivities()) {
                if (activity instanceof Task) {
                    activity.accept(this);
                }
            }

            if (intervalsSubProject.size() > 0) {
                setActiveStringBuilder(subProjects);
                ReportInterval report = getDurationByIntervals(
                        intervalsSubProject);

                writeTxt(project.getName() + " "
                        + getDateFormated(report.getStart())
                        + " " + getDateFormated(report.getEnd()) + " "
                        + durationToStr(report.getDuration()));
            }
        }
    }

    @Override
    public final void visit(final Task task) {
        intervals = new ArrayList<>();
        actualTask = task;
        ArrayList<Interval> intervalsTask = task.getIntervals();

        for (int i = 0; i < intervalsTask.size(); i++) {
            intervalId = i + 1;
            intervalsTask.get(i).accept(this);
        }

        if (intervals.size() > 0) {
            intervalsProject.addAll(intervals);
            if (intervalsSubProject != null) {
                intervalsSubProject.addAll(intervals);
            }
            setActiveStringBuilder(tasks);
            ReportInterval report = getDurationByIntervals(intervals);
            writeTxt(actualProject.getName() + " " + task.getName()
                    + " " + getDateFormated(report.getStart()) + " "
                    + getDateFormated(report.getEnd())
                    + " " + durationToStr(report.getDuration())
                );
        }
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
            startInside = taskStartDate;
            endInside = taskEndDate;
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
            setActiveStringBuilder(intervalsString);
            writeTxt(actualProject.getName() + "\t" + actualTask.getName()
                    + "\t" + intervalId + " " + getDateFormated(startInside)
                    + "\t" + getDateFormated(endInside)
                    + "\t" + durationToStr(duration));

        }
    }

    @Override
    public final String getResult() {
        return header.toString() + rootProjects.toString()
                + subProjects.toString() + tasks.toString()
                + intervalsString.toString();
    }
}
