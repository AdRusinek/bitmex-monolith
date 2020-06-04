package com.rusinek.bitmexmonolith.services.websocket.executors;

import com.rusinek.bitmexmonolith.model.Alert;
import com.rusinek.bitmexmonolith.model.User;
import com.rusinek.bitmexmonolith.repositories.AlertRepository;
import com.rusinek.bitmexmonolith.services.MailService;
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

    private final MailService mailService;
    private final AlertRepository alertRepository;

    public void iterateAlert(Trade trade) {
        alertRepository.findAll().forEach(alert -> {
            if (alert.getDirection().equals("above")) {
                if (trade.getPrice().doubleValue() == alert.getAlertTriggeringPrice() ||
                        trade.getPrice().doubleValue() > alert.getAlertTriggeringPrice()) {
                    deleteAlert(alert, trade);
                }
            }
            if (alert.getDirection().equals("below")) {
                if (trade.getPrice().doubleValue() == alert.getAlertTriggeringPrice() ||
                        trade.getPrice().doubleValue() < alert.getAlertTriggeringPrice()) {
                    deleteAlert(alert, trade);
                }
            }
        });
    }

    private void deleteAlert(Alert alert, Trade trade) {
        int sendAlert = sendAlert(alert.getUser(), alert, trade);
        if (isEmailSent(sendAlert)) {
            alertRepository.delete(alert);
        }
    }

    private int sendAlert(User user, Alert alert, Trade trade) {

        if (alert.getAlertMessage().isEmpty()) {
            alert.setAlertMessage("Powiadomienie o cenie.");
        }

        try {
            log.debug("Attempting to sent mail to user: " + user.getUsername());
            mailService.sendMail(user.getUsername(),
                    "Price while sending: " + trade.getPrice().doubleValue(),
                    alert.getAlertMessage(),
                    "bitmexprogram@gmail.com",
                    false, null);
        } catch (MailException | MessagingException ex) {
            log.error("Mail for user '" + user.getUsername() + "' not sent");
            return 1;
        }
        return 0;
    }

    private boolean isEmailSent(int response) {
        return response == 0;
    }
}
