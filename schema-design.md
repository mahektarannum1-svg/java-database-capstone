MySQL Database Design
Table: patients

id: INT, Primary Key, AUTO_INCREMENT

first_name: VARCHAR(100), NOT NULL

last_name: VARCHAR(100), NOT NULL

email: VARCHAR(255), UNIQUE, NOT NULL -- validate format in application code

phone: VARCHAR(20), UNIQUE, NOT NULL -- validate format in application code

dob: DATE, NULL

gender: ENUM('M','F','O'), NULL

address: TEXT, NULL

created_at: DATETIME, NOT NULL, DEFAULT CURRENT_TIMESTAMP

updated_at: DATETIME, NOT NULL, DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP

is_deleted: TINYINT(1), NOT NULL, DEFAULT 0 -- soft-delete flag (recommended)

-- Comment: Use soft-delete (is_deleted) so patient history (appointments, prescriptions) is retained for audits/medical history. Hard delete is discouraged.

Table: doctors

id: INT, Primary Key, AUTO_INCREMENT

first_name: VARCHAR(100), NOT NULL

last_name: VARCHAR(100), NOT NULL

email: VARCHAR(255), UNIQUE, NOT NULL -- validate in code

phone: VARCHAR(20), UNIQUE, NOT NULL -- validate in code

specialization: VARCHAR(150), NULL

qualifications: TEXT, NULL

consultation_fee: DECIMAL(10,2), NOT NULL, DEFAULT 0.00

created_at: DATETIME, NOT NULL, DEFAULT CURRENT_TIMESTAMP

updated_at: DATETIME, NOT NULL, DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP

is_active: TINYINT(1), NOT NULL, DEFAULT 1

-- Comment: is_active allows temporary deactivation (vacation, suspended). Overlapping-appointment policy enforced in application layer (or via triggers/procedures if desired).

Table: appointments

id: INT, Primary Key, AUTO_INCREMENT

doctor_id: INT, Foreign Key → doctors(id), NOT NULL

patient_id: INT, Foreign Key → patients(id), NOT NULL

clinic_location_id: INT, Foreign Key → clinic_locations(id), NULL -- optional if teleconsultation or single location

appointment_start: DATETIME, NOT NULL

appointment_end: DATETIME, NOT NULL

status: ENUM('SCHEDULED','COMPLETED','CANCELLED','NO_SHOW'), NOT NULL, DEFAULT 'SCHEDULED'

booking_source: ENUM('WEB','MOBILE','ADMIN'), NOT NULL, DEFAULT 'WEB'

created_by_admin: INT, Foreign Key → admin(id), NULL -- who created it if admin created it

created_at: DATETIME, NOT NULL, DEFAULT CURRENT_TIMESTAMP

updated_at: DATETIME, NOT NULL, DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP

notes: TEXT, NULL

-- Comment: Do NOT rely on DB-level unique constraints for overlapping appointment prevention (MySQL lacks exclusion constraints). Enforce non-overlap in Service Layer: before inserting, check that appointment_start/appointment_end do not overlap with existing confirmed appointments for same doctor_id. Optionally implement DB triggers or serialized transactions to avoid race conditions.

-- Deletion policy: Prefer soft-delete of patients/doctors; for hard delete of patient, set patient_id to NULL? Better: never hard-delete — keep for audit. If a patient must be removed, cascade should NOT delete appointments; use ON DELETE SET NULL for patient_id if hard-delete allowed (but I recommend soft-delete).

Table: admin

id: INT, Primary Key, AUTO_INCREMENT

username: VARCHAR(100), UNIQUE, NOT NULL

email: VARCHAR(255), UNIQUE, NOT NULL

password_hash: VARCHAR(255), NOT NULL

role: ENUM('SUPER_ADMIN','ADMIN','RECEPTION'), NOT NULL, DEFAULT 'ADMIN'

full_name: VARCHAR(200), NULL

phone: VARCHAR(20), NULL

