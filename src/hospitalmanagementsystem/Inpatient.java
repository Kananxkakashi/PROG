
package hospitalmanagementsystem;

public class Inpatient extends Patient {

    private int wardNumber;
    private int bedNumber;

    public Inpatient(String patientId,
                     String firstName,
                     String lastName,
                     int age,
                     String gender,
                     String medicalCondition,
                     int wardNumber) {

        super(patientId,
              firstName,
              lastName,
              age,
              gender,
              medicalCondition,
              PatientCategory.INPATIENT);

        this.wardNumber = wardNumber;
        this.bedNumber = 0; // 0 means no bed currently allocated
    }

    public int getWardNumber() {
        return wardNumber;
    }

    public int getBedNumber() {
        return bedNumber;
    }

    public void setWardNumber(int wardNumber) {
        this.wardNumber = wardNumber;
    }

    public void setBedNumber(int bedNumber) {
        this.bedNumber = bedNumber;
    }

    public boolean hasBed() {
        return bedNumber != 0;
    }

    @Override
    public void displayDetails() {
        super.displayDetails();

        System.out.println("Ward Number: " + wardNumber);

        if (bedNumber == 0) {
            System.out.println("Bed Number: Not Allocated");
        } else {
            System.out.println("Bed Number: " + bedNumber);
        }

        System.out.println("--------------------------------");
    }
}