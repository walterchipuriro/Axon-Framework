package walter.example.lab.service;

import lombok.Value;

@Value
public class EventDTO {

    long sequenceNumber;
    String type;
    String payload;
    String timestamp;
}
