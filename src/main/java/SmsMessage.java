import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SmsMessage {
    private String phoneNumber;
    private String message;
}