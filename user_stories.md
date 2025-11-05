DOCTOR USER STORIES

User Story 1 – Doctor Login

Title: Doctor – Login to Portal

User Story:
As a doctor, I want to log into the portal, so that I can manage my appointments and update my schedule securely.

Acceptance Criteria:

[ ] Doctor enters valid username and password to log in.

[ ] System validates credentials with the database.

[ ] Successful login redirects to the doctor dashboard.

[ ] Invalid credentials display an error message.

[ ] Session management ensures secure access during login.

User Story 2 – Doctor Logout

Title: Doctor – Logout from Portal

User Story:
As a doctor, I want to log out of the portal, so that I can protect my account and ensure data security.

Acceptance Criteria:

[ ] Doctor can log out from any page using a visible logout button.

[ ] Session/token should be invalidated immediately after logout.

[ ] System redirects to the login or homepage after logout.

[ ] Logged-out users cannot access any restricted pages without logging in again.

User Story 3 – View Appointment Calendar

Title: Doctor – View Appointment Calendar

User Story:
As a doctor, I want to view my appointment calendar, so that I can stay organized and manage my daily consultations effectively.

Acceptance Criteria:

[ ] Doctor can view a list or calendar view of all upcoming appointments.

[ ] Each appointment displays patient name, date, time, and consultation mode (online/offline).

[ ] Calendar should auto-update when new appointments are booked or canceled.

[ ] Option to filter appointments by date or patient.

User Story 4 – Mark Unavailability

Title: Doctor – Mark Unavailability

User Story:
As a doctor, I want to mark my unavailability, so that patients can only book appointments during my available slots.

Acceptance Criteria:

[ ] Doctor can select specific dates or time slots as unavailable.

[ ] Unavailable slots are automatically blocked on the patient booking portal.

[ ] Confirmation message appears after updating unavailability.

[ ] Patients cannot book appointments during unavailable times.

User Story 5 – Update Profile

Title: Doctor – Update Profile Information

User Story:
As a doctor, I want to update my profile details such as specialization, contact information, and availability hours, so that patients always have up-to-date information.

Acceptance Criteria:

[ ] Doctor can edit profile details like name, specialization, experience, contact number, and working hours.

[ ] System validates inputs before saving changes.

[ ] Updated information is stored in the database.

[ ] Patients see the updated profile information in the doctor list immediately.

User Story 6 – View Patient Details

Title: Doctor – View Patient Details for Upcoming Appointments

User Story:
As a doctor, I want to view patient details for my upcoming appointments, so that I can prepare for the consultation in advance.

Acceptance Criteria:

[ ] Doctor can view each appointment’s patient details (name, age, contact, health notes).

[ ] Patient data should be visible only to the doctor assigned to that appointment.

[ ] The interface should list appointments chronologically.

[ ] Doctor can mark appointment status as “Completed” or “Cancelled” after the session.


Patient user stories

User Story 1 – View List of Doctors

Title: Patient – View List of Doctors

User Story:
As a patient, I want to view a list of available doctors without logging in, so that I can explore options before registering on the platform.

Acceptance Criteria:

[ ] Patient can access the list of doctors from the homepage.

[ ] Doctor information (name, specialization, availability) is visible.

[ ] No login or registration is required to view this list.

[ ] The list dynamically updates as doctors are added or removed by the admin.

User Story 2 – Patient Sign Up

Title: Patient – Sign Up

User Story:
As a patient, I want to sign up using my email and password, so that I can create an account to book appointments.

Acceptance Criteria:

[ ] The signup form should include name, email, password, and contact details.

[ ] Password should be encrypted before storing in the database.

[ ] The system must validate that the email is unique.

[ ] Upon successful registration, the patient should receive a confirmation message or email.

User Story 3 – Patient Login

Title: Patient – Login

User Story:
As a patient, I want to log into the portal using my credentials, so that I can securely manage my bookings and appointments.

Acceptance Criteria:

[ ] Patient enters valid email and password.

[ ] Successful login redirects to the patient dashboard.

[ ] Invalid credentials show an error message.

[ ] Session management ensures secure access and auto logout after inactivity.

User Story 4 – Patient Logout

Title: Patient – Logout

User Story:
As a patient, I want to log out of the portal, so that I can secure my account from unauthorized access.

Acceptance Criteria:

[ ] Patient can log out from any page through a visible logout button.

[ ] Session/token is invalidated after logout.

[ ] The system redirects to the login or homepage after logout.

[ ] After logout, re-accessing dashboard pages should not be possible without logging in again.

User Story 5 – Book Appointment

Title: Patient – Book an Appointment

User Story:
As a patient, I want to log in and book an hour-long appointment with a doctor, so that I can schedule consultations easily.

Acceptance Criteria:

[ ] Only logged-in patients can book appointments.

[ ] Patients can choose a doctor, date, and time slot (hour-long).

[ ] The system validates slot availability before confirming.

[ ] Upon confirmation, appointment details are saved and a success message is displayed.

[ ] Appointment data is stored in the database and visible in the patient’s dashboard.

Admin Login Functionality  Stories 


User Story 1 – Admin Login

Title: Admin can log into the portal securely

As an admin,
I want to log into the portal with my username and password,
so that I can securely access and manage the platform.

Acceptance Criteria:

Admin must enter a valid username and password.

The system validates credentials against the database.

If credentials are correct, access is granted to the admin dashboard.

If incorrect, an error message (“Invalid username or password”) is displayed.

The admin session should be secured using authentication tokens or session management.

User Story 2 – Admin Logout

Title: Admin can log out of the portal

As an admin,
I want to log out of the portal,
so that I can ensure no unauthorized access occurs after I leave the system.

Acceptance Criteria:

Admin can click the Logout button from any page.

Session/token must be invalidated upon logout.

System redirects admin to the login page after logout.

Access to admin dashboard should be blocked without re-login.

User Story 3 – Add Doctor to Portal

Title: Admin can add a new doctor’s profile

As an admin,
I want to add new doctors to the portal,
so that they can be listed in the system and manage appointments with patients.

Acceptance Criteria:

Admin can fill out a form with doctor details (Name, Specialty, Email, Contact, etc.).

The form data must be validated before submission.

The new doctor record should be saved in the database.

Confirmation message (“Doctor added successfully”) appears after successful addition.

The added doctor should appear in the doctor list immediately.

User Story 4 – Delete Doctor Profile

Title: Admin can delete an existing doctor’s profile

As an admin,
I want to delete a doctor’s profile from the portal,
so that I can remove inactive or incorrect records from the system.

Acceptance Criteria:

Admin can select a doctor from the list to delete.

A confirmation prompt appears before deletion (“Are you sure you want to delete this doctor?”).

Once confirmed, the doctor’s record is permanently removed from the database.

A success message (“Doctor profile deleted successfully”) should appear.

Deleted doctors should no longer be visible in the doctor list.

User Story 5 – Run MySQL Stored Procedure for Appointment Stats

Title: Admin can track monthly appointment statistics via MySQL

As an admin,
I want to run a stored procedure in the MySQL Command Line Interface (CLI),
so that I can get the number of appointments per month and analyze platform usage.

Acceptance Criteria:

A MySQL stored procedure (e.g., get_monthly_appointments()) must exist in the database.

The admin can execute the stored procedure in MySQL CLI or via an admin dashboard.

The result should display the total number of appointments grouped by month.

The data output can be used for internal analytics or performance monitoring.




