// ===============================
// Import Required Modules
// ===============================
import { openModal } from "./components/modals.js";
import { getDoctors, filterDoctors, saveDoctor } from "./services/doctorServices.js";
import { createDoctorCard } from "./components/doctorCard.js";

// ===============================
// Event Binding - Add Doctor Button
// ===============================
document.getElementById('addDocBtn').addEventListener('click', () => {
  openModal('addDoctor');
});

// ===============================
// Load Doctor Cards on Page Load
// ===============================
window.addEventListener('DOMContentLoaded', loadDoctorCards);

async function loadDoctorCards() {
  const contentDiv = document.getElementById("content");
  contentDiv.innerHTML = "<p>Loading doctors...</p>";

  try {
    const doctors = await getDoctors();

    if (!doctors || doctors.length === 0) {
      contentDiv.innerHTML = "<p>No doctors found.</p>";
      return;
    }

    renderDoctorCards(doctors);
  } catch (error) {
    console.error("Error loading doctors:", error);
    contentDiv.innerHTML = "<p>Failed to load doctors.</p>";
  }
}

// ===============================
// Render Doctor Cards Utility
// ===============================
function renderDoctorCards(doctors) {
  const contentDiv = document.getElementById("content");
  contentDiv.innerHTML = "";

  doctors.forEach(doctor => {
    const card = createDoctorCard(doctor);
    contentDiv.appendChild(card);
  });
}

// ===============================
// Search and Filter Logic
// ===============================
document.getElementById("searchBar").addEventListener("input", filterDoctorsOnChange);
document.getElementById("filterTime").addEventListener("change", filterDoctorsOnChange);
document.getElementById("filterSpecialty").addEventListener("change", filterDoctorsOnChange);

async function filterDoctorsOnChange() {
  const name = document.getElementById("searchBar").value.trim();
  const time = document.getElementById("filterTime").value.trim();
  const specialty = document.getElementById("filterSpecialty").value.trim();

  const contentDiv = document.getElementById("content");
  contentDiv.innerHTML = "<p>Filtering doctors...</p>";

  try {
    const doctors = await filterDoctors(name, time, specialty);

    if (!doctors || doctors.length === 0) {
      contentDiv.innerHTML = "<p>No doctors found.</p>";
      return;
    }

    renderDoctorCards(doctors);
  } catch (error) {
    console.error("Error filtering doctors:", error);
    contentDiv.innerHTML = "<p>Failed to filter doctors.</p>";
  }
}

// ===============================
// Add Doctor - Modal Handling
// ===============================
window.adminAddDoctor = async function () {
  const name = document.getElementById("doctorName").value.trim();
  const specialty = document.getElementById("doctorSpecialty").value.trim();
  const email = document.getElementById("doctorEmail").value.trim();
  const password = document.getElementById("doctorPassword").value.trim();
  const mobile = document.getElementById("doctorMobile").value.trim();

  // Collect checkbox values for availability time
  const availabilityCheckboxes = document.querySelectorAll('input[name="availability"]:checked');
  const availability = Array.from(availabilityCheckboxes).map(cb => cb.value);

  // Validate admin authentication token
  const token = localStorage.getItem("token");
  if (!token) {
    alert("You are not authorized. Please log in again.");
    return;
  }

  const doctor = { name, specialty, email, password, mobile, availability };

  try {
    const result = await saveDoctor(doctor, token);

    if (result.success) {
      alert(result.message || "Doctor added successfully!");
      document.getElementById('addDoctorModal').style.display = 'none'; // Close modal if applicable
      loadDoctorCards(); // Refresh doctor list
    } else {
      alert(result.message || "Failed to add doctor.");
    }
  } catch (error) {
    console.error("Error adding doctor:", error);
    alert("An unexpected error occurred while adding the doctor.");
  }
};
