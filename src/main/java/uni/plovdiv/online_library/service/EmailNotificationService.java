package uni.plovdiv.online_library.service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import uni.plovdiv.online_library.jpa.BookRepository;
import uni.plovdiv.online_library.jpa.TakenBookRepository;
import uni.plovdiv.online_library.model.Book;
import uni.plovdiv.online_library.model.TakenBook;

import java.util.*;

@Service
public class EmailNotificationService {

    private TakenBookRepository takenBookRepository;
    private BookRepository bookRepository;

    public EmailNotificationService(TakenBookRepository takenBookRepository, BookRepository bookRepository) {
        this.takenBookRepository = takenBookRepository;
        this.bookRepository = bookRepository;
    }

    public static void sendEmail(String userEmail, String bookTitle) {
        String subject = "Reminder: Book Return Due Soon";
        String msg = "Dear " + userEmail + ",\n\n" +
                "This is a reminder that the book titled \"" + bookTitle + "\" " +
                "is due for return in less than 3 days. Please return it on time.\n\n" +
                "Thank you!";

        String fromEmail = "myschedule.fmi@gmail.com";
        String password = "***";

        String host = "smtp.gmail.com";
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.addRecipient(jakarta.mail.Message.RecipientType.TO, new InternetAddress(userEmail));
            message.setSubject(subject);
            message.setText(msg);

            Transport.send(message);
            System.out.println("Email sent successfully!");
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Error: Unable to send email.");
        }
    }

    @Scheduled(cron = "0 0 0 * * *") //every day at 00:00
    public void checkAndSendReminders() {
        Date currentDate = new Date();
        System.out.println("Current Date: " + currentDate);

        // Subtract 27 days
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_MONTH, -27);
        Date threeDaysDue = calendar.getTime();
        List<TakenBook> booksToReturnSoon = takenBookRepository.findAllDue(threeDaysDue);
        List<Book> books = bookRepository.findAllById(
                booksToReturnSoon
                .stream()
                .map(TakenBook::getBookId)
                .toList()
        );
        for (TakenBook book : booksToReturnSoon) {
            String userEmail = book.getTakenFrom();
            if (userEmail != null) {
                sendEmail(userEmail, books.stream().filter(b -> b.getId().equals(book.getBookId())).findFirst().get().getTitle());
            }
        }
    }
}