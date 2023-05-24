package com.example.isochannelproject;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class ResStatus {
    @JsonInclude(JsonInclude.Include.NON_NULL)
private String successfullTransaction;
    @JsonInclude(JsonInclude.Include.NON_NULL)
private String failedTransaction;
}
