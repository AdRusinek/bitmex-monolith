package com.rusinek.bitmexmonolith.services.websocket.observers;

import com.rusinek.bitmexmonolith.model.Alert;
import com.rusinek.bitmexmonolith.model.User;
import com.rusinek.bitmexmonolith.services.alerts.AlertService;
import com.rusinek.bitmexmonolith.services.mail.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowm.xchange.dto.marketdata.Trade;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

/**
 * Created by Adrian Rusinek on 22.03.2020
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class AlertSender {

    private final String ABOVE = "above";
    private final String BELOW = "below";

    private final AlertService alertService;
    private final MailService mailService;

    public void iterateAndSentEmail(Trade trade) {
        alertService.findAll().forEach(alert -> {
            if (alert.getDirection().equals(ABOVE)) {
                if (trade.getPrice().doubleValue() == alert.getAlertTriggeringPrice() ||
                        trade.getPrice().doubleValue() > alert.getAlertTriggeringPrice()) {
                    sendAndDecideIfDelete(alert, trade);
                }
            }
            if (alert.getDirection().equals(BELOW)) {
                if (trade.getPrice().doubleValue() == alert.getAlertTriggeringPrice() ||
                        trade.getPrice().doubleValue() < alert.getAlertTriggeringPrice()) {
                    sendAndDecideIfDelete(alert, trade);
                }
            }
        });
    }

    private void sendAndDecideIfDelete(Alert alert, Trade trade) {
        int sendAlert = sendAlert(alert.getUser(), alert, trade);
        if (isEmailIsSent(sendAlert)) {
            alertService.delete(alert);
        }
    }

    private int sendAlert(User user, Alert alert, Trade trade) {
        try {
            log.debug("Attempting to sent mail to user: " + user.getUsername());
            mailService.sendMail(user.getUsername(),
                    "Price while sending: " + trade.getPrice().doubleValue(),
                    alert.getAlertMessage(),
                    "bitmexprogram@gmail.com",
                    false);
        } catch (MailException | MessagingException ex) {
            log.debug("Mail not sent");
            return 1;
        }
        return 0;
    }

    private boolean isEmailIsSent(int response) {
        return response == 0;
    }
}
