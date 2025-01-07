import java.util.ArrayList;
import java.util.List;

public class LogModel {
    private List<String> logs;

    public LogModel() {
        logs = new ArrayList<>();
    }

    public void addLog(String event) {
        logs.add(event);
    }

    public List<String> getLogs() {
        return logs;
    }

    public void clearLogs() {
        logs.clear();
    }
}
