import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This file is an integral part the submission for Algorithms and Data Structures
 *
 *@version  27 Dec 2022  Ver.1_final
 */
public class AEDepartment {

    private static Hospital hospital = new Hospital();

    //currency symbol
    static Currency currency = Currency.getInstance("GBP");

    public static void main(String[] args) {

        //starting the program
        runProgram();
    }

    /**
     * requests user to input patient information then adds to patient collection
     */
    static void addNewPatient() {
        String patientID = "";
        String firstName = "";
        String lastName = "";
        String age = "";
        String cost = "";
        String specialty = "";

        boolean quit = false, quit1 = false, quit2 = false, quit3 = false, quit4 = false,
                quit5 = false, quit6 = false;

        try {
            //get user input for patient ID
            do {
                System.out.println("> Enter patient's ID: ");
                patientID = EasyScanner.nextString();

                if (InputValidator.validID(patientID)) {
                    //check patient id does not already exist
                    if (!hospital.getPatientList().idExist(patientID.trim())) {
                        ///no exising same id detected, move on to next step
                        quit = true;
                    } else {
                        //same ID found, asks to choose
                        System.out.println(">> Patient ID already exist. Do you wish to try again?  (Y/N)");
                        String answer = EasyScanner.nextString();
                        if (InputValidator.validYesNo(answer)) {  //valid Y/N
                            //'N' is selected - program terminates to go back to main menu
                            if (answer.trim().startsWith("N") || answer.trim().startsWith("n")) {
                                return;
                            }
                        } else {//invalid Y/N
                            System.out.println("> Invalid choice, Try again later");
                            //program terminates to go back to menu
                            return;
                        }
                    }
                } else {
                    System.out.println(">> Invalid choice entered, numbers only allowed for patient ID,");
                }
            } while (!quit);

            //first name
            do {
                System.out.println("> Enter patient's first name: ");
                firstName = EasyScanner.nextString();

                if (InputValidator.validName(firstName)) {
                    //first name is valid, proceed to next step
                    quit1 = true;

                } else {
                    System.out.println(">> Invalid input entered, letters and - allowed only for name.");
                }
            } while (!quit1);

            //get user input for last name
            do {
                System.out.println("> Enter patient's last name: ");
                lastName = EasyScanner.nextString();

                if (InputValidator.validName(lastName)) {
                    //last name is valid, proceed to next step
                    quit2 = true;

                } else {
                    System.out.println(">> Invalid input entered, letters and - allowed only for name.");
                }
            } while (!quit2);

            //get user input for age
            do {
                System.out.println("> Enter patient's age: ");
                age = EasyScanner.nextString();

                if (InputValidator.validAge(age)) {
                    //age is valid, proceed to next step
                    quit3 = true;

                } else {
                    System.out.println(">> Invalid input entered, number between 0 and 150 allowed for age.");
                }
            } while (!quit3);

            //get user input for treatment cost
            do {
                System.out.println("> Enter patient's treatment cost: ");
                cost = EasyScanner.nextString();

                if (InputValidator.validCost(cost)) {
                    //cost input is valid, proceed to next step
                    quit4 = true;

                } else {
                    System.out.println(">> Invalid input entered, number between 0 and 999999 allowed for cost. ");
                }
            } while (!quit4);

            //get user input for specialty
            do {
                System.out.println("> Enter a number for specialty :" +
                        "\n1 for Cardiovascular " +
                        "\n2 for Dermatology " +
                        "\n3 for Orthopaedics ");
                specialty = EasyScanner.nextString();

                if (InputValidator.validNumber1to3(specialty)) {
                    //specialty is valid proceed to next step
                    quit5 = true;

                } else {
                    System.out.println(">> Invalid choice entered, number between 1 and 3 allowed for specialty.");
                }

            } while (!quit5);

            //names to be capitalised
            firstName = InputValidator.convertName(firstName.trim());
            lastName = InputValidator.convertName(lastName.trim());

            //parse as int
            int ID = Integer.parseInt(patientID.trim());
            //remove any erroneous zeros at the beginning of cost and parse values
            int patientAge = Integer.parseInt(InputValidator.removeZero(age.trim()));
            int treatmentCost = Integer.parseInt(InputValidator.removeZero(cost.trim()));
            int specialtyID = Integer.parseInt(specialty.trim());
            //find doctor id by specialty input
            int doctorID = Integer.parseInt(specialty.trim());

            //get GBP symbol
            String gbp = currency.getSymbol();
            //display inputs and ask to confirm final details
            System.out.println();
            System.out.println("====   Patient details   ====" +
                    "\n\tPatient ID : " + ID +
                    "\n\tFirst Name : " + firstName +
                    "\n\tLast Name : " + lastName +
                    "\n\tAge : " + "" + patientAge +
                    "\n\tTreatment Cost " + gbp  + treatmentCost +
                    "\n\tSpecialty : " + hospital.convertToSpecialty(specialtyID) +
                    "\n\tDoctor: " + hospital.getDoctorList().doctorById(doctorID).getName());

            System.out.println("\t\t>> Confirmation required. " +
                    "Do you wish to save this patient's information?  (Y/N) ");

            //confirmation and adding process
            do {
                String answer = EasyScanner.nextString();
                //validate input: regex will let only yes or no without case restrictions can reach here
                if (InputValidator.validYesNo(answer)) {
                    if (answer.trim().startsWith("N") || answer.trim().startsWith("n")) {
                        //No selected - discard data and terminate the program
                        System.out.println("\t> Patient information not saved.");
                        return;
                    } else {
                        //Yes selected - try to store data
                        if (hospital.addNewPatient(ID, firstName, lastName, patientAge,
                                treatmentCost, specialtyID, doctorID)) {
                            //when new patient data added successfully
                            System.out.println("\t> Patient information saved successfully");
                            quit6 = true;
                        } else {
                            //something went wrong while saving data
                            System.out.println("\t> Patient information not saved. Please try again later");
                        }
                    }
                } else {
                    //invalid answer
                    System.out.println("\t>> Invalid choice, enter Y or N only.");
                }
            } while (!quit6);

        } catch (InputMismatchException | NumberFormatException e) {
            //any invalid inputs not caught so far, program will reach here
            System.out.println("Error in inputs. Data not saved.");
            //for information only
            System.out.println("caught exceptions: " + e.getMessage());
        }
    }


