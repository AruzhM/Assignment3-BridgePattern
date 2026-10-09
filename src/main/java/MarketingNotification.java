public class MarketingNotification extends Notification {

    public MarketingNotification(MessageSender sender) {
        super(sender);
    }

    @Override
    public void send(String recipient, String message) {

        String marketingMessage =
                "SPECIAL OFFER! " + message;

        if (sender.isAvailable()) {
            sender.sendMessage(
                    recipient,
                    "Marketing Notification",
                    marketingMessage
            );
        } else {
            System.out.println("Marketing notification failed: sender unavailable!");
        }
    }
}