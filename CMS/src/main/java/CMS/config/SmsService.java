package CMS.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import jakarta.annotation.PostConstruct;


@Service
public class SmsService {

    private static final Logger logger = LoggerFactory.getLogger(SmsService.class);

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.phone.number}")
    private String fromNumber;

    @PostConstruct
    public void init() {
        logger.info("Twilio Account SID: {}", accountSid);
        logger.info("Twilio Auth Token: {}", authToken);
        logger.info("Twilio Phone Number: {}", fromNumber);

        Twilio.init(accountSid, authToken);
    }

    public void sendSms(String to, String message) {
        if (to.equals(fromNumber)) {
            throw new IllegalArgumentException("'To' and 'From' number cannot be the same.");
        }

        Message.creator(
                new PhoneNumber(to),
                new PhoneNumber(fromNumber),
                message
        ).create();
    }
}
