package com.example.socialApp.chat.converter;

import com.example.socialApp.chat.model_repo.Chat;
import com.example.socialApp.message.converter.MessageDateComparator;
import com.example.socialApp.message.model_repo.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatComparator implements Comparator<Chat> {

    private final MessageDateComparator messageDateComparator;

    @Override
    public int compare(Chat o1, Chat o2) {
        if (o1.getMessages().isEmpty() && o2.getMessages().isEmpty()) {
            return 0;
        } else if (o1.getMessages().isEmpty()) {
            return 1;
        } else if (o2.getMessages().isEmpty()) {
            return -1;
        }

        Optional<Message> m1 = o1.getMessages().stream().max(Comparator.comparing(Message::getWriteDate));
        Optional<Message> m2 = o2.getMessages().stream().max(Comparator.comparing(Message::getWriteDate));
        if (m1.isPresent() && m2.isPresent()) {
            return messageDateComparator.compare(m1.get(), m2.get());
        }
        return 0;
    }
}
