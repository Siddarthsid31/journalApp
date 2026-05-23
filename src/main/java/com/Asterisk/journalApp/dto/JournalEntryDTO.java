package com.Asterisk.journalApp.dto;

import com.Asterisk.journalApp.enums.Sentiment;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JournalEntryDTO {
    private String title;
    private String content;
    private Sentiment sentiment;
}
