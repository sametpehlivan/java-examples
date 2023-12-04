package bank.domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Debt {
    public String userIdentifier;
    public LocalDate dueDate;
    public double amount;
    public String description;
    public long id;

    @Override
    public String toString() {
        return "[No: "+id+" ,Borç Adı: "+description+" ,Ödenecek Miktar: "+amount+" ,Son Ödeme Tarihi : "+dueDate.toString()+"]";
    }
    public String getLocaleDate(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return dueDate.format(formatter);
    }
}
