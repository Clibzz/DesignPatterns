package nhlstenden.bookandsales.Model;

import java.util.Stack;

public class PaymentCartHistory
{
    private Stack<PaymentCartMemento> history;

    public PaymentCartHistory()
    {
        this.history = new Stack<>();
    }

    public void saveState(PaymentCartMemento memento)
    {
        this.history.push(memento);
    }

    public PaymentCartMemento restoreState()
    {
        if (!this.history.isEmpty())
        {
            return history.pop();
        }
        return null;
    }
}