created_at: DATETIME, NOT NULL, DEFAULT CURRENT_TIMESTAMP

last_login_at: DATETIME, NULL

is_active: TINYINT(1), NOT NULL, DEFAULT 1

-- Comment: Store password as hashed value. Admins manage users, appointments, reports.

Table: clinic_locations

id: INT, Primary Key, AUTO_INCREMENT

name: VARCHAR(150), NOT NULL -- e.g., "Downtown Clinic"

address: TEXT, NULL

phone: VARCHAR(20), NULL

timezone: VARCHAR(50), NULL -- helpful for multi-location scheduling

created_at: DATETIME, NOT NULL, DEFAULT CURRENT_TIMESTAMP

-- Comment: Useful if the clinic has multiple branches or rooms.

Table: payments

id: INT, Primary Key, AUTO_INCREMENT

appointment_id: INT, Foreign Key → appointments(id), NOT NULL

patient_id: INT, Foreign Key → patients(id), NOT NULL

amount: DECIMAL(10,2), NOT NULL

currency: CHAR(3), NOT NULL, DEFAULT 'INR'

payment_method: ENUM('CARD','UPI','CASH','INSURANCE'), NOT NULL

status: ENUM('PENDING','PAID','FAILED','REFUNDED'), NOT NULL, DEFAULT 'PENDING'

transaction_id: VARCHAR(255), UNIQUE, NULL -- from payment gateway

paid_at: DATETIME, NULL

created_at: DATETIME, NOT NULL, DEFAULT CURRENT_TIMESTAMP

-- Comment: Keep payments tied to appointments for traceability. If offline payment exists, transaction_id can be NULL.

Table: prescriptions

id: INT, Primary Key, AUTO_INCREMENT

appointment_id: INT, Foreign Key → appointments(id), NULL -- nullable to allow prescriptions without appointment (if policy allows)

patient_id: INT, Foreign Key → patients(id), NOT NULL

doctor_id: INT, Foreign Key → doctors(id), NOT NULL

issued_at: DATETIME, NOT NULL, DEFAULT CURRENT_TIMESTAMP

content: TEXT, NOT NULL -- structured JSON or text of medicines + instructions

followup_days: INT, NULL

is_active: TINYINT(1), NOT NULL, DEFAULT 1

-- Comment: Prefer linking prescription to the appointment that generated it (appointment_id) when available. Keep patient_id and doctor_id to allow prescriptions if appointment deleted/soft-deleted. Consider storing prescriptions in separate document store (MongoDB) if structure varies a lot — you already have MongoDB in your architecture.

Table: doctor_availability (time slots)

id: INT, Primary Key, AUTO_INCREMENT

doctor_id: INT, Foreign Key → doctors(id), NOT NULL

clinic_location_id: INT, Foreign Key → clinic_locations(id), NULL

day_of_week: TINYINT, NULL -- 0=Sunday .. 6=Saturday (optional if using date ranges)

start_time: TIME, NOT NULL

end_time: TIME, NOT NULL

start_date: DATE, NULL -- for temporary schedules (like 2025-11-01 start)

end_date: DATE, NULL -- nullable for ongoing availability

is_recurring: TINYINT(1), NOT NULL, DEFAULT 1

created_at: DATETIME, NOT NULL, DEFAULT CURRENT_TIMESTAMP

-- Comment: This table defines available slots (repeating weekly or date-limited). Real appointment slot generation (e.g., 15-min or 30-min slots) can be computed from these ranges at booking-time. Use it to present only valid booking times to patients.

Indexes & Performance Suggestions

Index appointments(doctor_id, appointment_start, appointment_end) for fast overlap checks.

Index appointments(patient_id, appointment_start) for patient history queries.

Index doctor_availability(doctor_id, day_of_week) for quick schedule lookups.

Index emails/phones on patients and doctors.

Referential actions & deletion rules (recommended)

