Section 1: Architecture summary
his Spring Boot application uses both MVC and REST controllers. Thymeleaf templates are used for the Admin and Doctor dashboards, while REST APIs serve all other modules. The application interacts with two databases—MySQL (for patient, doctor, appointment, and admin data) and MongoDB (for prescriptions). All controllers route requests through a common service layer, which in turn delegates to the appropriate repositories. MySQL uses JPA entities while MongoDB uses document models.


Section 2: Numbered flow of data and control

1. The user accesses the application through either the Thymeleaf-based dashboards (e.g., AdminDashboard, DoctorDashboard) or through REST API clients such as PatientDashboard, Appointments, or PatientRecord.


2. The user action (e.g., submitting a form, viewing records, booking an appointment) triggers an HTTP request that is routed to the appropriate controller based on the URL path and request type (GET, POST, etc.).


3. The controller (Thymeleaf or REST) receives the request and validates the input data.

Thymeleaf controllers return .html templates populated with dynamic data.

REST controllers return JSON responses for API-based clients.



4. The controller delegates the business logic execution to the Service Layer, ensuring that the controller remains lightweight and focused on request handling only.


5. The Service Layer processes the request by applying business rules and performing operations such as verifying doctor availability, managing appointments, or updating patient information.


6. To access or modify stored data, the Service Layer communicates with the Repository Layer, which handles all database-related operations.


7. The Repository Layer interacts with the underlying databases using Spring Data frameworks:

MySQL Repositories manage relational data for entities like doctors, patients, and appointments.

MongoDB Repositories manage flexible, document-oriented data such as prescriptions.



8. The repositories fetch or persist data and return the results to the Service Layer in the form of model objects.


9. The Service Layer processes the received data, performs any necessary transformations, and sends it back to the Controller Layer.


10. The Controller Layer prepares the final response:

For web clients, data models are passed to Thymeleaf templates to render dynamic HTML pages.

For API clients, data is converted to JSON format and returned as the HTTP response.



11. The user interface displays the processed information—such as updated appointments, patient records, or dashboard summaries—completing the request–response cycle.