    /**
     * displays patients numbers by doctor
     */
    static void displayPatientsPerDoctor() {
        System.out.println();
        System.out.println("____________________________________");
        System.out.println("=====   Patient Allocation     =====");
        System.out.println("ID" + " | Dr. Name " + "\t| Allocated Patient");
        for (int i = 0; i < hospital.getDoctorList().numberOfDoctors(); i++) {
            int doctorID = i + 1; //doctor id starts from 1
            //get doctor name by id
            String doctorName = hospital.getDoctorList().doctorById(doctorID).getName();
            //get number of patient count for each doctor
            System.out.println(doctorID + "\t" + doctorName + "\t\t"
                    + hospital.listPatientsCountByDoctor().get(i));
        }
    }


    /** displays list of patient by cost in descending order
     * */
    static void displayPatientOrderByCost() {
        System.out.println();
        System.out.println("___________________________________________");
        System.out.println("====  Patient List - by Treatment Cost ====");
        System.out.println("ID" + "|First & Last Name" + "|Age"
                + "|Cost" + "|Specialty");
        hospital.displaySortedPatientList(hospital.getSortedPatientList(1));

    }

    /** displays patients list order by last name
     * */

    static void displayPatientOrderByLastName() {
        System.out.println();
        System.out.println("_______________________________________");
        System.out.println("====  Patient List - by Last Name  ====");
        System.out.println("ID" + "|First & Last Name" + "|Age"
                + "|Cost" + "|Specialty");
        hospital.displaySortedPatientList(hospital.getSortedPatientList(2));

    }

    /** displays patient count by age group
     **/

    static void displayCostOfTreatmentByAgeBand() {
        //get GBP symbol
        String gbp = currency.getSymbol();
        System.out.println();
        System.out.println("___________________________________________________");
        System.out.println("==== Total Number of Patients & Treatment Cost ====");
        System.out.println("Age Band " + "|No. Patients" + "\t|Total Amount");

        for (int i = 0; i < 3; i++) {
            System.out.println(hospital.ageBandList().get(i) + "\t\t"
                    + hospital.listAgeBandCost().get(i)[0] +
                    "\t" + gbp + hospital.listAgeBandCost().get(i)[1]);
        }
    }

    /**
     * displays a patient with the highest bill amount
     */
    static void displayHighCostPatient() {
        System.out.println();

        //using converted parameter retrieve the string with patient information
        String result = hospital.getHighestOrLowestCostPatient(1);
        if (result != null) { //check if no data to retrieve
            System.out.println("_____________________________________________");
            //displays information
            System.out.println("==== Patient with Highest Treatment Cost ====");
            System.out.println("ID" + "|First & Last Name" + "|Age"
                    + "|Cost" + "|Specialty" + "|Doctor");
            System.out.println(result);

        } else {
            System.out.println(">No data available");
        }
    }

