package walter.example.lab.service;

import lombok.Value;

import java.util.UUID;

@Value
public class ResponseDTO {

    String id;

    public ResponseDTO(String id) {
        this.id = id;
    }

    public ResponseDTO(UUID id) {
        this.id = id.toString();
    }
}
