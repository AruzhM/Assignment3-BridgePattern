import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

public class NotificationTest {

    private String captureOutput(Runnable action) {
        PrintStream original = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));
            action.run();
        } finally {
            System.setOut(original);
        }

        return output.toString(StandardCharsets.UTF_8);
    }


    @Test
    void testSuccessfulDelivery() {
        TelegramMessageSender telegram = new TelegramMessageSender();

        assertTrue(telegram.sendMessage("user", "Hello", "Test message"));
    }


    @Test
    void testUnavailableProvider() {
        TelegramMessageSender telegram = new TelegramMessageSender();
        telegram.setAvailable(false);

        assertFalse(telegram.isAvailable());
        assertFalse(telegram.sendMessage("user", "Title", "Message"));
    }


    @Test
    void testFallback() {
        TelegramMessageSender telegram = new TelegramMessageSender();
        telegram.setAvailable(false);

        SmsMessageSender sms = new SmsMessageSender();
        UrgentNotification urgent = new UrgentNotification(telegram, sms);

        String output = captureOutput(
                () -> urgent.send("12345", "Emergency")
        );

        assertTrue(output.contains("Fallback successful!"));
        assertTrue(output.contains("=== SMS ==="));
    }


    @Test
    void testSmsLengthRestriction() {
        SmsMessageSender sms = new SmsMessageSender();

        String longMessage = "A".repeat(161);

        assertFalse(sms.sendMessage("12345", "Title", longMessage));
    }


    @Test
    void testEncryptedMessage() {
        EmailMessageSender email = new EmailMessageSender();
        EncryptedNotification encrypted = new EncryptedNotification(email);

        String output = captureOutput(
                () -> encrypted.send("user@email.com", "Hello")
        );

        assertTrue(output.contains("SGVsbG8="));
    }


    @Test
    void testBatchNotification() {
        SmsMessageSender sms = new SmsMessageSender();

        ScheduledBatchNotification batch =
                new ScheduledBatchNotification(
                        sms,
                        Arrays.asList("111", "222", "333")
                );

        String output = captureOutput(
                () -> batch.sendBatch("Test message")
        );

        assertTrue(output.contains("Total: 3"));
        assertTrue(output.contains("Successful: 3"));
        assertTrue(output.contains("Failed: 0"));
    }


    @Test
    void testRuntimeSwitching() {
        TelegramMessageSender telegram = new TelegramMessageSender();
        EmailMessageSender email = new EmailMessageSender();

        Notification notification = new UrgentNotification(telegram);

        String first = captureOutput(
                () -> notification.send("user", "First")
        );

        notification.setSender(email);

        String second = captureOutput(
                () -> notification.send("user@email.com", "Second")
        );

        assertTrue(first.contains("=== TELEGRAM ==="));
        assertTrue(second.contains("=== EMAIL ==="));
    }


    @Test
    void testPushSender() {
        PushNotificationSender push = new PushNotificationSender();

        assertTrue(push.sendMessage("device123", "Title", "Message"));
        assertEquals("Push", push.getChannelName());
    }


    @Test
    void testMarketingNotification() {
        PushNotificationSender push = new PushNotificationSender();
        MarketingNotification marketing = new MarketingNotification(push);

        String output = captureOutput(
                () -> marketing.send("device123", "Discount today")
        );

        assertTrue(output.contains("SPECIAL OFFER!"));
    }


    @Test
    void testFallbackFailure() {
        TelegramMessageSender telegram = new TelegramMessageSender();
        telegram.setAvailable(false);

        SmsMessageSender sms = new SmsMessageSender();
        sms.setAvailable(false);

        UrgentNotification urgent = new UrgentNotification(telegram, sms);

        String output = captureOutput(
                () -> urgent.send("12345", "Emergency")
        );

        assertTrue(output.contains("Failed to deliver urgent notification!"));
    }
}
