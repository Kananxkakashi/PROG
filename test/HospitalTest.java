import hospitalmanagementsystem.Hospital;
import hospitalmanagementsystem.Inpatient;
import hospitalmanagementsystem.Patient;
import hospitalmanagementsystem.PatientCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HospitalTest {

    private Hospital hospital;

    @BeforeEach
    void setUp() {
        hospital = new Hospital();
    }

    // =========================================================
    // TEST 1 - REGISTER PATIENT
    // =========================================================

    @Test
    void testRegisterPatient() {

        Patient patient = new Patient(
                "P001",
                "John",
                "Smith",
                30,
                "Male",
                "Flu",
                PatientCategory.OUTPATIENT
        );

        assertTrue(hospital.registerPatient(patient));

        assertNotNull(
                hospital.searchPatient("P001")
        );

        assertEquals(
                1,
                hospital.getTotalRegisteredPatients()
        );
    }

    // =========================================================
    // TEST 2 - SEARCH PATIENT
    // =========================================================

    @Test
    void testSearchPatient() {

        Patient patient = new Patient(
                "P002",
                "Sarah",
                "Jones",
                25,
                "Female",
                "Asthma",
                PatientCategory.OUTPATIENT
        );

        hospital.registerPatient(patient);

        Patient found = hospital.searchPatient("P002");

        assertNotNull(found);
        assertEquals("Sarah", found.getFirstName());
        assertEquals("Jones", found.getLastName());
    }

    // =========================================================
    // TEST 3 - UPDATE PATIENT
    // =========================================================

    @Test
    void testUpdatePatient() {

        Patient patient = new Patient(
                "P003",
                "John",
                "Brown",
                40,
                "Male",
                "Cold",
                PatientCategory.OUTPATIENT
        );

        hospital.registerPatient(patient);

        boolean result = hospital.updatePatient(
                "P003",
                "James",
                "Brown",
                41,
                "Male",
                "Pneumonia"
        );

        assertTrue(result);

        Patient updated = hospital.searchPatient("P003");

        assertEquals("James", updated.getFirstName());
        assertEquals(41, updated.getAge());
        assertEquals("Pneumonia",
                updated.getMedicalCondition());
    }

    // =========================================================
    // TEST 4 - DELETE PATIENT
    // =========================================================

    @Test
    void testDeletePatient() {

        Patient patient = new Patient(
                "P004",
                "David",
                "Williams",
                50,
                "Male",
                "Diabetes",
                PatientCategory.OUTPATIENT
        );

        hospital.registerPatient(patient);

        assertTrue(
                hospital.deletePatient("P004")
        );

        assertNull(
                hospital.searchPatient("P004")
        );

        assertEquals(
                0,
                hospital.getTotalRegisteredPatients()
        );
    }

    // =========================================================
    // TEST 5 - ALLOCATE BED
    // =========================================================

    @Test
    void testAllocateBed() {

        Inpatient patient = new Inpatient(
                "I001",
                "Michael",
                "Smith",
                45,
                "Male",
                "Fracture",
                1
        );

        hospital.registerPatient(patient);

        assertTrue(
                hospital.allocateBed("I001", 1)
        );

        assertEquals(
                1,
                patient.getBedNumber()
        );

        assertEquals(
                1,
                hospital.getTotalOccupiedBeds()
        );

        assertTrue(
                hospital.isBedOccupied(1)
        );
    }

    // =========================================================
    // TEST 6 - RELEASE BED
    // =========================================================

    @Test
    void testReleaseBed() {

        Inpatient patient = new Inpatient(
                "I002",
                "Peter",
                "Johnson",
                55,
                "Male",
                "Heart Condition",
                1
        );

        hospital.registerPatient(patient);

        hospital.allocateBed("I002", 5);

        assertEquals(
                5,
                patient.getBedNumber()
        );

        assertTrue(
                hospital.releaseBed("I002")
        );

        assertEquals(
                0,
                patient.getBedNumber()
        );

        assertFalse(
                hospital.isBedOccupied(5)
        );
    }

    // =========================================================
    // TEST 7 - PREVENT DUPLICATE PATIENT IDs
    // =========================================================

    @Test
    void testPreventDuplicatePatientIds() {

        Patient patient1 = new Patient(
                "P100",
                "John",
                "Smith",
                30,
                "Male",
                "Flu",
                PatientCategory.OUTPATIENT
        );

        Patient patient2 = new Patient(
                "P100",
                "Sarah",
                "Jones",
                25,
                "Female",
                "Asthma",
                PatientCategory.EMERGENCY
        );

        assertTrue(
                hospital.registerPatient(patient1)
        );

        assertFalse(
                hospital.registerPatient(patient2)
        );

        assertEquals(
                1,
                hospital.getTotalRegisteredPatients()
        );
    }

    // =========================================================
    // TEST 8 - PREVENT ALLOCATING OCCUPIED BED
    // =========================================================

    @Test
    void testPreventAllocatingOccupiedBed() {

        Inpatient patient1 = new Inpatient(
                "I100",
                "John",
                "Smith",
                30,
                "Male",
                "Flu",
                1
        );

        Inpatient patient2 = new Inpatient(
                "I101",
                "Sarah",
                "Jones",
                25,
                "Female",
                "Asthma",
                1
        );

        hospital.registerPatient(patient1);
        hospital.registerPatient(patient2);

        assertTrue(
                hospital.allocateBed("I100", 1)
        );

        assertFalse(
                hospital.allocateBed("I101", 1)
        );

        assertEquals(
                1,
                hospital.getTotalOccupiedBeds()
        );
    }

    // =========================================================
    // TEST 9 - PREVENT BED ALLOCATION WHEN ALL BEDS OCCUPIED
    // =========================================================

    @Test
    void testPreventAllocationWhenAllBedsOccupied() {

        // Create 20 inpatients
        for (int i = 1; i <= 20; i++) {

            Inpatient patient = new Inpatient(
                    "I" + i,
                    "First" + i,
                    "Last" + i,
                    20 + i,
                    "Male",
                    "Condition",
                    1
            );

            hospital.registerPatient(patient);

            assertTrue(
                    hospital.allocateBed(
                            "I" + i,
                            i
                    )
            );
        }

        assertEquals(
                20,
                hospital.getTotalOccupiedBeds()
        );

        assertEquals(
                0,
                hospital.getTotalAvailableBeds()
        );

        Inpatient extraPatient = new Inpatient(
                "I21",
                "Extra",
                "Patient",
                30,
                "Female",
                "Condition",
                1
        );

        hospital.registerPatient(extraPatient);

        assertFalse(
                hospital.allocateFirstAvailableBed("I21")
        );

        assertEquals(
                20,
                hospital.getTotalOccupiedBeds()
        );
    }

    // =========================================================
    // TEST 10 - SORT BY SURNAME
    // =========================================================

    @Test
    void testSortPatientsBySurname() {

        hospital.registerPatient(
                new Patient(
                        "P003",
                        "John",
                        "Zulu",
                        30,
                        "Male",
                        "Flu",
                        PatientCategory.OUTPATIENT
                )
        );

        hospital.registerPatient(
                new Patient(
                        "P001",
                        "Sarah",
                        "Adams",
                        25,
                        "Female",
                        "Asthma",
                        PatientCategory.OUTPATIENT
                )
        );

        hospital.registerPatient(
                new Patient(
                        "P002",
                        "Peter",
                        "Brown",
                        40,
                        "Male",
                        "Cold",
                        PatientCategory.OUTPATIENT
                )
        );

        List<Patient> sorted =
                hospital.getPatientsSortedBySurname();

        assertEquals(
                "Adams",
                sorted.get(0).getLastName()
        );

        assertEquals(
                "Brown",
                sorted.get(1).getLastName()
        );

        assertEquals(
                "Zulu",
                sorted.get(2).getLastName()
        );
    }

    // =========================================================
    // TEST 11 - SORT BY PATIENT ID
    // =========================================================

    @Test
    void testSortPatientsById() {

        hospital.registerPatient(
                new Patient(
                        "P003",
                        "John",
                        "Zulu",
                        30,
                        "Male",
                        "Flu",
                        PatientCategory.OUTPATIENT
                )
        );

        hospital.registerPatient(
                new Patient(
                        "P001",
                        "Sarah",
                        "Adams",
                        25,
                        "Female",
                        "Asthma",
                        PatientCategory.OUTPATIENT
                )
        );

        hospital.registerPatient(
                new Patient(
                        "P002",
                        "Peter",
                        "Brown",
                        40,
                        "Male",
                        "Cold",
                        PatientCategory.OUTPATIENT
                )
        );

        List<Patient> sorted =
                hospital.getPatientsSortedById();

        assertEquals(
                "P001",
                sorted.get(0).getPatientId()
        );

        assertEquals(
                "P002",
                sorted.get(1).getPatientId()
        );

        assertEquals(
                "P003",
                sorted.get(2).getPatientId()
        );
    }

    // =========================================================
    // ADDITIONAL TEST - ONLY INPATIENTS GET BEDS
    // =========================================================

    @Test
    void testOnlyInpatientsCanGetBeds() {

        Patient outpatient = new Patient(
                "O001",
                "John",
                "Smith",
                30,
                "Male",
                "Flu",
                PatientCategory.OUTPATIENT
        );

        Patient emergency = new Patient(
                "E001",
                "Sarah",
                "Jones",
                25,
                "Female",
                "Injury",
                PatientCategory.EMERGENCY
        );

        hospital.registerPatient(outpatient);
        hospital.registerPatient(emergency);

        assertFalse(
                hospital.allocateBed("O001", 1)
        );

        assertFalse(
                hospital.allocateBed("E001", 2)
        );

        assertEquals(
                0,
                hospital.getTotalOccupiedBeds()
        );
    }

    // =========================================================
    // ADDITIONAL TEST - OCCUPANCY PERCENTAGE
    // =========================================================

    @Test
    void testWardOccupancyPercentage() {

        Inpatient patient = new Inpatient(
                "I500",
                "Test",
                "Patient",
                40,
                "Male",
                "Test Condition",
                1
        );

        hospital.registerPatient(patient);

        hospital.allocateBed("I500", 1);

        assertEquals(
                5.0,
                hospital.getWardOccupancyPercentage(),
                0.001
        );
    }
}