    /**
     * displays a patient with the lowest bill amount
     */
    static void displayLowCostPatient() {
        System.out.println();

        //using converted parameter retrieve the string with patient information
        String result = hospital.getHighestOrLowestCostPatient(2);
        if (result != null) { //check if no data to retrieve
            //displays information
            System.out.println("____________________________________________");
            System.out.println("==== Patient with Lowest Treatment Cost ====");
            System.out.println("ID" + "|First & Last Name" + "|Age"
                    + "|Cost" + "|Specialty" + "|Doctor");
            System.out.println(result);

        } else {
            System.out.println(">No data available");
        }
    }


    /**
     * runs program at the starting point, shows user options and executes functions by user selection
     */
    static void runProgram() {

        String choiceInput, choice;
        boolean quit = false;
        do {
            showOptions();
            choiceInput = EasyScanner.nextString();
            choice = choiceInput.trim();  //remove any spaces

            try {
                if (InputValidator.validNumber0to7(choice)) {
                    switch (choice) {
                        case "1":
                            addNewPatient();
                            break;
                        case "2":
                            displayPatientsPerDoctor();
                            break;
                        case "3":
                            displayPatientOrderByCost();
                            break;
                        case "4":
                            displayPatientOrderByLastName();
                            break;
                        case "5":
                            displayCostOfTreatmentByAgeBand();
                            break;
                        case "6":
                            displayHighCostPatient();
                            break;
                        case "7":
                            displayLowCostPatient();
                            break;
                        default:
                            quit = true;
                    }

                } else {
                    System.out.println(">> Invalid choice entered, select a number from 0 to 7.");
                }
            } catch (InputMismatchException e) {
                //if validation fails program will reach here to prevent crash
                System.out.println("Error while processing request, Exiting...");
                return;
            }
        } while (!quit);

    }


    /** displays available options
     * */
        static void showOptions() {
        System.out.println();
        System.out.println("__________________________________________________");
        System.out.println("______________  A & E Department  ________________ ");
        System.out.println("\nAvailable options are... ");
        System.out.println("1 - Add new patient " +
                "\n2 - Patients allocations " +
                "\n3 - Patients list by treatment cost " +
                "\n4 - Patients list by last name " +
                "\n5 - Treatment cost per age band " +
                "\n6 - Patient with the highest treatment cost " +
                "\n7 - Patient with the lowest treatment cost " +
                "\n0 - Quit " +
                "\n> Enter a number: ");
    }

}

//end of Main class

/**
 * class holds data of hospital - patients list, doctors list, age bands

 */
class Hospital {
    /**
     * constructor
     */
    public Hospital() {
    }

    private DoctorList doctorList = new DoctorList();
    private PatientList patientList = new PatientList();
    private AgeBand ageBand = new AgeBand();

    //initialise static age band objects
    public static AgeBand one = new AgeBand(1, 0, 18);  //age from 0 to 18
    public static AgeBand two = new AgeBand(2, 19, 45); //age from 19 to 45
    public static AgeBand three = new AgeBand(3, 46, 150); //age 46 and over

    //holds age band list
    public List<AgeBand> ageBandList() {
        List<AgeBand> list = ageBand.getAllAgeBands();
        list.add(one);
        list.add(two);
        list.add(three);
        return list;
    }

    //holds doctor collection
    public DoctorList getDoctorList() {
        return doctorList;
    }

    //holds patient collection
    public PatientList getPatientList() {
        return patientList;
    }

    //initializing block - add doctors
    {
        //stores doctors name and their specialty when program starts
        addNewDoctor(1, "James", Specialty.Cardiovascular);
        addNewDoctor(2, "Richard", Specialty.Dermatology);
        addNewDoctor(3, "Peter", Specialty.Orthopaedics);
    }


    ////////////////////     INSERT   //////////////////////////////

    /**
     * methods adding additional doctor's data
     *
     * @param ID        patient iD
     * @param specialty doctor's specialty
     * @param name      doctor's name
     * @return returns true if successfully add the information and false otherwise
     */
    public boolean addNewDoctor(int ID, String name, Specialty specialty) {
        //check if id and name and specialty is valid
        if ((ID > 0) && specialty != null && !name.isEmpty()) {
            //id needs to be converted to Specialty then instantiate new doctor
            return doctorList.addDoctor(ID, new Doctor(ID, name, specialty));

        }
        return false;
    }

    /**
     * receives user inputs and stores new patient data to patients collection map
     *
     * @param ID        patient id
     * @param firstName patient's first name
     * @param lastName  patient's last name
     * @param age       patient's age
     * @param cost      treatment cost
     * @param specialty patient's treatment specialty id
     * @param doc       patient's doctor
     * @return true if data added successfully and false otherwise
     */
    public boolean addNewPatient(int ID, String firstName, String lastName, int age, int cost,
                                 int specialty, int doc) {

        if ((ID >= 0) && (specialty > 0 && specialty < 4) && (!firstName.isEmpty() &&
                !lastName.isEmpty() && firstName.length() <= 20 && lastName.length() <= 20) &&
                (cost >= 0 && cost < 1000000) && (doc > 0 && doc < 4)) {
            //instantiate new patient
            Patient newPatient =
                    new Patient(ID, firstName, lastName, age, cost, convertToSpecialty(specialty), doc);

            //call the methods to add
            return patientList.addNewPatient(ID, newPatient);
        }

        return false;

    }


