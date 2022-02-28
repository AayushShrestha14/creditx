package com.sb.solutions.core.utils.email;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.sb.solutions.core.constant.EmailConstant;

@Service
public class MailSenderService {


    @Autowired
    private JavaMailSenderImpl javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;


    public void send(EmailConstant.Template template, Email email) {
        this.javaMailSender.send(mimeMessage -> {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
            message.setSubject(template.get());
            message.setTo(email.getTo());
            message.setFrom(new InternetAddress(javaMailSender.getUsername()));

            final Context ctx = new Context();
            ctx.setVariable("data", email);
            String content = templateEngine.process(EmailConstant.MAIL.get(template), ctx);
            message.setText(content, true);
        });
    }

    public void sendComplexMail(EmailConstant.Template template, Email dto)
        throws MessagingException, IOException {

        MimeMessage message = javaMailSender.createMimeMessage();
        message.setFrom(new InternetAddress(javaMailSender.getUsername()));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(dto.getTo()));
        List<String> ccList = dto.getCc() == null ? new ArrayList<>() : dto.getCc();
        for (String cc : ccList) {
            message.addRecipient(RecipientType.CC, new InternetAddress(cc));
        }
        List<String> bccList = dto.getBcc() == null ? new ArrayList<>() : dto.getBcc();
        for (String bcc : bccList) {
            message.addRecipient(RecipientType.BCC, new InternetAddress(bcc));
        }
        message.setSubject(template.get());

        // Create a multipart message
        Multipart multipart = new MimeMultipart();

        BodyPart messageBodyPart = new MimeBodyPart();
        final Context ctx = new Context();
        ctx.setVariable("data", dto);
        messageBodyPart
            .setContent(templateEngine.process(EmailConstant.MAIL.get(template), ctx), "text/html");

        multipart.addBodyPart(messageBodyPart);

        List<String> items =
            dto.getAttachment() == null ? new ArrayList<>() : dto.getAttachment();

        for (String attached : items) {
            if (attached != null) {
                BodyPart attachment = new MimeBodyPart();
                URL url = new URL(attached);

                attachment.setDataHandler(new DataHandler(url));
                attachment.setDisposition(Part.ATTACHMENT);
                attachment.setFileName("test");
                multipart.addBodyPart(attachment);
            }
        }
        message.setContent(multipart);

        // Send message
        javaMailSender.send(message);
    }
}
