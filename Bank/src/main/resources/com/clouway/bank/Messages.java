package com.clouway.bank;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
public class Messages {
    public String successfullyAddedAmount(){
        return "Amount successfully added!";
    }

    public String addAmountWithTooManyDecimalPlacesError(){
        return "Amount not added! Enter amount with two digits after decimal point.";
    }

    public String addNegativeAmountError(){
        return "Amount not added! Enter only positive amount.";
    }

    public String addStringAmountError(){
        return "Amount not added! Enter only numbers.";
    }

    public String successfullyWithdrawnAmount(){
        return "Amount successfully withdrawn!";
    }

    public String insufficientCurrentBalance(){
        return "Can not perform operation! Not enough money in your account!";
    }

    public String withdrawNegativeAmountError(){
        return "Amount not withdrawn! Enter only positive amount.";
    }

    public String withdrawStringAmountError(){
        return "Amount not withdrawn! Enter only numbers";
    }

    public String withdrawAmountWithTooManyDecimalPlacesError(){
        return "Amount not withdrawn! Enter amount with two digits after decimal point.";
    }
}