    /**
     * convert int value into Specialty object, helps simplify adding patient
     * by offering user to select an integer rather than typing name of specialty
     *
     * @param input specialty as number input
     * @return Specialty
     */
    public Specialty convertToSpecialty(int input) {
        //if input within the range
        if (input > 0 && input <= 3) {
            switch (input) {
                case 1:
                    return Specialty.Cardiovascular;
                case 2:
                    return Specialty.Dermatology;
                case 3:
                    return Specialty.Orthopaedics;
                default:
                    throw new NullPointerException("Specialty does not exist for the input");
            }
        }
        return null;
    }


    ///////////////////////////// SORTING  ////////////////////////////////////

    /**
     * generates sorted patient list order by treatment cost descending order or
     * order by last name depending on the input parameter
     *
     * @param choice 1 for order by cost 2 for order by last name
     * @return sorted list
     */
    public List<Patient> getSortedPatientList(int choice) {

        //list holds patients
        List<Patient> list = patientList.PatientsAsList();
        if (list.size() > 1) {  //sorting only if the size is greater than 2

            if (choice == 1) {//sorting for cost list
                //quick sort
                quickSortInt(list, 0, list.size() - 1);

                //insertion sort
                //insertionSortInt(sortedList);
            } else if (choice == 2) {//sorting for last name list
                //quick sort
                quickSortString(list, 0, list.size() - 1);

                //insertion sort
                //insertionSortString(sortedList);
            }
        }
        return list;
    }

    /**
     * displays list of patients
     *
     * @param input sorted list
     */
    public void displaySortedPatientList(List<Patient> input) {
        if (input.size() > 0) {
            for (Patient patient : input) {
                System.out.println(patient);
            }
        } else {//if the input is empty
            System.out.println(">No data available, Try again later");
        }
    }

    /**
     * sorts string by alphabetical order using Quick Sort algorithm
     */
    private void quickSortString(List<Patient> list, int low, int high) {
        if (low < high) {

            int index = partitionString(list, low, high);
            //recursively calls itself
            if (index != -1) {
                quickSortString(list, low, index - 1);
                quickSortString(list, index + 1, high);
            }
        }
    }

    /**
     * partitions string array for quick sort
     *
     * @return integer index for partition
     */
    private int partitionString(List<Patient> list, int low, int high) {

        if (list.size() > 0) {
            //set pivot as last element
            Patient pivot = list.get(high);
            int i = low - 1;
            for (int j = low; j < high; j++) {
                if (list.get(j).getLastName().compareTo(pivot.getLastName()) < 0) {
                    i++;
                    Patient temp = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, temp);
                }
            }

            //exchange two array values between lower and higher side
            Patient temp = list.get(i + 1);
            list.set(i + 1, list.get(high));
            list.set(high, temp);
            //partition index
            return i + 1;

        }
        return -1;
    }


    /**
     * sorts integer list by descending order using Quick Sort algorithm
     */
    private void quickSortInt(List<Patient> list, int low, int high) {
        if (low < high) {

            //get index to divide array into two smaller array
            int index = partitionInt(list, low, high);
            //recursively calls itself
            if (index != -1) {
                quickSortInt(list, low, index - 1);
                quickSortInt(list, index + 1, high);
            }
        }
    }

    /**
     * partitions integer array for quick sort
     *
     * @return integer index for partition
     */
    private int partitionInt(List<Patient> list, int low, int high) {
        if (list != null) {
            //set pivot as last element
            Patient pivot = list.get(high);
            int i = low - 1;
            for (int j = low; j < high; j++) {
                if (list.get(j).getTreatmentCost() > pivot.getTreatmentCost()) {
                    i++;
                    Patient temp = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, temp);
                }
            }

            //exchange two array values between lower and higher side
            Patient temp = list.get(i + 1);
            list.set(i + 1, list.get(high));
            list.set(high, temp);

            //partition index
            return i + 1;
        }
        return -1;

    }

    /**
     * sorts integer list using insertion sorting algorithm in alphabetical order
     */
    private void insertionSortInt(List<Patient> list) {
        if (list != null) {
            //set pivot value as last element
            for (int i = 1; i < list.size(); i++) {
                //set key element
                Patient key = list.get(i);
                int j = i - 1;
                while (j >= 0 && list.get(j).getTreatmentCost() <
                        key.getTreatmentCost()) {
                    list.set(j + 1, list.get(j));
                    j = j - 1;
                }
                list.set(j + 1, key);
            }

        }
    }

    /**
     * sorts string list using insertion sorting algorithm in alphabetical order
     */
    private void insertionSortString(List<Patient> list) {
        if (list != null) {
            for (int i = 1; i < list.size(); i++) {
                //set key element
                Patient key = list.get(i);
                int j = i - 1;
                while (j >= 0 && list.get(j).getLastName()
                        .compareTo(key.getLastName()) > 0) {
                    list.set(j + 1, list.get(j));
                    j = j - 1;
                }
                list.set(j + 1, key);
            }
        }
    }


