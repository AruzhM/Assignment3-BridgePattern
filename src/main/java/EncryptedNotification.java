import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class EncryptedNotification extends Notification {

    public EncryptedNotification(MessageSender sender) {
        super(sender);
    }

    @Override
    public void send(String recipient, String message) {

        String encryptedMessage = Base64.getEncoder()
                .encodeToString(
                        message.getBytes(StandardCharsets.UTF_8)
                );

        if (sender.isAvailable()) {
            sender.sendMessage(
                    recipient,
                    "Encrypted Notification",
                    encryptedMessage
            );
        } else {
            System.out.println("Sender is unavailable!");
        }
    }
}

