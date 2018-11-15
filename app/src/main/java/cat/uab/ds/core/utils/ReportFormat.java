package cat.uab.ds.core.utils;

import java.util.Collection;
import java.util.Date;

interface ReportFormat {
    void addLine();
    void newLine();
    void addHeader(String name, Date start, Date end);
    void addTable(String[] header, Collection<String[]> rows,
                  int[] columnsSizes);
    void addText(String text);

    void generate();
}