//end of quicksort

    ///////////////////// COUNTING // SUMMATION////////////////////////////

    /**
     * calculates total number of patients and total cost per age band
     *
     * @return list of each age band's patient count total and total cost
     */
    public List<Integer[]> listAgeBandCost() {
        //new empty array holds int arrays
        List<Integer[]> list = new ArrayList<>();
        int bandOneCount = 0; //patient count for band one
        int bandTwoCount = 0;  //patient count for band two
        int bandThreeCount = 0;  //patient count for band three
        int bandOneCost = 0; //total cost for band one
        int bandTwoCost = 0;  //total cost for band two
        int bandThreeCost = 0;  //total cost for band three
        if (patientList != null) {
            for (Patient patient : patientList.getPatientsMap().values()) {
                //count all patient within a range of each age band
                if (one.isWithinRange(patient.getAge())) {
                    //band one count
                    bandOneCount += 1;
                    //band one cost
                    bandOneCost += patient.getTreatmentCost();
                } else if (two.isWithinRange(patient.getAge())) {
                    //band two count
                    bandTwoCount += 1;
                    //band two cost
                    bandTwoCost += patient.getTreatmentCost();
                } else {
                    //band three count
                    bandThreeCount += 1;
                    //band three cost
                    bandThreeCost += patient.getTreatmentCost();

                }
            }
            //add count and cost to the list
            list.add(new Integer[]{bandOneCount, bandOneCost});
            list.add(new Integer[]{bandTwoCount, bandTwoCost});
            list.add(new Integer[]{bandThreeCount, bandThreeCost});

            return list;
        } else {
            throw new NullPointerException("patient list is empty");
        }
    }

    /**
     * Counts number of patients seen by a doctor
     *
     * @return list of numbers holds each doctor's patient count
     */
    public List<Integer> listPatientsCountByDoctor() {
        //new empty array
        ArrayList<Integer> list = new ArrayList<>();
        int count1 = 0;
        int count2 = 0;
        int count3 = 0;

        if (patientList != null) {
            for (Patient patient : patientList.getPatientsMap().values()) {
                if (patient.getDoctorId() == 1) { //doctor 1's patient count
                    count1 += 1;
                } else if (patient.getDoctorId() == 2) {  //doctor 2's patient count
                    count2 += 1;
                } else {
                    count3 += 1; //doctor 3's patient count
                }
            }
            //add to the list
            list.add(count1);
            list.add(count2);
            list.add(count3);

            return list;
        } else {
            throw new NullPointerException("patient list is empty");
        }
    }

    ////////////////////////   MIN and MAX   //////////////////////////

    /**
     * finds the highest or lowest cost patient by setting an arbitrary amount as min/max
     * then comparing sequentially to find min/max amount
     *
     * @param choice 1 for the maximum cost patient 2 for minimum cost patient
     * @return string with patient information
     */
    public String getHighestOrLowestCostPatient(int choice) {

        String result = "";
        if (patientList == null || patientList.numberOfPatient() < 1) { //check if the data is null
            result = "Patient list is empty";

        } else {

            //initial index indicating position of min/max value
            int minIndex = 0;
            int maxIndex = 0;

            //initial minimum and maximum value
            int max = Integer.MIN_VALUE;
            int min = Integer.MAX_VALUE;

            //validate input
            if (choice > 2 || choice < 0) { //invalid number input
                result = "number should be 1 or 2";
            } else if (choice == 1) { //find max value and the patient with the amount
                //iterate each patient for treatment cost
                for (Patient patient : patientList.getPatientsMap().values()) {
                    if (patient.getTreatmentCost() > max) {
                        max = patient.getTreatmentCost();
                        maxIndex = patient.getPatientID();
                    }
                }
                //patient in max index
                Patient recordPatient = patientList.getPatientsMap().get(maxIndex);

                //get doctor from the patient
                Doctor doctor = doctorList.doctorById(recordPatient.getDoctorId());
                //result as string
                result = recordPatient + ", " + doctor.getName();

            } else if (choice == 2) {//find min value and the patient with the amount
                //iterate each patient for treatment cost
                for (Patient patient : patientList.getPatientsMap().values()) {
                    if (patient.getTreatmentCost() < min) {
                        min = patient.getTreatmentCost();
                        minIndex = patient.getPatientID();
                    }
                }

                //patient in min index
                Patient recordPatient = patientList.getPatientsMap().get(minIndex);
                //get doctor from the patient
                Doctor doctor = doctorList.doctorById(recordPatient.getDoctorId());
                //result as string
                result = recordPatient + ", " + doctor.getName();

            }

        }
        return result;
    }


}

