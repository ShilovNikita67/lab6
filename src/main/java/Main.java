import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws Exception {
        Type listType = new TypeToken<List<Visitor>>() {}.getType();
        List<Visitor> visitors;
        try (Reader reader = new FileReader("books.json")) {
            visitors = new Gson().fromJson(reader, listType);
        }

        System.out.println("№ 1. Посетители");
        visitors.forEach(System.out::println);
        System.out.println("Количество: " + visitors.stream().count());

        List<Book> uniqueBooks = visitors.stream()
                .flatMap(v -> v.getFavoriteBooks().stream())
                .distinct()
                .collect(Collectors.toList());
        System.out.println("\n№ 2. Книги без повторений");
        uniqueBooks.forEach(System.out::println);
        System.out.println("Количество: " + uniqueBooks.stream().count());

        System.out.println("\n№ 3. Книги по году издания");
        uniqueBooks.stream()
                .sorted(Comparator.comparingInt(Book::getPublishingYear))
                .forEach(System.out::println);

        boolean hasAusten = visitors.stream()
                .flatMap(v -> v.getFavoriteBooks().stream())
                .anyMatch(b -> "Jane Austen".equals(b.getAuthor()));
        System.out.println("\n№ 4. Есть ли Jane Austen: " + hasAusten);

        int max = visitors.stream()
                .mapToInt(v -> v.getFavoriteBooks().size())
                .max()
                .orElse(0);
        System.out.println("\n№ 5. Максимум книг у одного посетителя: " + max);

        double avg = visitors.stream()
                .mapToInt(v -> v.getFavoriteBooks().size())
                .average()
                .orElse(0);
        List<SmsMessage> sms = visitors.stream()
                .filter(Visitor::isSubscribed)
                .map(v -> {
                    int n = v.getFavoriteBooks().size();
                    String text = n > avg ? "you are a bookworm"
                            : n < avg ? "read more"
                            : "fine";
                    return new SmsMessage(v.getPhone(), text);
                })
                .collect(Collectors.toList());
        System.out.println("\n№ 6. SMS (среднее = " + avg + ")");
        sms.forEach(System.out::println);
    }
}