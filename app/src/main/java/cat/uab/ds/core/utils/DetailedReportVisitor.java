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

public class DetailedReportVisitor extends ReportVisitor {

    private static final String[] HEADER_PROJECTS = new String[]{
            "Project", "Start Date", "End Date", "Total time"
    };
    private static final int POS_PROJECT_NAME = 0;
    private static final int POS_PROJECT_START = 12;
    private static final int POS_PROJECT_END = 35;
    private static final int POS_PROJECT_DURATION = 60;

    private static final String[] HEADER_TASKS = new String[]{
            "Project", "Task", "Start Date", "End Date", "Total time"
    };
    private static final int POS_TASK_PROJECT = 0;
    private static final int POS_TASK_NAME = 8;
    private static final int POS_TASK_START = 15;
    private static final int POS_TASK_END = 38;
    private static final int POS_TASK_DURATION = 60;

    private static final String[] HEADER_INTERVALS = new String[]{
            "Project", "Task", "Interval", "Start Date",
            "End Date", "Total time"
    };
    private static final int POS_INTERVAL_PROJECT = 0;
    private static final int POS_INTERVAL_TASK = 8;
    private static final int POS_INTERVAL_NAME = 15;
    private static final int POS_INTERVAL_START = 25;
    private static final int POS_INTERVAL_END = 48;
    private static final int POS_INTERVAL_DURATION = 70;

    private final Logger logger =
            LoggerFactory.getLogger(
                    DetailedReportVisitor.class);

    private Project actualProject;
    private Task actualTask;
    private int intervalId;
    private Collection<ReportInterval> intervals;
    private Collection<ReportInterval> intervalsProject;
    private Collection<ReportInterval> intervalsSubProject;

    private final Collection<String[]> projectsResults;
    private final Collection<String[]> subProjectsResults;
    private final Collection<String[]> tasksResults;
    private final Collection<String[]> intervalsResults;

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

                projectsResults.add(new String[]{project.getName(),
                        getDateString(report.getStart()),
                        getDateString(report.getEnd()),
                        durationToStr(report.getDuration())
                });
            }

        } else if (project.getLevel() == 2) {

            intervalsSubProject = new ArrayList<>();
            for (Activity activity: project.getActivities()) {
                if (activity instanceof Task) {
                    activity.accept(this);
                }
            }

            if (intervalsSubProject.size() > 0) {
                ReportInterval report =
                        mergeReportInterval(intervalsSubProject);

                subProjectsResults.add(new String[]{project.getName(),
                        getDateString(report.getStart()),
                        getDateString(report.getEnd()),
                        durationToStr(report.getDuration())
                });
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

            tasksResults.add(new String[]{actualProject.getName(),
                    task.getName(),
                    getDateString(report.getStart()),
                    getDateString(report.getEnd()),
                    durationToStr(report.getDuration())
            });
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

            intervalsResults.add(new String[]{actualProject.getName(),
                    actualTask.getName(),
                    Integer.toString(intervalId),
                    getDateString(startInside),
                    getDateString(endInside),
                    durationToStr(duration)
            });
        }
    }

    public final void generate() {
        ReportFormat reportFormat = getReportFormat();
        reportFormat.addHeader("Detailed report", getStartDate(), getEndDate());
        reportFormat.addLine();
        reportFormat.addText("Root projects");
        reportFormat.addTable(HEADER_PROJECTS, projectsResults, new int[]{
                POS_PROJECT_NAME, POS_PROJECT_START, POS_PROJECT_END,
                POS_PROJECT_DURATION});
        reportFormat.addLine();
        reportFormat.addText("Sub projects");
        reportFormat.addTable(HEADER_PROJECTS, subProjectsResults, new int[]{
                POS_PROJECT_NAME, POS_PROJECT_START, POS_PROJECT_END,
                POS_PROJECT_DURATION});
        reportFormat.addLine();
        reportFormat.addText("Tasks");
        reportFormat.addTable(HEADER_TASKS, tasksResults, new int[]{
                POS_TASK_PROJECT, POS_TASK_NAME, POS_TASK_START, POS_TASK_END,
                POS_TASK_DURATION});
        reportFormat.addLine();
        reportFormat.addText("Intervals");
        reportFormat.addTable(HEADER_INTERVALS, intervalsResults, new int[]{
                POS_INTERVAL_PROJECT, POS_INTERVAL_TASK, POS_INTERVAL_NAME,
                POS_INTERVAL_START, POS_INTERVAL_END, POS_INTERVAL_DURATION});
        reportFormat.addLine();
        reportFormat.generate();
    }

}
