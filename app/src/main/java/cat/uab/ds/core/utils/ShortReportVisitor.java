package cat.uab.ds.core.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import cat.uab.ds.core.entity.Activity;
import cat.uab.ds.core.entity.Interval;
import cat.uab.ds.core.entity.Project;
import cat.uab.ds.core.entity.Task;

public abstract class ShortReportVisitor extends ReportVisitor {

    private Collection<Interval> intervals;
    private Collection<Interval> intervalsProject;

    private Collection<String> projectsResults;

    /**
     * Initialize basic menu info with table header.
     * @param newStartDate Start period
     * @param newEndDate End period
     */
    public ShortReportVisitor(final Date newStartDate, final Date newEndDate) {
        super(newStartDate, newEndDate);

        projectsResults = new ArrayList<>();
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
                ReportInterval report = getDurationByIntervals(
                        intervalsProject);

                StringBuilder sb = new StringBuilder(WHITE_LINE);
                insertInLine(sb, POS_PROJECT_NAME, project.getName());
                insertInLine(sb, POS_PROJECT_START,
                        getDateString(report.getStart()));
                insertInLine(sb, POS_PROJECT_END,
                        getDateString(report.getEnd()));
                insertInLine(sb, POS_PROJECT_DURATION,
                        durationToStr(report.getDuration()));

                projectsResults.add(sb.toString());
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

        if (taskStartDate.after(startDate)
                && taskEndDate.before(endDate)) { //Inside task
            duration = (int) interval.getDuration();
        } else if (taskStartDate.before(startDate)
                && taskEndDate.after(startDate)
                && taskEndDate.before(endDate)) { //Left cut task
            duration = Interval.getDuration(startDate, taskEndDate);
        } else if (taskStartDate.after(startDate)
                && taskStartDate.before(endDate)
                && taskEndDate.after(endDate)) { //Right cut task
            duration = Interval.getDuration(taskStartDate, endDate);
        } else if (taskStartDate.before(startDate)
                && taskEndDate.after(endDate)) { //All fill task
            duration = Interval.getDuration(startDate, endDate);
        }

        if (duration > 0) {
            intervals.add(interval);
        }
    }

    protected abstract void headersReport();
    protected abstract void projectReport();

    public final Collection<String> getProjectsResults() {
        return projectsResults;
    }
}