////end of Hospital class

/**
 * collection class holds list of patients
 * *
 */
class PatientList {
    private Map<Integer, Patient> patientsMap;

    public PatientList() {
        this.patientsMap = new HashMap<>();
    }

    /**
     * returns copy of patients
     */
    public Map<Integer, Patient> getPatientsMap() {
        return new HashMap<>(patientsMap);
    }

    public boolean addNewPatient(int id, Patient patient) {

        //check if patient list is not zero and patient is not already in the list prior to add
        if (patient != null) {
            //add a patient to map
            patientsMap.put(id, patient);
            return true;
        } else {
            System.out.println("Patient data can't be stored");
        }
        return false;
    }

    /**
     * returns total number of patients stored
     */
    public int numberOfPatient() {

        return patientsMap.size();
    }

    /**
     * print all patients data including doctor name
     */
    public void showAllPatients() {
        if (patientsMap.size() > 0) {
            //get each patient from patient list and display details
            for (Patient patient : patientsMap.values()) {
                DoctorList doctorList = new DoctorList();
                System.out.println(patient + ", " + doctorList.doctorById(patient.getDoctorId()));
            }
        }
    }

    /**
     * generate a collection of each patient
     *
     * @return list
     */
    public List<Patient> PatientsAsList() {
        List<Patient> list = new ArrayList<>();
        if (patientsMap != null) {
            //copying collection time complexity O(1)
            list.addAll(patientsMap.values());
            return list;
        } else {
            throw new NullPointerException("No data available");
        }
    }

    /**
     * finds and returns patient by patient ID from the list,
     * can be used to avoid duplicate id input when adding new patient
     */
    public Patient findPatientById(int patientID) {
        if (patientID > 0) {
            if (patientsMap != null) {
                for (Patient patient : patientsMap.values()) {
                    if (patient.getPatientID() == patientID) {
                        //find a patient with the given ID
                        return patientsMap.get(patientID);
                    }
                }
            }
        }
        return null;
    }


    /**
     * check whether the patient ID input is already in the database
     *
     * @return true if error in ID or the ID exists in the patient map or false if it doesn't
     */
    public boolean idExist(String patientID) {
        int ID = Integer.parseInt(patientID);
        try {
            return patientsMap.containsKey(ID);
        } catch (NullPointerException | ClassCastException e) {
            System.out.println(e.getMessage());
            return true;
        }
    }
}
//end of PatientList Class

/**
 * collection class holds list of doctors

 */
class DoctorList {

    private TreeMap<Integer, Doctor> doctorsMap;

    /***
     *constructor
     */

    public DoctorList() {
        this.doctorsMap = new TreeMap<>();
    }

    public Map<Integer, Doctor> getDoctors() {
        return new TreeMap<>(doctorsMap);
    }

    /**
     * add new doctor record to store
     *
     * @return true if stored successfully and false otherwise
     */

    public boolean addDoctor(int ID, Doctor newDoctor) {
        if (newDoctor != null && (findDoctor(newDoctor) < 0)) {
            doctorsMap.put(ID, newDoctor);
            return true;
        }
        return false;
    }

    /**
     * returns total number of doctors stored
     */
    public int numberOfDoctors() {
        if (doctorsMap.size() > 0) {
            return doctorsMap.size();
        }
        return -1;
    }

    /**
     * print all patients with details
     */
    public void showAllDoctors() {
        if (doctorsMap.size() > 0) {
            //get each doctor from doctor list - toString method in Doctor class will contain details
            for (Doctor doctor : doctorsMap.values()) {
                System.out.println(doctor.getName() + "\t" +doctor.getSpecialty());
            }
        }
    }

    /**
     * returns doctor by doctor ID
     */
    public Doctor doctorById(int doctorID) {
        if (doctorID > 0) {
            //find doctor by id
            return doctorsMap.get(doctorID);
        }
        return null;
    }

