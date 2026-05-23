package com.Asterisk.journalApp.entity;

import com.Asterisk.journalApp.enums.Sentiment;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;
import java.util.Date;

// POJO class - Plain old java object
@Document(collection = "journal_entries")
@Data
@NoArgsConstructor
public class JournalEntry {

    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    @NonNull
    private String title;

    private String content;

    private LocalDateTime date;

    private Sentiment sentiment;

}
