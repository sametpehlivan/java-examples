package bank.services;

import bank.Main;
import bank.domain.Debt;
import bank.domain.User;
import bank.services.exceptions.InsufficientBalanceException;
import bank.services.exceptions.NotFoundException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserService extends  Service{
    private static UserService instance;
    private final DebtService debtService;
    private UserService(){
        debtService = DebtService.createInstance();
    }
    public  static UserService createInstance(){
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }
    public List<User> findAll() throws IOException, ParseException {
        var array = findAllJSON(Main.userPath);
        List<User> users = new ArrayList<>();
        for (Object o : array){
            JSONObject jsonObject = (JSONObject) o;
            User user = new User();

            user.identifier = (String) jsonObject.get("identifier");
            user.firstname = (String) jsonObject.get("firstname");
            user.lastname = (String) jsonObject.get("lastname");
            user.password = (String) jsonObject.get("password");
            user.balance = (double) jsonObject.get("balance");
            user.debts = debtService.findAllWithUserIdentifier(user.identifier);


            users.add(user);
        }
        return users;
    }
    public User findByIdentifier(String identifier) throws IOException, ParseException {
        var user = findAll().stream()
                .filter(u -> u.identifier.equals(identifier))
                .findFirst()
                .orElseThrow(()-> new NotFoundException("User Not Found"));
        return user;
    }
    public void userPriceTransfer(User from,User to,double amount) throws IOException, ParseException {
        if (from.equals(to)) return;
        if (from.balance < amount) throw new InsufficientBalanceException("Yetersiz Bakiye.");
        from.balance -= amount;
        to.balance += amount;
        var updatedUsers = findAll().stream().map((user) -> {
            if (user.equals(from)){
                user.balance = from.balance;
            }
            if (user.equals(to)){
                user.balance = to.balance;
            }
            return user;
        }).toList();
        save(updatedUsers);
    }

    private void save(List<User> updatedUsers) throws IOException {
        JSONArray array = new JSONArray();
       updatedUsers.forEach(user -> {
           JSONObject object= new JSONObject();
           object.put("identifier",user.identifier);
           object.put("firstname",user.firstname);
           object.put("lastname",user.lastname);
           object.put("password",user.password);
           object.put("balance",user.balance);
           array.add(object);
       });
        File file = new File(Main.userPath);
        DataOutputStream outstream = new DataOutputStream(new FileOutputStream(file, false));
        outstream.write(array.toString().getBytes());
        outstream.close();
    }

    public void payDebt(User user, int debtId) throws IOException, ParseException {
        Debt debt = debtService.findById(debtId);
        if (!debt.userIdentifier.equals(user.identifier)) throw new NotFoundException("Borç bu kullanıcıya ait değil");
        if (debt.amount > user.balance) throw new InsufficientBalanceException("Yetersiz Bakiye.");
        debtService.delete(debt);
        user.balance -= debt.amount;
        var users  = findAll();
        users.stream().map(u->{
            if (u.equals(user)){
                u.balance = user.balance;
            }
            return user;
        }).toList();
        save(users);
        user.debts = debtService.findAllWithUserIdentifier(user.identifier);
    }
}

