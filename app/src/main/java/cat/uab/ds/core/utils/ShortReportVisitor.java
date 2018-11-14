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

public abstract class ShortReportVisitor extends ReportVisitor {

    public static final String SEPARATOR = "|";

    private final Logger logger =
            LoggerFactory.getLogger(
                    ShortReportVisitor.class);

    private Collection<ReportInterval> intervals;
    private Collection<ReportInterval> intervalsProject;

    private Collection<String> projectsResults;

    /**
     * Initialize basic menu info with table header.
     * @param newStartDate Start period
     * @param newEndDate End period
     * @param newReportFormat Report format
     */
    public ShortReportVisitor(final Date newStartDate, final Date newEndDate,
                              final ReportFormat newReportFormat) {
        super(newStartDate, newEndDate, newReportFormat);

        projectsResults = new ArrayList<>();
    }

    /**
     * Visit project and their task or intervals and make the project's report.
     * @param project
     */
    @Override
    public final void visit(final Project project) {

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
                        + SEPARATOR + durationToStr(report.getDuration()));
            }

        } else if (project.getLevel() == 2) {

            for (Activity activity: project.getActivities()) {
                if (activity instanceof Task) {
                    activity.accept(this);
                }
            }
        }
    }
    /**
     * Visit task and their intervals and make the task's report.
     * @param task
     */
    @Override
    public final void visit(final Task task) {
        intervals = new ArrayList<>();
        ArrayList<Interval> intervalsTask = task.getIntervals();

        for (int i = 0; i < intervalsTask.size(); i++) {
            intervalsTask.get(i).accept(this);
        }

        intervalsProject.addAll(intervals);
    }
    /**
     * Visit interval and make the intervals's report.
     * @param interval
     */
    @Override
    public final void visit(final Interval interval) {

        ReportInterval reportInterval = convertToReportInterval(interval);

        if (reportInterval != null) {
            intervals.add(reportInterval);
        }
    }

    protected abstract void headersReport();
    protected abstract void projectReport();

    public final Collection<String> getProjectsResults() {
        return projectsResults;
    }
}
