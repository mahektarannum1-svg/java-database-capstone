// doctorCard.js - Reusable Doctor Card Component

/**
 * Creates a dynamic doctor card element
 * @param {Object} doctor - Doctor object containing name, specialty, email, availability, etc.
 * @returns {HTMLElement} - The complete doctor card element
 */
export function createDoctorCard(doctor) {
    // Create the main card container
    const card = document.createElement("div");
    card.classList.add("doctor-card");

    // Fetch the user's role from localStorage
    const role = localStorage.getItem("userRole");

    // Create doctor info section
    const infoDiv = document.createElement("div");
    infoDiv.classList.add("doctor-info");

    // Create and add doctor name
    const name = document.createElement("h3");
    name.textContent = doctor.name;

    // Create and add specialization
    const specialization = document.createElement("p");
    specialization.classList.add("specialty");
    specialization.textContent = `Specialty: ${doctor.specialization || doctor.specialty || "General"}`;

    // Create and add email
    const email = document.createElement("p");
    email.textContent = `Email: ${doctor.email}`;

    // Create and add availability
    const availability = document.createElement("p");
    if (Array.isArray(doctor.availability)) {
        availability.textContent = `Available: ${doctor.availability.join(", ")}`;
    } else {
        availability.textContent = `Available: ${doctor.availability || "Not specified"}`;
    }

    // Append all info elements to infoDiv
    infoDiv.appendChild(name);
    infoDiv.appendChild(specialization);
    infoDiv.appendChild(email);
    infoDiv.appendChild(availability);

    // Create button container
    const actionsDiv = document.createElement("div");
    actionsDiv.classList.add("card-actions");

    // Conditionally add buttons based on role
    if (role === "admin") {
        // Admin: Show Delete button
        const removeBtn = document.createElement("button");
        removeBtn.textContent = "Delete";
        removeBtn.classList.add("delete-btn");

        removeBtn.addEventListener("click", async () => {
            // 1. Confirm deletion
            const confirmed = confirm(`Are you sure you want to delete Dr. ${doctor.name}?`);
            
            if (!confirmed) return;

            try {
                // 2. Get token from localStorage
                const token = localStorage.getItem("token");

                if (!token) {
                    alert("Authentication required. Please log in again.");
                    window.location.href = "/";
                    return;
                }

                // 3. Call API to delete doctor
                // Import deleteDoctor from doctorServices.js
                const { deleteDoctor } = await import("../services/doctorServices.js");
                const success = await deleteDoctor(doctor.id, token);

                // 4. On success: remove the card from the DOM
                if (success) {
                    card.remove();
                    alert(`Dr. ${doctor.name} has been deleted successfully.`);
                } else {
                    alert("Failed to delete doctor. Please try again.");
                }
            } catch (error) {
                console.error("Error deleting doctor:", error);
                alert("An error occurred while deleting the doctor.");
            }
        });

        actionsDiv.appendChild(removeBtn);

    } else if (role === "patient") {
        // Patient (not logged in): Show Book Now button but require login
        const bookNow = document.createElement("button");
        bookNow.textContent = "Book Now";
        bookNow.classList.add("book-btn");

        bookNow.addEventListener("click", () => {
            alert("Patient needs to login first.");
            // Optionally redirect to login or open login modal
        });

        actionsDiv.appendChild(bookNow);

    } else if (role === "loggedPatient") {
        // Logged-in Patient: Show Book Now button with full functionality
        const bookNow = document.createElement("button");
        bookNow.textContent = "Book Now";
        bookNow.classList.add("book-btn");

        bookNow.addEventListener("click", async (e) => {
            try {
                // Get token from localStorage
                const token = localStorage.getItem("token");

                if (!token) {
                    alert("Session expired. Please log in again.");
                    window.location.href = "/pages/patientDashboard.html";
                    return;
                }

                // Fetch patient data
                const { getPatientData } = await import("../services/patientServices.js");
                const patientData = await getPatientData(token);

                if (!patientData) {
                    alert("Unable to fetch patient data. Please try again.");
                    return;
                }

                // Show booking overlay/modal
                const { showBookingOverlay } = await import("../components/modals.js");
                showBookingOverlay(e, doctor, patientData);

            } catch (error) {
                console.error("Error during booking:", error);
                alert("An error occurred while processing your booking request.");
            }
        });

        actionsDiv.appendChild(bookNow);
    }

    // Final assembly - add all sections to the card
    card.appendChild(infoDiv);
    card.appendChild(actionsDiv);

    // Return the complete card element
    return card;
}

/**
 * Helper function to render multiple doctor cards into a container
 * @param {Array} doctors - Array of doctor objects
 * @param {string} containerId - ID of the container element
 */
export function renderDoctorCards(doctors, containerId = "content") {
    const container = document.getElementById(containerId);
    
    if (!container) {
        console.error(`Container with id "${containerId}" not found`);
        return;
    }

    // Clear existing content
    container.innerHTML = "";

    // Check if there are doctors to display
    if (!doctors || doctors.length === 0) {
        container.innerHTML = '<p class="no-doctors">No doctors available at the moment.</p>';
        return;
    }

    // Create and append each doctor card
    doctors.forEach(doctor => {
        const card = createDoctorCard(doctor);
        container.appendChild(card);
    });
}