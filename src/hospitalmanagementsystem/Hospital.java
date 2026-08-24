
package hospitalmanagementsystem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Hospital {

    private final List<Patient> patients;

    // 20 beds arranged as 4 rows x 5 columns
    private final Inpatient[] beds;

    private static final int TOTAL_BEDS = 20;
    private static final int ROWS = 4;
    private static final int COLUMNS = 5;

    public Hospital() {
        patients = new ArrayList<>();
        beds = new Inpatient[TOTAL_BEDS];
    }

    // =========================================================
    // FEATURE 1 - PATIENT MANAGEMENT
    // =========================================================

    /**
     * Registers a new patient.
     *
     * @return true if registration was successful
     */
    public boolean registerPatient(Patient patient) {

        if (patient == null) {
            return false;
        }

        if (searchPatient(patient.getPatientId()) != null) {
            return false; // Duplicate ID
        }

        // Inpatients must be stored as Inpatient objects
        if (patient.getCategory() == PatientCategory.INPATIENT
                && !(patient instanceof Inpatient)) {
            return false;
        }

        patients.add(patient);
        return true;
    }

    /**
     * Searches for a patient by ID.
     */
    public Patient searchPatient(String patientId) {

        for (Patient patient : patients) {

            if (patient.getPatientId().equalsIgnoreCase(patientId)) {
                return patient;
            }
        }

        return null;
    }

    /**
     * Updates patient information.
     *
     * The patient ID cannot be changed.
     */
    public boolean updatePatient(String patientId,
                                 String firstName,
                                 String lastName,
                                 int age,
                                 String gender,
                                 String medicalCondition) {

        Patient patient = searchPatient(patientId);

        if (patient == null) {
            return false;
        }

        patient.setFirstName(firstName);
        patient.setLastName(lastName);
        patient.setAge(age);
        patient.setGender(gender);
        patient.setMedicalCondition(medicalCondition);

        return true;
    }

    /**
     * Deletes a patient.
     *
     * If the patient is an inpatient with a bed,
     * their bed is automatically released.
     */
    public boolean deletePatient(String patientId) {

        Patient patient = searchPatient(patientId);

        if (patient == null) {
            return false;
        }

        if (patient instanceof Inpatient inpatient) {

            if (inpatient.hasBed()) {
                releaseBed(patientId);
            }
        }

        patients.remove(patient);
        return true;
    }

    /**
     * Displays all patients.
     */
    public void displayAllPatients() {

        if (patients.isEmpty()) {
            System.out.println("No patients are registered.");
            return;
        }

        for (Patient patient : patients) {
            patient.displayDetails();
        }
    }

    // =========================================================
    // FEATURE 2 - BED MANAGEMENT
    // =========================================================

    /**
     * Allocates a bed to an inpatient.
     *
     * Bed numbers range from 1 to 20.
     */
    public boolean allocateBed(String patientId, int bedNumber) {

        Patient patient = searchPatient(patientId);

        if (patient == null) {
            return false;
        }

        if (!(patient instanceof Inpatient inpatient)) {
            return false;
        }

        // Patient already has a bed
        if (inpatient.hasBed()) {
            return false;
        }

        // Invalid bed number
        if (bedNumber < 1 || bedNumber > TOTAL_BEDS) {
            return false;
        }

        // Bed already occupied
        if (beds[bedNumber - 1] != null) {
            return false;
        }

        // Allocate the bed
        beds[bedNumber - 1] = inpatient;
        inpatient.setBedNumber(bedNumber);

        return true;
    }

    /**
     * Automatically allocates the first available bed.
     */
    public boolean allocateFirstAvailableBed(String patientId) {

        Patient patient = searchPatient(patientId);

        if (!(patient instanceof Inpatient)) {
            return false;
        }

        int availableBed = getFirstAvailableBed();

        if (availableBed == -1) {
            return false;
        }

        return allocateBed(patientId, availableBed);
    }

    /**
     * Releases the bed assigned to an inpatient.
     */
    public boolean releaseBed(String patientId) {

        Patient patient = searchPatient(patientId);

        if (!(patient instanceof Inpatient inpatient)) {
            return false;
        }

        if (!inpatient.hasBed()) {
            return false;
        }

        int bedNumber = inpatient.getBedNumber();

        beds[bedNumber - 1] = null;
        inpatient.setBedNumber(0);

        return true;
    }

    /**
     * Gets the first available bed.
     */
    public int getFirstAvailableBed() {

        for (int i = 0; i < TOTAL_BEDS; i++) {

            if (beds[i] == null) {
                return i + 1;
            }
        }

        return -1;
    }

    /**
     * Checks whether a bed is occupied.
     */
    public boolean isBedOccupied(int bedNumber) {

        if (bedNumber < 1 || bedNumber > TOTAL_BEDS) {
            return false;
        }

        return beds[bedNumber - 1] != null;
    }

    /**
     * Displays the complete 4 x 5 ward layout.
     */
    public void displayWardLayout() {

        System.out.println("\n========== WARD LAYOUT ==========");

        for (int row = 0; row < ROWS; row++) {

            for (int column = 0; column < COLUMNS; column++) {

                int bedNumber = row * COLUMNS + column + 1;

                if (beds[bedNumber - 1] == null) {
                    System.out.printf("[Bed %02d: AVAILABLE] ", bedNumber);
                } else {
                    System.out.printf(
                            "[Bed %02d: %s] ",
                            bedNumber,
                            beds[bedNumber - 1].getPatientId()
                    );
                }
            }

            System.out.println();
        }

        System.out.println("=================================\n");
    }

    /**
     * Displays all available beds.
     */
    public void displayAvailableBeds() {

        System.out.println("\n========== AVAILABLE BEDS ==========");

        boolean found = false;

        for (int i = 0; i < TOTAL_BEDS; i++) {

            if (beds[i] == null) {
                System.out.print((i + 1) + " ");
                found = true;
            }
        }

        if (!found) {
            System.out.print("No beds available.");
        }

        System.out.println("\n====================================\n");
    }

    /**
     * Displays all occupied beds.
     */
    public void displayOccupiedBeds() {

        System.out.println("\n========== OCCUPIED BEDS ==========");

        boolean found = false;

        for (int i = 0; i < TOTAL_BEDS; i++) {

            if (beds[i] != null) {

                Inpatient inpatient = beds[i];

                System.out.println(
                        "Bed " + (i + 1)
                        + " -> "
                        + inpatient.getPatientId()
                        + " - "
                        + inpatient.getFirstName()
                        + " "
                        + inpatient.getLastName()
                );

                found = true;
            }
        }

        if (!found) {
            System.out.println("No beds are currently occupied.");
        }

        System.out.println("===================================\n");
    }

    // =========================================================
    // FEATURE 3 - STATISTICS
    // =========================================================

    public int getTotalRegisteredPatients() {
        return patients.size();
    }

    public int getTotalOccupiedBeds() {

        int count = 0;

        for (Inpatient bed : beds) {

            if (bed != null) {
                count++;
            }
        }

        return count;
    }

    public int getTotalAvailableBeds() {
        return TOTAL_BEDS - getTotalOccupiedBeds();
    }

    public double getWardOccupancyPercentage() {

        return (getTotalOccupiedBeds() / (double) TOTAL_BEDS) * 100;
    }

    /**
     * Displays hospital statistics.
     */
    public void displayStatistics() {

        System.out.println("\n========== HOSPITAL STATISTICS ==========");

        System.out.println(
                "Total Registered Patients: "
                        + getTotalRegisteredPatients()
        );

        System.out.println(
                "Total Available Beds: "
                        + getTotalAvailableBeds()
        );

        System.out.println(
                "Total Occupied Beds: "
                        + getTotalOccupiedBeds()
        );

        System.out.printf(
                "Ward Occupancy: %.2f%%%n",
                getWardOccupancyPercentage()
        );

        System.out.println("==========================================\n");
    }

    // =========================================================
    // FEATURE 5 - SORTING
    // =========================================================

    /**
     * Returns patients sorted by surname.
     */
    public List<Patient> getPatientsSortedBySurname() {

        List<Patient> sortedPatients = new ArrayList<>(patients);

        sortedPatients.sort(
                Comparator.comparing(
                        Patient::getLastName,
                        String.CASE_INSENSITIVE_ORDER
                )
        );

        return sortedPatients;
    }

    /**
     * Returns patients sorted by patient ID.
     */
    public List<Patient> getPatientsSortedById() {

        List<Patient> sortedPatients = new ArrayList<>(patients);

        sortedPatients.sort(
                Comparator.comparing(
                        Patient::getPatientId,
                        String.CASE_INSENSITIVE_ORDER
                )
        );

        return sortedPatients;
    }

    /**
     * Displays patients sorted by surname.
     */
    public void displayPatientsSortedBySurname() {

        for (Patient patient : getPatientsSortedBySurname()) {
            patient.displayDetails();
        }
    }

    /**
     * Displays patients sorted by patient ID.
     */
    public void displayPatientsSortedById() {

        for (Patient patient : getPatientsSortedById()) {
            patient.displayDetails();
        }
    }

    /**
     * Returns the total number of beds.
     */
    public int getTotalBeds() {
        return TOTAL_BEDS;
    }
}