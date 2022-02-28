package com.sb.solutions.api.userNotification.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sb.solutions.api.authorization.entity.Role;
import com.sb.solutions.api.authorization.service.RoleService;
import com.sb.solutions.api.loan.entity.CustomerLoan;
import com.sb.solutions.api.loan.service.CustomerLoanService;
import com.sb.solutions.api.user.entity.User;
import com.sb.solutions.api.user.service.UserService;
import com.sb.solutions.api.userNotification.entity.Message;
import com.sb.solutions.api.userNotification.repository.MessageRepository;
import com.sb.solutions.api.userNotification.repository.specification.NotificationSpecBuilder;
import com.sb.solutions.core.enums.DocAction;
import com.sb.solutions.core.enums.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;
    private final RoleService roleService;
    private final CustomerLoanService customerLoanService;


    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    @Override
    public Message findOne(Long id) {
        return messageRepository.getOne(id);
    }

    @Override
    public Message save(Message message) {
        User fromUser = userService.findOne(message.getFromId());
        Role fromRole = roleService.findOne(message.getFromRole());

        if (message.getId() == null) {
            message.setStatus(Status.ACTIVE);
            User toUser = userService.findOne(message.getToUserId());
            Role toRole = roleService.findOne(message.getToRoleId());
            CustomerLoan customerLoan = customerLoanService.findOne(message.getCustomerId());

            if (message.getDocAction().equals(DocAction.FORWARD)
                || message.getDocAction().equals(DocAction.BACKWARD)
                || message.getDocAction().equals(DocAction.TRANSFER)) {
                message.setMessage(String.format(
                    "%s (%s) has %s a loan document of Customer %s of limit Rs. %s to %s (%s).",
                    fromUser.getName(),
                    fromRole.getRoleName(),
                    message.getDocAction().toString().toLowerCase(),
                    customerLoan.getLoanHolder().getName(),
                    customerLoan.getProposal().getProposedLimit(),
                    toUser.getName(),
                    toRole.getRoleName()));
            } else if (message.getDocAction().equals(DocAction.APPROVED)
                || message.getDocAction().equals(DocAction.REJECT)
                || message.getDocAction().equals(DocAction.CLOSED)
                || message.getDocAction().equals(DocAction.BACKWARD_TO_COMMITTEE)) {
                message.setMessage(
                    String.format("%s (%s) has %s a loan document of Customer %s of limit Rs. %s.",
                        fromUser.getName(),
                        fromRole.getRoleName(),
                        message.getDocAction().toString().toLowerCase(),
                        customerLoan.getLoanHolder().getName(),
                        customerLoan.getProposal().getProposedLimit()));
            }
        }
        return messageRepository.save(message);
    }

    @Override
    public Page<Message> findAllPageable(Object t, Pageable pageable) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> s = objectMapper.convertValue(t, Map.class);
        final NotificationSpecBuilder notificationSpecBuilder = new NotificationSpecBuilder(s);
        final Specification<Message> specification = notificationSpecBuilder.build();
        return messageRepository.findAll(specification, pageable);
    }

    @Override
    public List<Message> saveAll(List<Message> list) {
        return messageRepository.saveAll(list);
    }

}
