package bank;

import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static final String  userPath =  getBaseDirectory()+"/users.json";
    public static final String  debtPath =  getBaseDirectory()+"/debts.json";

    public static void main(String[] args) throws IOException, ParseException {
        createJsonFileWithName(userPath);
        createJsonFileWithName(debtPath);
        App app = new App();
        app.execute();
    }
    public static String getBaseDirectory(){
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString()+"/src/bank";
        return  s;
    }
    public static void createJsonFileWithName(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()){
            file.getParentFile().mkdirs(); // Will create parent directories if not exists
            file.createNewFile();

            FileOutputStream s = new FileOutputStream(file,false);
            PrintStream p = new PrintStream(s);
            p.print("[]");
            s.close();
            p.close();
        }
    }
}
