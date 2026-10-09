import java.util.List;

public class ScheduledBatchNotification extends Notification {

    private final List<String> recipients;

    public ScheduledBatchNotification(
            MessageSender sender,
            List<String> recipients
    ) {
        super(sender);
        this.recipients = recipients;
    }

    @Override
    public void send(String recipient, String message) {
        sendBatch(message);
    }

    public void sendBatch(String message) {

        int successful = 0;
        int failed = 0;

        for (String recipient : recipients) {

            if (sender.isAvailable() &&
                    sender.sendMessage(
                            recipient,
                            "Batch Notification",
                            message
                    )) {
                successful++;
            } else {
                failed++;
            }
        }

        System.out.println("=== BATCH SUMMARY ===");
        System.out.println("Total: " + recipients.size());
        System.out.println("Successful: " + successful);
        System.out.println("Failed: " + failed);
    }
}