patients.id referenced by appointments/prescriptions/payments: prefer soft-delete on patients (is_deleted=1) so history remains intact. If hard-delete allowed: use ON DELETE SET NULL on appointments.patient_id (but this breaks integrity for historical data) — avoid hard-deleting medical records.

doctors.id: when a doctor leaves, mark is_active=0 and keep their appointments/prescriptions. Do not cascade delete.

appointments.id referenced by payments and prescriptions: prefer ON DELETE RESTRICT or disallow delete if there are payments/prescriptions. Soft-delete appointments instead.

clinic_locations.id: if a location removed, either prevent deletion or set clinic_location_id NULL on historical appointments.

Business rules & constraints (application-level)

Non-overlapping appointments for same doctor: Enforce in application service layer: before creating appointment, check for existing appointments where appointment_start < new_end AND appointment_end > new_start. Use transaction/locking to prevent race conditions.

Slot granularity: Decide slot length (e.g., 15m, 30m). Enforce in service logic when converting availability to bookable slots.

Appointment retention: Keep full patient appointment history forever (medical/legal reasons). Use soft-delete flags for data removal requests and an audit log table if needed.

Prescription linkage: Prefer tying prescriptions to an appointment for traceability; allow nullable appointment_id for exceptions.

Example small notes you can add as comments in your schema file

-- Use soft deletes for patients & appointments to preserve medical history and for audit.

-- Enforce overlapping checks in Service Layer; consider DB-level locks for concurrency.

-- Validate email & phone formats in application code and also consider uniqueness constraints at DB level.

MongoDB Collection Design
🩺 Collection: prescriptions { "_id": "ObjectId('675abc123001')", "appointmentId": 51, "patient": { "id": 101, "name": "John Smith" }, "doctor": { "id": 201, "name": "Dr. Emily Carter", "specialization": "Dermatologist" }, "medications": [ { "name": "Paracetamol", "dosage": "500mg", "instructions": "Take 1 tablet every 6 hours after meals" }, { "name": "Cetirizine", "dosage": "10mg", "instructions": "Take 1 tablet before bedtime" } ], "refillCount": 2, "pharmacy": { "name": "CareWell Pharmacy", "address": "Baker Street, London" }, "issuedDate": "2025-11-06T10:30:00Z", "tags": ["skin", "fever", "prescription"] }

🧠 Rationale: Each document keeps both doctor and patient details for easy retrieval. Nested arrays support multiple medications.

💬 Collection: feedback { "_id": "ObjectId('675abc123002')", "appointmentId": 73, "patientId": 101, "doctorId": 201, "rating": 4.5, "comments": "Doctor was very attentive and explained the treatment clearly.", "tags": ["positive", "consultation", "friendly"], "submittedAt": "2025-11-06T12:00:00Z", "metadata": { "responseStatus": "pending", "responseDate": null } }

🧠 Rationale: Helps track patient satisfaction and allows doctors/admins to respond to feedback later.

📜 Collection: logs { "_id": "ObjectId('675abc123003')", "userId": 201, "role": "doctor", "action": "Updated appointment status", "timestamp": "2025-11-06T09:15:00Z", "ipAddress": "192.168.0.22", "details": { "appointmentId": 51, "oldStatus": "Scheduled", "newStatus": "Completed" } }

🧠 Rationale: Tracks user activities for security and debugging purposes.

📱 Collection: messages { "_id": "ObjectId('675abc123004')", "conversationId": "conv_78901", "sender": { "id": 101, "role": "patient", "name": "John Smith" }, "receiver": { "id": 201, "role": "doctor", "name": "Dr. Emily Carter" }, "messages": [ { "text": "Hello Doctor, can I take medicine after breakfast?", "timestamp": "2025-11-06T08:45:00Z", "status": "delivered" }, { "text": "Yes, that’s fine. Avoid coffee right after though.", "timestamp": "2025-11-06T08:50:00Z", "status": "seen" } ], "lastUpdated": "2025-11-06T08:50:00Z" }

🧠 Rationale: Enables real-time patient-doctor communication with message history stored in a single document.
