package com.grinnotech.patients.mongodb.events;

import com.grinnotech.patients.mongodb.model.User;
import com.grinnotech.patients.mongodb.services.SequenceGeneratorService;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

@Component
public class UserModelListener extends AbstractMongoEventListener<User> {

    private SequenceGeneratorService sequenceGenerator;

    public UserModelListener(SequenceGeneratorService sequenceGenerator) {
        this.sequenceGenerator = sequenceGenerator;
    }

    @Override
    public void onBeforeConvert(@NotNull BeforeConvertEvent<User> event) {
//        if (event.getSource().getId().compareTo("") == 0) {
//            event.getSource().setId(sequenceGenerator.generateSequence(User.SEQUENCE_NAME));
//        }
    }


}
