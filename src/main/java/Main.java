import java.util.Arrays;

public class Main {


    public  static void main(String[] args) {

        MessageSender telegram = new TelegramMessageSender();
        MessageSender email = new EmailMessageSender();
        MessageSender sms = new SmsMessageSender();
        MessageSender push = new PushNotificationSender();

        System.out.println("\n=== SCENARIO 1: URGENT + TELEGRAM ===");

        Notification urgent = new UrgentNotification(telegram, sms);
        urgent.send("@student", "Server is down!");



        System.out.println("\n=== SCENARIO 2: ENCRYPTED + EMAIL ===");
        Notification encrypted = new EncryptedNotification(email);
        encrypted.send("student@gmail.com", "Secret message");


        System.out.println("\n=== SCENARIO 3: BATCH + SMS ===");



        ScheduledBatchNotification batch =
                new ScheduledBatchNotification(
                        sms,
                        Arrays.asList("111", "222", "333")
                );

        batch.sendBatch("Your class starts tomorrow!");


        System.out.println("\n=== SCENARIO 4: RUNTIME SWITCHING ===");

        Notification switching = new UrgentNotification(telegram, sms);

        switching.send("@student", "First message via Telegram");

        switching.setSender(email);

        switching.send("student@gmail.com", "Second message via Email");

        System.out.println("\n=== SCENARIO 5: FALLBACK ===");

        ((TelegramMessageSender) telegram).setAvailable(false);


        Notification fallback = new UrgentNotification(telegram, sms);

        fallback.send("123456789", "Emergency alert!");


        System.out.println("\n=== SCENARIO 6: MARKETING + PUSH ===");

        Notification marketing = new MarketingNotification(push);

        marketing.send("device123", "Get 20% discount today!");

        System.out.println("\n=== ALL SCENARIOS COMPLETED ===");


    }


}
