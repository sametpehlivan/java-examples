package bank;

import bank.services.exceptions.InsufficientBalanceException;
import bank.services.exceptions.NotFoundException;
import bank.domain.User;
import bank.services.UserService;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.Scanner;

public class App {
    private UserService userService;
    public App(){
        userService = UserService.createInstance();
    }
    private User user;
    private void login() throws IOException,ParseException {
            Scanner in = new Scanner(System.in);
            while (!isLogged()){

                if (user == null){

                    System.out.println("Çıkış için q");
                    System.out.print("TC : ");
                    String identifier = in.nextLine();
                    if (identifier.equals("q"))  break;
                    System.out.print("Şifre : ");
                    String password =  in.nextLine();

                    try {
                        User u = userService.findByIdentifier(identifier);
                        if (u.password.equals(password)) user = u;
                    }catch (NotFoundException e){
                        System.err.println("Şifre veya Kullancı adı hatalı");
                    }
                }
                else  {
                    break;
                }
            }

    }
    private void register(){

    }
    private boolean isLogged(){
        return user != null;
    }
    private void selectOperation() throws IOException, ParseException {
        if (isLogged()){
            Scanner in = new Scanner(System.in);

            System.out.println("Para Transeri için: 1");
            System.out.println("Borç Ödeme için: 2");
            String select = in.nextLine();

            switch (select){
                case "1":
                    System.out.println("Para Transferi");
                    transfer();
                    break;
                case "2":
                    System.out.println("Borç Ödeme");
                    payDebt();
                    break;
                default:
                    System.out.println("Geçerli bir işlem giriniz");
                    break;
            }
        }
        else System.err.println("Lütfen Giriş Yapınız");
    }

    private void payDebt() throws IOException,ParseException {
        if (user.debts.isEmpty()){
            System.out.println("Borç Bulunamdı");
            return;
        }
        Scanner in = new Scanner(System.in);
        System.out.println("Güncel Borçlar\n");
        user.debts.forEach(System.err::println);
        System.out.println("Ödemek İstediğiniz Borcun Nosunu giriniz");
        String debtNo = in.nextLine();
        try {
            int debtId = Integer.parseInt(debtNo);
            userService.payDebt(user,debtId);
        }catch (NumberFormatException e){
            System.err.println("Lütfen geçerli bir miktar giriniz");
        }catch (NotFoundException | InsufficientBalanceException e){
            System.err.println(e.getMessage());
        }
    }


    public void transfer()throws IOException,ParseException{
        Scanner in = new Scanner(System.in);
        System.out.println("Transfer Yapılacak kişi TC");
        String identifier = in.nextLine();
        System.out.println("Miktar:");
        String amountS = in.nextLine();
        try {
            double amount = Double.parseDouble(amountS);
            User to =  userService.findByIdentifier(identifier);
            userService.userPriceTransfer(user,to,amount);
        }catch (NumberFormatException e){
            System.err.println("Lütfen geçerli bir miktar giriniz");
        }catch (NotFoundException e){
            System.err.println("Transfer Yapılmak istenen TC No'a ait kişi bulunamadı");
        }catch (InsufficientBalanceException e){
            System.err.println("Yetersiz Bakiye işlem Gerçekleştirilemedi");
        }
    }
    public void execute() throws IOException, ParseException {
        login();
        selectOperation();
    }
}