    /**
     * check if doctor already exist in the doctor list
     *
     * @param doctor doctor object to search for
     * @return returns index of doctor
     */
    private int findDoctor(Doctor doctor) {
        if (doctor != null) {
            for (int i = 0; i < doctorsMap.size(); i++) {
                if (doctorsMap.get(i) == doctor) {
                    return i;
                }
            }
        }
        return -1;
    }

}
//end of DoctorList class

/**
 * class holds patient information
 */
class Patient {
    private int patientID;
    private String firstName;
    private String lastName;
    private int age;
    private int treatmentCost;

    private Specialty specialty;
    private int doctorID;

    public Patient() {
    }

    /**
     * constructor initialise patient
     */
    public Patient(int patientID, String firstName, String lastName, int age,
                   int treatmentCost, Specialty specialty, int doctorID) {

        this.patientID = patientID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.treatmentCost = treatmentCost;
        this.specialty = specialty;
        this.doctorID = doctorID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAge() {
        return age;
    }

    public int getTreatmentCost() {
        return treatmentCost;
    }

    public Specialty getSpecialty() {
        return specialty;
    }

    public int getPatientID() {
        return patientID;
    }

    public int getDoctorId() {
        return doctorID;
    }

    /**
     * forms full name of patient
     *
     * @return a string combination of first name and last name
     */
    public String fullName() {
        return this.firstName + " " + this.lastName;
    }


    //to check hash value of the object - necessary method in case this class to be key of Map
    @Override
    public int hashCode() {
        //hashing firstname, lastname and age and add an arbitrary integer
        return this.firstName.hashCode() + this.lastName.hashCode() + this.age + 89;
    }


    /**
     * compares two patient object
     *
     * @return true in case same object and false otherwise
     */
    @Override
    public final boolean equals(Object obj) {
        //compare to itself
        if (this == obj) {
            return true;
        }
        //any subclass could be same object as super class
        if (this.getClass() != obj.getClass()) {
            return false;
        }

        //compare full name
        return this.fullName().equals(((Patient) obj).fullName());
    }

    /*this method does not include doctor's name -  use method in Hospital class in case doctor name
    should be displayed */
    @Override
    public String toString() {
        //get currency symbol
        Currency currency = Currency.getInstance("GBP");
        String gbp = currency.getSymbol();

        return this.patientID + ", " + this.firstName + ", " + this.lastName + ", " + this.age +
                ", " + gbp + this.treatmentCost + ", " + this.specialty;
    }
}
//end of Patient Class


/**
 * class holds doctor information
 */
class Doctor {

    private final int doctorID;
    private final String name;
    private final Specialty specialty;

    /**
     * constructor initialise Doctor
     */
    public Doctor(int doctorID, String name, Specialty specialty) {
        this.doctorID = doctorID;
        this.name = name;
        this.specialty = specialty;
    }


    public String getName() {
        return name;
    }

    public Specialty getSpecialty() {
        return specialty;
    }


    @Override
    public int hashCode() {
        return this.doctorID + this.name.hashCode()  + 47;
    }

    @Override
    public boolean equals(Object obj) {
        //compare to itself
        if (this == obj) {
            return true;
        }
        //any subclass could be same object as super class
        if (this.getClass() != obj.getClass()) {
            return false;
        }

        //compare name
        return this.name.equals(((Doctor) obj).name);
    }

    @Override
    public String toString() {
        return this.name;
    }

}

//end of Doctor Class


/**
 * class holds name of specialities
 */
enum Specialty {Cardiovascular, Dermatology, Orthopaedics}

/**
 * class holds age band data and its collection
 */
class AgeBand {
    private int ageBand;
    private int low;
    private int high;
    private List<AgeBand> ageBands;

    /**
     * constructor initialises AgeBand
     */
    public AgeBand(int ageBand, int low, int high) {
        if (ageBand >= 0) {
            this.ageBand = ageBand;
        } else {
            this.ageBand = -1;
        }
        if (low >= 0 && high < 200) {
            this.low = low;
            this.high = high;
        } else {
            this.low = -1;
            this.high = -1;
        }


    }

    public AgeBand() {
        this.ageBands = new ArrayList<>();
    }

    /**
     * returns a copy of age band list
     */
    public List<AgeBand> getAllAgeBands() {
        return new ArrayList<>(ageBands);
    }

    public int findAgeBand(int age) {
        if (age >= 0 && age < 200) {
            if (age >= this.low && age <= this.high) {
                return this.ageBand;
            }
        }
        return -1;
    }

    /**
     * verify it the age belongs to a certain band
     *
     * @param age age
     * @return true if within the range false otherwise
     */
    public boolean isWithinRange(int age) {
        if (age >= 0 && age < 200) {
            //check the age is between low and high age
            return age >= this.low && age <= this.high;
        }
        return false;
    }

