
package hospitalmanagementsystem;

import java.util.Scanner;

public class HospitalApp {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Hospital hospital = new Hospital();

    public static void main(String[] args) {

        boolean running = true;

        while (running) {

            displayMenu();

            int choice = readInt("Enter your choice: ");

            switch (choice) {

                case 1 -> registerPatient();
                case 2 -> searchPatient();
                case 3 -> updatePatient();
                case 4 -> deletePatient();

                case 5 -> hospital.displayAllPatients();

                case 6 -> allocateBed();
                case 7 -> releaseBed();

                case 8 -> hospital.displayWardLayout();
                case 9 -> hospital.displayAvailableBeds();
                case 10 -> hospital.displayOccupiedBeds();

                case 11 -> hospital.displayStatistics();

                case 12 -> hospital.displayPatientsSortedBySurname();
                case 13 -> hospital.displayPatientsSortedById();

                case 0 -> {
                    System.out.println("Exiting Hospital Management System...");
                    running = false;
                }

                default -> System.out.println("Invalid choice.");
            }
        }

        scanner.close();
    }

    // =========================================================
    // MENU
    // =========================================================

    private static void displayMenu() {

        System.out.println("\n==============================================");
        System.out.println("       HOSPITAL MANAGEMENT SYSTEM");
        System.out.println("==============================================");

        System.out.println("1.  Register New Patient");
        System.out.println("2.  Search Patient");
        System.out.println("3.  Update Patient");
        System.out.println("4.  Delete Patient");
        System.out.println("5.  Display All Patients");

        System.out.println("\n--- BED MANAGEMENT ---");
        System.out.println("6.  Allocate Bed");
        System.out.println("7.  Release Bed");
        System.out.println("8.  Display Ward Layout");
        System.out.println("9.  Display Available Beds");
        System.out.println("10. Display Occupied Beds");

        System.out.println("\n--- STATISTICS ---");
        System.out.println("11. Display Hospital Statistics");

        System.out.println("\n--- SORTING ---");
        System.out.println("12. Sort Patients by Surname");
        System.out.println("13. Sort Patients by Patient ID");

        System.out.println("\n0.  Exit");

        System.out.println("==============================================");
    }

    // =========================================================
    // REGISTER
    // =========================================================

    private static void registerPatient() {

        System.out.println("\n========== REGISTER PATIENT ==========");

        String id = readString("Patient ID: ");

        if (hospital.searchPatient(id) != null) {
            System.out.println("ERROR: Patient ID already exists.");
            return;
        }

        String firstName = readString("First Name: ");
        String lastName = readString("Last Name: ");
        int age = readInt("Age: ");
        String gender = readString("Gender: ");
        String condition = readString("Medical Condition: ");

        System.out.println("\nPatient Category:");
        System.out.println("1. Inpatient");
        System.out.println("2. Outpatient");
        System.out.println("3. Emergency");

        int categoryChoice = readInt("Select category: ");

        Patient patient;

        switch (categoryChoice) {

            case 1 -> {

                int wardNumber = readInt("Ward Number: ");

                patient = new Inpatient(
                        id,
                        firstName,
                        lastName,
                        age,
                        gender,
                        condition,
                        wardNumber
                );
            }

            case 2 -> patient = new Patient(
                    id,
                    firstName,
                    lastName,
                    age,
                    gender,
                    condition,
                    PatientCategory.OUTPATIENT
            );

            case 3 -> patient = new Patient(
                    id,
                    firstName,
                    lastName,
                    age,
                    gender,
                    condition,
                    PatientCategory.EMERGENCY
            );

            default -> {
                System.out.println("Invalid category.");
                return;
            }
        }

        if (hospital.registerPatient(patient)) {
            System.out.println("Patient registered successfully.");
        } else {
            System.out.println("Patient registration failed.");
        }
    }

    // =========================================================
    // SEARCH
    // =========================================================

    private static void searchPatient() {

        System.out.println("\n========== SEARCH PATIENT ==========");

        String id = readString("Enter Patient ID: ");

        Patient patient = hospital.searchPatient(id);

        if (patient == null) {
            System.out.println("Patient not found.");
        } else {
            patient.displayDetails();
        }
    }

    // =========================================================
    // UPDATE
    // =========================================================

    private static void updatePatient() {

        System.out.println("\n========== UPDATE PATIENT ==========");

        String id = readString("Enter Patient ID: ");

        Patient patient = hospital.searchPatient(id);

        if (patient == null) {
            System.out.println("Patient not found.");
            return;
        }

        String firstName = readString("New First Name: ");
        String lastName = readString("New Last Name: ");
        int age = readInt("New Age: ");
        String gender = readString("New Gender: ");
        String condition = readString("New Medical Condition: ");

        if (hospital.updatePatient(
                id,
                firstName,
                lastName,
                age,
                gender,
                condition)) {

            System.out.println("Patient updated successfully.");

        } else {
            System.out.println("Patient update failed.");
        }
    }

    // =========================================================
    // DELETE
    // =========================================================

    private static void deletePatient() {

        System.out.println("\n========== DELETE PATIENT ==========");

        String id = readString("Enter Patient ID: ");

        if (hospital.deletePatient(id)) {
            System.out.println("Patient deleted successfully.");
        } else {
            System.out.println("Patient not found.");
        }
    }

    // =========================================================
    // BED ALLOCATION
    // =========================================================

    private static void allocateBed() {

        System.out.println("\n========== ALLOCATE BED ==========");

        String id = readString("Enter Inpatient ID: ");

        Patient patient = hospital.searchPatient(id);

        if (patient == null) {
            System.out.println("Patient not found.");
            return;
        }

        if (!(patient instanceof Inpatient)) {
            System.out.println(
                    "ERROR: Only inpatients can be allocated a bed."
            );
            return;
        }

        if (hospital.getTotalAvailableBeds() == 0) {
            System.out.println("ERROR: No beds are currently available.");
            return;
        }

        System.out.println(
                "Available beds:"
        );

        hospital.displayAvailableBeds();

        int bedNumber = readInt(
                "Enter bed number to allocate: "
        );

        if (hospital.allocateBed(id, bedNumber)) {
            System.out.println(
                    "Bed " + bedNumber
                            + " allocated successfully."
            );
        } else {
            System.out.println(
                    "ERROR: Bed allocation failed. "
                            + "The bed may already be occupied or "
                            + "the patient may already have a bed."
            );
        }
    }

    // =========================================================
    // RELEASE BED
    // =========================================================

    private static void releaseBed() {

        System.out.println("\n========== RELEASE BED ==========");

        String id = readString("Enter Inpatient ID: ");

        if (hospital.releaseBed(id)) {
            System.out.println("Bed released successfully.");
        } else {
            System.out.println(
                    "Bed release failed. "
                            + "Check that the patient is an inpatient "
                            + "with an allocated bed."
            );
        }
    }

    // =========================================================
    // INPUT METHODS
    // =========================================================

    private static String readString(String message) {

        System.out.print(message);

        return scanner.nextLine().trim();
    }

    private static int readInt(String message) {

        while (true) {

            try {

                System.out.print(message);

                return Integer.parseInt(
                        scanner.nextLine().trim()
                );

            } catch (NumberFormatException e) {

                System.out.println(
                        "Please enter a valid number."
                );
            }
        }
    }
}