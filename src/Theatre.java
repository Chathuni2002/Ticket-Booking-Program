import java.util.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

public class Theatre {
    int RowNo;
    int SeatNo;
    double price;
    double totalPrice;

    int[] row01 = new int[12];
    int[] row02 = new int[16];
    int[] row03 = new int[20];
    int[][] AllRows = {row01,row02,row03};
    ArrayList<Ticket> ticketList = new ArrayList<>(); // This array list stores the booked tickets.

    Scanner input = new Scanner(System.in);
    public static void main(String[] args) {

        System.out.println("Welcome to the New Theatre");
        Theatre newTheatre = new Theatre();

        while(true) {

            System.out.println("-----------------------------------------------------");
            System.out.println("Please select an option: ");
            System.out.println("1) Buy a ticket");
            System.out.println("2) Print seating area");
            System.out.println("3) Cancel ticket");
            System.out.println("4) List available seats");
            System.out.println("5) Save to file");
            System.out.println("6) Load from file");
            System.out.println("7) Print ticket information and total price");
            System.out.println("8) Sort tickets by price");
            System.out.println("    0) Quit");
            System.out.println("-----------------------------------------------------");

            Scanner input = new Scanner(System.in); // Takes user inputs and calls the right method.
            System.out.println("Enter option: ");
            int option = input.nextInt();
            System.out.println("-----------------------------------------------------");
            switch (option) {
                case 1:
                    newTheatre.buy_ticket();
                    break;
                case 2:
                    newTheatre.print_seating_area();
                    break;
                case 3:
                    newTheatre.cancel_ticket();
                    break;
                case 4:
                    newTheatre.show_available();
                    break;
                case 5:
                    newTheatre.save();
                    break;
                case 6:
                    newTheatre.load();
                    break;
                case 7:
                    newTheatre.show_ticket_info();
                    break;
                case 8:
                    newTheatre.sort_tickets();
                    break;
                case 0:
                    System.out.println("Good Bye...");
                    System.exit(0);
                default:
                    System.out.println("The selected option is incorrect.");
            }
        }
    }
    // Gets row and seat number inputs and checks the data type and range.
    public void getting_inputs_andValidation(){
        while(true){
            try{
                System.out.println("Enter the row number: ");
                RowNo = input.nextInt();
                System.out.println("Enter the seat number: ");
                SeatNo = input.nextInt();
                int value = AllRows[RowNo-1][SeatNo-1];
                break;
            }
            catch(InputMismatchException e){
                System.out.println("Enter an valid integer.");
                input.next();
            }
            catch(ArrayIndexOutOfBoundsException e){
                System.out.println("This seat doesn't exist.");
                input.nextLine();
            }
        }
    }
    public void buy_ticket() { // Books tickets as much as the user wants.
        System.out.println("Ticket Prices: \nFirst row - Rs.400/=\nSecond & Third rows - Rs.250/=");
        System.out.println();
        while(true) {
            getting_inputs_andValidation();
            // Checks whether the seat available.
            if (AllRows[RowNo - 1][SeatNo - 1] == 0) {
                System.out.println("The Seat is available.");
                AllRows[RowNo - 1][SeatNo - 1] = 1;
                // Takes users information.
                System.out.println();
                System.out.println("Personal Information: ");
                System.out.println("Enter your name: ");
                String name = input.next();
                System.out.println("Enter your surname: ");
                String surname = input.next();
                System.out.println("Enter your email: ");
                String email = input.next();
                Person person = new Person(name, surname, email);
                int row = RowNo;
                int seat = SeatNo;
                // Decides the correct ticket price.
                if(RowNo==1){
                    price = 400;
                }
                else{
                    price = 250;
                }
                totalPrice += price;

                Ticket ticket = new Ticket(row, seat, price, person);
                ticketList.add(ticket);
                System.out.println("Ticket is successfully booked.");
                System.out.println();

                System.out.println("Do you want to buy more tickets? Enter \"y\" to continue, Enter \"q\" to stop. ");
                String choice = input.next();
                if (choice.equals("y")) {
                    continue;
                }
                else{
                    break;
                }
            } else {
                System.out.println("The seat is not available.");
                System.out.println("Try Again.");
                System.out.println();
            }
        }

    }
    public void print_seating_area() { // Prints the seats in the correct location.
        System.out.println("    ***********\n    *  STAGE  *\n    ***********\n");
        for(int rowNo=0; rowNo< AllRows.length; rowNo++){ // https://www.programiz.com/java-programming/examples/pyramid-pattern
            for(int i=0; i< 4-(2*rowNo); i++){ // Prints the spaces at the beginning.
                System.out.print(" ");
            }
            for(int seatNo=0; seatNo<AllRows[rowNo].length; seatNo++){
                if (seatNo == AllRows[rowNo].length/2){ // Prints the spaces in the middle.
                    System.out.print("  ");
                }
                if(AllRows[rowNo][seatNo]==0){
                    System.out.print("O");
                }
                else{
                    System.out.print("X");
                }
            }
            System.out.println();
        }
    }
    public void cancel_ticket() { // Cancels tickets.
        System.out.println("To cancel your ticket, ");
        getting_inputs_andValidation();
        Iterator<Ticket> iterator = ticketList.iterator(); // https://www.geeksforgeeks.org/how-to-use-iterator-in-java/
        boolean found = false;
        // This loop iterates through the ticketList and cancels the ticket if it exists.
        while (iterator.hasNext()) {
            Ticket ticket = iterator.next();
            if (ticket.getRow() == RowNo && ticket.getSeat() == SeatNo) {
                AllRows[RowNo - 1][SeatNo - 1] = 0;
                totalPrice -= ticket.getPrice();
                iterator.remove();
                found = true;
                System.out.println("Ticket canceled.");
                System.out.println();
                break;
            }
        }
        if (!found) {
            System.out.println("This seat is not booked.");
            System.out.println();
        }

    }
    public void show_available(){ // Prints the available seat numbers on each row.
        for(int i=0; i< AllRows.length; i++){
            System.out.print("Seats available in row"+(i+1)+": ");
            for(int j=0; j<AllRows[i].length; j++){
                if(AllRows[i][j]==0){
                    System.out.print(j+1+" ");
                }
            }
            System.out.println();
        }
    }
    public void save(){ // Saves array information to a text file.
        try{
            FileWriter dataWriter = new FileWriter("SeatInformation.txt");
            for(int i=0; i< AllRows.length; i++){
                dataWriter.write("Row "+(i+1)+" : ");
                for(int j=0; j< AllRows[i].length; j++) {
                    dataWriter.write(AllRows[i][j]+" ");
                }
                dataWriter.write("\n");
            }
            dataWriter.close();
            System.out.println("Information saved.");
        }
        catch(IOException e){
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    public void load(){ // Reload and prints the array data from the file.
        try{
            File savedFile = new File("SeatInformation.txt");
            Scanner fileReader = new Scanner(savedFile);
            while(fileReader.hasNextLine()){
                String dataText = fileReader.nextLine();
                System.out.println(dataText);
            }
            fileReader.close();
        }
        catch(IOException e){
            System.out.println("Error while reading the file.");
            e.printStackTrace();
        }
    }
    public void show_ticket_info(){ // Prints the tickets with full information.
        for(Ticket tickets : ticketList){
            System.out.println("---------------------");
            tickets.print();
            System.out.println("---------------------");
        }
        System.out.println("Total Price is : Rs."+totalPrice+"/="); // Prints the total price of all the booked tickets.
    }
    public List<Ticket> sort_tickets(){ // Sorts and prints the tickets by the price in ascending order.
        // Stores the sorted tickets.
        List<Ticket> sortedTickets = new ArrayList<>(ticketList); // https://www.javatpoint.com/java-list
        int n = sortedTickets.size();
        for (int i = 0; i < n-1; i++) { // https://www.javatpoint.com/bubble-sort-in-java
            for (int j = 0; j < n-i-1; j++) {
                if (sortedTickets.get(j).getPrice() > sortedTickets.get(j+1).getPrice()) {
                    // This code block swaps the tickets if they are in the wrong order.
                    Ticket temp = sortedTickets.get(j);
                    sortedTickets.set(j, sortedTickets.get(j+1));
                    sortedTickets.set(j+1, temp);
                }
            }
        }
        for(Ticket sortedticket : sortedTickets){ // Prints the sorted tickets.
            System.out.println("---------------------");
            sortedticket.print();
            System.out.println("---------------------");
        }
        return sortedTickets;
    }
}