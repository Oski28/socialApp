package com.example.socialApp.message.converter;

import com.example.socialApp.message.model_repo.Message;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Service
public class MessageDateComparator implements Comparator<Message> {
    @Override
    public int compare(Message o1, Message o2) {
        if (o1.getWriteDate().isBefore(o2.getWriteDate())) {
            return 1;
        } else if (o1.getWriteDate().isAfter(o2.getWriteDate())) {
            return -1;
        } else {
            return 0;
        }
    }
}
