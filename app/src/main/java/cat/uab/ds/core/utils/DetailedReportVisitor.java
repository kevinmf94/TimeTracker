package cat.uab.ds.core.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import cat.uab.ds.core.entity.Activity;
import cat.uab.ds.core.entity.Interval;
import cat.uab.ds.core.entity.Project;
import cat.uab.ds.core.entity.Task;

public abstract class DetailedReportVisitor extends ReportVisitor {

    static final int POS_TASK_PROJECT = 0;
    static final int POS_TASK_NAME = 8;
    static final int POS_TASK_START = 15;
    static final int POS_TASK_END = 38;
    static final int POS_TASK_DURATION = 60;

    static final int POS_INTERVAL_PROJECT = 0;
    static final int POS_INTERVAL_TASK = 8;
    static final int POS_INTERVAL_NAME = 15;
    static final int POS_INTERVAL_START = 25;
    static final int POS_INTERVAL_END = 48;
    static final int POS_INTERVAL_DURATION = 70;

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
                insertInLine(sb, POS_PROJECT_NAME,
                        project.getName());
                insertInLine(sb, POS_PROJECT_START,
                        getDateString(report.getStart()));
                insertInLine(sb, POS_PROJECT_END,
                        getDateString(report.getEnd()));
                insertInLine(sb, POS_PROJECT_DURATION,
                        durationToStr(report.getDuration()));

                projectsResults.add(sb.toString());
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

                StringBuilder sb = new StringBuilder(WHITE_LINE);
                insertInLine(sb, POS_PROJECT_NAME, project.getName());
                insertInLine(sb, POS_PROJECT_START,
                        getDateString(report.getStart()));
                insertInLine(sb, POS_PROJECT_END,
                        getDateString(report.getEnd()));
                insertInLine(sb, POS_PROJECT_DURATION,
                        durationToStr(report.getDuration()));
                subProjectsResults.add(sb.toString());
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

            ReportInterval report = getDurationByIntervals(intervals);

            StringBuilder sb = new StringBuilder(WHITE_LINE);
            insertInLine(sb, POS_TASK_PROJECT, actualProject.getName());
            insertInLine(sb, POS_TASK_NAME, task.getName());
            insertInLine(sb, POS_TASK_START, getDateString(report.getStart()));
            insertInLine(sb, POS_TASK_END, getDateString(report.getEnd()));
            insertInLine(sb, POS_TASK_DURATION,
                    durationToStr(report.getDuration()));

            tasksResults.add(sb.toString());
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

        if (taskStartDate.after(startDate)
                && taskEndDate.before(endDate)) { //Inside task
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

            StringBuilder sb = new StringBuilder(WHITE_LINE);
            insertInLine(sb, POS_INTERVAL_PROJECT, actualProject.getName());
            insertInLine(sb, POS_INTERVAL_TASK, actualTask.getName());
            insertInLine(sb, POS_INTERVAL_NAME, Integer.toString(intervalId));
            insertInLine(sb, POS_INTERVAL_START, getDateString(startInside));
            insertInLine(sb, POS_INTERVAL_END, getDateString(endInside));
            insertInLine(sb, POS_INTERVAL_DURATION, durationToStr(duration));

            intervalsResults.add(sb.toString());
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
