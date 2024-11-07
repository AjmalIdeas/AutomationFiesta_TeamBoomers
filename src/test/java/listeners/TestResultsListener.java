package listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.ideas2it.utils.GChatNotifier.sendMessage;

public class TestResultsListener implements ITestListener {

    private int passedTests = 0;
    private int failedTests = 0;
    private int skippedTests = 0;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private final List<String> failedTestNames = new ArrayList<>();

    @Override
    public void onStart(ITestContext context) {
        startTime = LocalDateTime.now();
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        passedTests++;
    }

    @Override
    public void onTestFailure(ITestResult result) {
        failedTests++;
        failedTestNames.add(result.getName());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        skippedTests++;
    }

    @Override
    public void onFinish(ITestContext context) {
        int totalTests = context.getSuite().getAllMethods().size();
        int completedTests = passedTests + failedTests + skippedTests;

        if (completedTests == totalTests) {
            endTime = LocalDateTime.now();
            sendMessage(generateSummaryMessage());
        }
    }

    private String generateSummaryMessage() {
        StringBuilder message = new StringBuilder().append("🚀 *Test Execution Summary* 🚀\n").append("Start Time: ").append(formatDateTime(startTime)).append("\n").append("End Time: ").append(formatDateTime(endTime)).append("\n").append("Duration: ").append(calculateExecutionTime()).append("\n").append("✅ Passed Tests: ").append(passedTests).append("\n").append("❌ Failed Tests: ").append(failedTests).append("\n").append("⚠️ Skipped Tests: ").append(skippedTests).append("\n");

        if (failedTests > 0) {
            message.append("\n*Failed Tests Details*:\n").append(String.join("\n", failedTestNames));
        }
        return message.toString();
    }

    private String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private String calculateExecutionTime() {
        long durationInSeconds = java.time.Duration.between(startTime, endTime).getSeconds();
        long hours = durationInSeconds / 3600;
        long minutes = (durationInSeconds % 3600) / 60;
        long seconds = durationInSeconds % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
