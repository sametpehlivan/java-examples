package bank.domain;

import java.util.List;
import java.util.Objects;

public class User {
    public String identifier;
    public String firstname;
    public String lastname;
    public String password;
    public double balance;
    public List<Debt> debts;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return identifier.equals(user.identifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier);
    }
}
