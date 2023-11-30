package com.sojrel.saccoapi.flashapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LipaNaMpesaDto {
    private Long BusinessShortCode;// ": "174379",
    private String Password;//": "MTc0Mzc5YmZiMjc5ZjlhYTliZGJjZjE1OGU5N2RkNzFhNDY3Y2QyZTBjODkzMDU5YjEwZjc4ZTZiNzJhZGExZWQyYzkxOTIwMTYwMjE2MTY1NjI3",
    private Long Timestamp;//":"20160216165627",
    private String TransactionType;//": "CustomerPayBillOnline",
    private int Amount;//": "1",
    private Long PartyA;//":"254708374149",
    private Long PartyB;//":"174379",
    private Long PhoneNumber;//":"254708374149",
    private String CallBackURL;//": "https://mydomain.com/pat",the url that will be used to get the response body on payment details and
    private String AccountReference;//":"Test",
    private String TransactionDesc;//flashloanId
}
