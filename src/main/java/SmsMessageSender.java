public class SmsMessageSender implements MessageSender {

    private boolean available = true;
    private static final int MAX_LENGTH = 160;

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public boolean sendMessage(String recipient, String title, String body) {
        if (!isAvailable()) {
            System.out.println("SMS is unavailable!");
            return false;
        }

        if (body.length() > MAX_LENGTH) {
            System.out.println("SMS failed: message exceeds 160 characters!");
            return false;
        }

        System.out.println("=== SMS ===");
        System.out.println("To: " + recipient);
        System.out.println("Message: " + body);
        return true;
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    @Override
    public String getChannelName() {
        return "SMS";
    }
}
