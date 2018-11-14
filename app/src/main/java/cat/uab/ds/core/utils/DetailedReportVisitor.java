package cat.uab.ds.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import cat.uab.ds.core.entity.Activity;
import cat.uab.ds.core.entity.Interval;
import cat.uab.ds.core.entity.Project;
import cat.uab.ds.core.entity.Task;

public abstract class DetailedReportVisitor extends ReportVisitor {

    private final Logger logger =
            LoggerFactory.getLogger(
                    DetailedReportVisitor.class);

    private Project actualProject;
    private Task actualTask;
    private int intervalId;
    private Collection<Interval> intervals;
    private Collection<Interval> intervalsProject;
    private Collection<Interval> intervalsSubProject;

    private Collection<String> projectsResults;
    private Collection<String> subProjectsResults;
    private Collection<String> tasksResults;
    private Collection<String> intervalsResults;

    /**
     * Initialize basic menu info with table header.
     * @param newStartDate Start period
     * @param newEndDate End period
     */
    public DetailedReportVisitor(
            final Date newStartDate, final Date newEndDate) {
        super(newStartDate, newEndDate);

        projectsResults = new ArrayList<>();
        subProjectsResults = new ArrayList<>();
        tasksResults = new ArrayList<>();
        intervalsResults = new ArrayList<>();
    }

    @Override
    public final void visit(final Project project) {
        actualProject = project;

        logger.info("Visit project " + project.getName());

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

                ReportInterval report = getDurationByIntervals(
                        intervalsProject);

                projectsResults.add(project.getName() + SEPARATOR
                        + getDateString(report.getStart())
                        + SEPARATOR + getDateString(report.getEnd())
                        + SEPARATOR
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
                ReportInterval report = getDurationByIntervals(
                        intervalsSubProject);

                subProjectsResults.add(project.getName() + SEPARATOR
                        + getDateString(report.getStart())
                        + SEPARATOR + getDateString(report.getEnd())
                        + SEPARATOR
                        + durationToStr(report.getDuration()));
            }
        }
    }

    @Override
    public final void visit(final Task task) {

        logger.info("Visit task " + task.getName());

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

            ReportInterval report = getDurationByIntervals(intervals);
            tasksResults.add(actualProject.getName()
                    + SEPARATOR + task.getName()
                    + SEPARATOR + getDateString(report.getStart())
                    + SEPARATOR
                    + getDateString(report.getEnd())
                    + SEPARATOR + durationToStr(report.getDuration())
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

        if ((taskStartDate.after(startDate)
                || taskStartDate.equals(startDate))
                && (taskEndDate.before(endDate)
                || taskEndDate.equals(endDate))) { //Inside task
            startInside = taskStartDate;
            endInside = taskEndDate;
            duration = (int) interval.getDuration();
        } else if (taskStartDate.before(startDate)
                && taskEndDate.after(startDate)
                && taskEndDate.before(endDate)) { //Left cut task
            startInside = startDate;
            endInside = taskEndDate;
            duration = Interval.getDuration(startInside, endInside);
        } else if (taskStartDate.after(startDate)
                && taskStartDate.before(endDate)
                && taskEndDate.after(endDate)) { //Right cut task
            startInside = taskStartDate;
            endInside = endDate;
            duration = Interval.getDuration(startInside, endInside);
        } else if (taskStartDate.before(startDate)
                && taskEndDate.after(endDate)) { //All fill task
            startInside = startDate;
            endInside = endDate;
            duration = Interval.getDuration(startInside, endInside);
        }

        if (duration > 0) {
            intervals.add(interval);
            intervalsResults.add(actualProject.getName()
                    + SEPARATOR + actualTask.getName()
                    + SEPARATOR + intervalId
                    + SEPARATOR + getDateString(startInside)
                    + SEPARATOR + getDateString(endInside)
                    + SEPARATOR + durationToStr(duration));
        }
    }

    protected abstract void headersReport();
    protected abstract void projectReport();
    protected abstract void subProjectsReport();
    protected abstract void taskReport();
    protected abstract void intervalsReport();

    public Collection<String> getProjectsResults() {
        return projectsResults;
    }

    public Collection<String> getSubProjectsResults() {
        return subProjectsResults;
    }

    public Collection<String> getTasksResults() {
        return tasksResults;
    }

    public Collection<String> getIntervalsResults() {
        return intervalsResults;
    }

}
