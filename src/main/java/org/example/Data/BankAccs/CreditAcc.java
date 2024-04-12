package org.example.Data.BankAccs;


import org.example.Data.Client;
import org.example.Exceptions.ErrorMoneyException;
import javax.persistence.*;
import java.util.List;

@Entity
@DiscriminatorValue("creditAcc")
public class CreditAcc extends BankAccount {

    public CreditAcc() {};

    public CreditAcc(Client client, long id) {
        super(client, id);
        List<BankAccount> accounts = client.getAccs();
        int g = 0;
        int c = 0;
        for (BankAccount acc : accounts) {
            if (acc instanceof CreditAcc) {
                ++c;
                String[] strs = acc.getTitle().split(" ");
                if (!strs[strs.length - 1].equals("����")) {
                    int tmp = Integer.parseInt(strs[strs.length - 1]);
                    g = Math.max(g, tmp);
                }
            }
        }
        String title = c == 0 ? "��������� ����" : "��������� ����" + " " + ++g;
        this.setTitle(title);
    }

    @Override
    public void transactMoney(BankAccount account, double money) throws ErrorMoneyException {
        if (money > 0) {
            if (this.getMoney() - money >= -10000) {
                account.topUpAcc(money);
                this.topDownAcc(money);
            }
            else {
                throw new ErrorMoneyException("� ��� ������������ �������.");
            }
        } else {
            throw new ErrorMoneyException("����� ������ ���� ������ 0.");
        }
    }
}
