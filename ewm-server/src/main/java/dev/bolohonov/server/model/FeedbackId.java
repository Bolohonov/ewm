package dev.bolohonov.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FeedbackId implements Serializable {
    private Long userId;
    private Long eventId;
}
