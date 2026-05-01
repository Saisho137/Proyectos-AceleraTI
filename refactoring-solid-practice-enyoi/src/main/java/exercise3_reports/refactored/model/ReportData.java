package exercise3_reports.refactored.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReportData {
    private final String title;
    private final List<String> columns;
    private final List<List<Object>> rows;

    public ReportData(String title, List<Map<String, Object>> listData) {
        this.title = title;
        this.columns = new ArrayList<>();
        this.rows = new ArrayList<>();
        if (!listData.isEmpty()) {
            columns.addAll(listData.get(0).keySet());

            listData.forEach((data) -> {
                List<Object> rowElement = new ArrayList<>();
                columns.forEach((key) -> {
                    rowElement.add(data.get(key));
                });
                rows.add(rowElement);
            });
        }
    }

    public String getTitle() {
        return title;
    }

    public List<List<Object>> getRows() {
        return new ArrayList<>(rows);
    }

    public List<String> getColumns() {
        return new ArrayList<>(columns);
    }
}
