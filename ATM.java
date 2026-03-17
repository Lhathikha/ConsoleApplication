import java.util.Scanner;
public class ATM {
    static double bal=0.0;
    public static double check(){
        return bal;
    }
     public static double deposit(double amount){
        bal+=amount;
        return bal;
    }
    public static double withdraw(double amount){
        if(bal-amount>=0){
            bal-=amount;
            return bal;
        }
        return -1;
    }
    public static void main(String[] args) {
        Scanner scan=new Scanner(System.in);

        do{
            System.out.println("1.Check balance");
            System.out.println("2.Deposit money");
            System.out.println("3.Withdraw amount");
            System.out.println("4.Exit");
            System.out.print("enter your choice:");
            int c=scan.nextInt();
            switch (c) {
                case 1:
                    double b=check();
                    System.out.println("current balance:"+b);
                    break;
                case 2:
                    System.out.print("enter money to deposit");
                    double amount=scan.nextDouble();
                    if(amount<=0){
                        System.out.println("invalid amount");
                        break;
                    }
                    double a=deposit(amount);
                    System.out.println("successfully deposited");
                    System.out.println("current balance:"+a);
                    break;
                case 3:
                    System.out.print("enter money to withdraw");
                    double amount1=scan.nextDouble();
                    if(amount1<=0){
                        System.out.println("invalid amount");
                        break;
                    }
                    double d=withdraw(amount1);
                    if(d==-1){
                        System.out.println("insufficient balance");
                    }
                    else{
                        System.out.println("successfully withdrawed amount:"+amount1);
                        System.out.println("current balance:"+d);
                    }
                    break;
                case 4:
                    
                    System.out.println("Thank you");
                    System.exit(0);
                    break;
            
                default:
                    System.out.println("enter a valid choice");
                    break;
            }
        }while(true);
    }
}