    /**
     * display age band range
     */
    @Override
    public String toString() {
        String ageBand = "";
        if (low < 46) {
            ageBand = low + " ~ " + high;
        } else {
            ageBand = low + " ~  ";
        }
        return ageBand;
    }
}
//end of AgeBand
////////////UTILITY CLASSES//////////////////////////////////////////////////////////

/**
 * Class validate user input using regular expressions
 * string and integer and checks null string
 *
 */
class InputValidator {
    /**
     * checks alphabet input
     */
    public static boolean validName(String nameInput) {
        //a to z letters between 1 and 20 in all cases
        String regex = "^[a-zA-Z]{1,20}?([\\-]{1})?([a-zA-Z]{1,20})?$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(nameInput.trim());
        return matcher.matches();
    }

    /**
     * checks numeral input
     */
    public static boolean validCost(String costInput) {
        //non-negative whole number range between 0 and 999999
        String regex = "^\\d{1,6}?$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(costInput.trim());
        return matcher.matches();
    }

    /**
     * checks numeral input
     */
    public static boolean validID(String idInput) {
        //non-negative whole number range between 0 and 999999
        String regex = "^\\d{1,6}?$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(idInput.trim());
        return matcher.matches();

    }

    /**
     * checks numeral input
     */
    public static boolean validNumber1to3(String numberInput) {
        //non-negative whole number between 1 and 3
        String regex = "^[1-3]$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(numberInput.trim());
        return matcher.matches();
    }

    /**
     * checks numeral input
     */
    public static boolean validNumber1to2(String numberInput) {

        //non-negative whole number between 1 and 2
        String regex = "^[1-2]$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(numberInput.trim());
        return matcher.matches();
    }

    /**
     * checks numeral input
     */
    public static boolean validNumber0to7(String numberInput) {
        //non-negative whole number range between 0 and 7
        String regex = "^[0-7]$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(numberInput.trim());
        return matcher.matches();
    }

    /**
     * checks numeral input
     */
    public static boolean validAge(String ageInput) {
        //non-negative whole number range between 0 and 199
        String regex = "^[0-1]?\\d?\\d$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(ageInput.trim());
        return matcher.matches();
    }

    /**
     * checks yes or no input
     */
    public static boolean validYesNo(String answer) {
        //begins with one capital Y or N followed by es or o in all cases
        String regex = "^(Y{1}[eE]?[sS]?|y{1}[eE]?[sS]?$)|(N{1}[oO]?|n{1}[oO]?$)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(answer.trim());
        return matcher.matches();

    }

    /**
     * checks quit input
     */
    public static boolean validQuit(String answer) {
        //either q , Q or quit in all cases
        String regex = "^(Q{1})|(Q{1}[uU][iI][tT]$)|(q{1})|(q{1}[uU][iI][tT]$)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(answer.trim());
        return matcher.matches();

    }

    /**
     * removes invalid zero input in numbers
     *
     * @return string with 0s removed ahead of other digits
     */
    public static String removeZero(String numberIn) {
        String trimmedString = numberIn.trim(); //trim input string first if not done already
        int index = trimmedString.indexOf('0');

        //check string is least 2 digits and 0 is the first letter
        if ((index == 0) && (trimmedString.length() > 1)) {
            //recursively removes erroneous 0 at the beginning of string
            return removeZero(trimmedString.substring(index + 1));
        }
        return trimmedString;
    }

    /**
     * converts a string to name format -works with name with hyphen
     *
     * @return string with first letter capitalised and lowercase the rest
     */
    public static String convertName(String nameIn) {
        //name must be trimmed prior to substring
        String first, rest1, second, rest2, resultString;
        String trimmedString = nameIn.trim();

        //not hyphenated names
        first = trimmedString.substring(0, 1); //first letter
        rest1 = trimmedString.substring(1);
        resultString = (first.toUpperCase()).concat(rest1.toLowerCase());

        //name validates true for hyphenated names
        if (nameIn.contains("-")) { //capitalise first letter for hyphenated names
            int index = trimmedString.indexOf("-");
            rest1 = trimmedString.substring(1, index);
            second = trimmedString.substring(index + 1, index + 2);
            rest2 = trimmedString.substring(index + 2);
            return first.toUpperCase() + rest1.toLowerCase()
                    + "-" + (second.toUpperCase()).concat(rest2.toLowerCase());
        } else {
            return resultString;
        }
    }
}


/**
 * Utility class to use Scanner class with stability
 * source: Java in Two Semesters, Charatan and Kans, Springer, 2019
 */
class EasyScanner {

    public static int nextInt() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }

    public static double nextDouble() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextDouble();
    }

    public static String nextString() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    public static char nextChar() {
        Scanner scanner = new Scanner(System.in);
        return scanner.next().charAt(0);
    }

}

