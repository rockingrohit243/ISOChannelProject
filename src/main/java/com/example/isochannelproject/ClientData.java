package com.example.isochannelproject;

import lombok.Data;

@Data
public class ClientData {
    private String amount;
    private String processingCode;
    private String pan;
    private String rrn;
    private  CustomerDetails customerDetails;
    @Data
    public static class CustomerDetails{
        private String name;
        private String address;

    }


}
