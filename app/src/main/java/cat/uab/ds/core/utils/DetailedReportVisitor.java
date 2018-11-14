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
    private Collection<ReportInterval> intervals;
    private Collection<ReportInterval> intervalsProject;
    private Collection<ReportInterval> intervalsSubProject;

    private Collection<String> projectsResults;
    private Collection<String> subProjectsResults;
    private Collection<String> tasksResults;
    private Collection<String> intervalsResults;

    /**
     * Initialize basic menu info with table header.
     * @param newStartDate Start period
     * @param newEndDate End period
     * @param newReportFormat Report format
     */
    public DetailedReportVisitor(final Date newStartDate,
                                 final Date newEndDate,
                                 final ReportFormat newReportFormat) {
        super(newStartDate, newEndDate, newReportFormat);

        projectsResults = new ArrayList<>();
        subProjectsResults = new ArrayList<>();
        tasksResults = new ArrayList<>();
        intervalsResults = new ArrayList<>();
    }

    /**
     * Visit project and their task or intervals and make the project's report.
     * @param project
     */
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

                ReportInterval report = mergeReportInterval(intervalsProject);

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
                ReportInterval report = mergeReportInterval(intervalsSubProject);

                subProjectsResults.add(project.getName() + SEPARATOR
                        + getDateString(report.getStart())
                        + SEPARATOR + getDateString(report.getEnd())
                        + SEPARATOR
                        + durationToStr(report.getDuration()));
            }
        }
    }

    /**
     * Visit task and their intervals and make the task's report.
     * @param task
     */
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

            ReportInterval report = mergeReportInterval(intervals);
            tasksResults.add(actualProject.getName()
                    + SEPARATOR + task.getName()
                    + SEPARATOR + getDateString(report.getStart())
                    + SEPARATOR
                    + getDateString(report.getEnd())
                    + SEPARATOR + durationToStr(report.getDuration())
            );
        }
    }

    /**
     * Visit interval and make the intervals's report.
     * @param interval
     */
    @Override
    public final void visit(final Interval interval) {

        ReportInterval reportInterval = convertToReportInterval(interval);

        if (reportInterval != null) {
            Date startInside = reportInterval.getStart();
            Date endInside = reportInterval.getEnd();
            int duration = reportInterval.getDuration();

            intervals.add(reportInterval);
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
