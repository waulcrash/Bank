package com.example.deal.dto;

public class EmailMessage {
    private String address;
    private String theme;
    private Long statementId;
    private String text;

   
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }
    public Long getStatementId() { return statementId; }
    public void setStatementId(Long statementId) { this.statementId = statementId; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}