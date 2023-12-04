package bank.services;

import bank.Main;
import bank.domain.Debt;
import bank.services.exceptions.NotFoundException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DebtService extends Service{
    private static DebtService instance;
    private DebtService(){}
    public  static DebtService createInstance(){
        if (instance == null) {
            instance = new DebtService();
        }
        return instance;
    }
    public List<Debt> findAll() throws IOException, ParseException {
        var array = findAllJSON(Main.debtPath);
        List<Debt> debts = new ArrayList<>();
        for (Object o : array){
            JSONObject jsonObject = (JSONObject) o;
            Debt debt = new Debt();
            debt.id = (long) jsonObject.get("id");
            debt.userIdentifier = (String) jsonObject.get("user-identifier");
            debt.dueDate = stringToLocalDate((String) jsonObject.get("due-date"));
            debt.amount  = (double) jsonObject.get("amount");
            debt.description = (String) jsonObject.get("description");
            debts.add(debt);
        }
        return debts;
    }
    public List<Debt> findAllWithUserIdentifier(String userIdentifier) throws IOException, ParseException {
       return findAll().stream().filter(debt -> debt.userIdentifier.equals(userIdentifier) ).toList();
    }
    private LocalDate stringToLocalDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return LocalDate.parse(date,formatter);
    }
    public Debt findById(long id) throws IOException, ParseException {
        return findAll().stream().filter(debt -> debt.id == id ).findFirst().orElseThrow(() -> new NotFoundException("Borç Bulunamdı"));
    }
    public void delete(Debt debt) throws IOException, ParseException {

        var lastDebs =  findAll().stream().filter(d -> d.id != debt.id).toList();
        save(lastDebs);
    }
    public void save(List<Debt> debts) throws IOException {
        JSONArray array = new JSONArray();
        debts.forEach(debt -> {
            JSONObject object= new JSONObject();
            object.put("user-identifier",debt.userIdentifier);
            object.put("due-date",debt.getLocaleDate());
            object.put("amount",debt.amount);
            object.put("desc",debt.description);
            object.put("id",debt.id);
            array.add(object);
        });
        File file = new File(Main.debtPath);
        DataOutputStream outstream = new DataOutputStream(new FileOutputStream(file, false));
        outstream.write(array.toString().getBytes());
        outstream.close();
    }
}
