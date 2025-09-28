package org.example.CandyCo.error;


import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
@Builder
public class ExceptionResponse {
    private int status;
    private String reason;
    private ZonedDateTime timestamp;
}
