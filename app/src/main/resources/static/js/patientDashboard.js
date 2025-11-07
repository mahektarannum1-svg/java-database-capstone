// --------------------------------------------
// patientDashboard.js
// Handles doctor listing, filtering, and patient authentication
// --------------------------------------------

// Import required modules
import { createDoctorCard } from "./components/doctorCard.js";
import { openModal } from "./components/modals.js";
import { getDoctors, filterDoctors } from "./services/doctorServices.js";
import { patientLogin, patientSignup } from "./services/patientServices.js";

// --------------------------------------------
// Load Doctor Cards on Page Load
// --------------------------------------------
document.addEventListener("DOMContentLoaded", () => {
  loadDoctorCards();

  // Bind modal triggers
  const signupBtn = document.getElementById("patientSignup");
  if (signupBtn) signupBtn.addEventListener("click", () => openModal("patientSignup"));

  const loginBtn = document.getElementById("patientLogin");
  if (loginBtn) loginBtn.addEventListener("click", () => openModal("patientLogin"));

  // Bind search & filter logic
  const searchBar = document.getElementById("searchBar");
  const filterTime = document.getElementById("filterTime");
  const filterSpecialty = document.getElementById("filterSpecialty");

  if (searchBar) searchBar.addEventListener("input", filterDoctorsOnChange);
  if (filterTime) filterTime.addEventListener("change", filterDoctorsOnChange);
  if (filterSpecialty) filterSpecialty.addEventListener("change", filterDoctorsOnChange);
});

// --------------------------------------------
// Function: Load All Doctors
// --------------------------------------------
async function loadDoctorCards() {
  const contentDiv = document.getElementById("content");
  contentDiv.innerHTML = "<p>Loading doctors...</p>";

  try {
    const doctors = await getDoctors();

    // Clear existing content
    contentDiv.innerHTML = "";

    if (!doctors || doctors.length === 0) {
      contentDiv.innerHTML = `<p>No doctors found at the moment.</p>`;
      return;
    }

    // Render all doctor cards
    doctors.forEach((doctor) => {
      const card = createDoctorCard(doctor);
      contentDiv.appendChild(card);
    });
  } catch (error) {
    console.error("Error loading doctors:", error);
    contentDiv.innerHTML = `<p class="text-danger">Failed to load doctors. Please try again later.</p>`;
  }
}

// --------------------------------------------
// Function: Search & Filter Doctors
// --------------------------------------------
async function filterDoctorsOnChange() {
  const name = document.getElementById("searchBar")?.value.trim() || "";
  const time = document.getElementById("filterTime")?.value || "";
  const specialty = document.getElementById("filterSpecialty")?.value || "";
  const contentDiv = document.getElementById("content");

  contentDiv.innerHTML = "<p>Filtering doctors...</p>";

  try {
    const doctors = await filterDoctors(name, time, specialty);

    // Clear content
    contentDiv.innerHTML = "";

    if (!doctors || doctors.length === 0) {
      contentDiv.innerHTML = `<p>No doctors found with the given filters.</p>`;
      return;
    }

    renderDoctorCards(doctors);
  } catch (error) {
    console.error("Error filtering doctors:", error);
    contentDiv.innerHTML = `<p class="text-danger">Error fetching filtered doctors.</p>`;
  }
}

// --------------------------------------------
// Utility Function: Render Doctor Cards
// --------------------------------------------
function renderDoctorCards(doctors) {
  const contentDiv = document.getElementById("content");
  contentDiv.innerHTML = "";

  doctors.forEach((doctor) => {
    const card = createDoctorCard(doctor);
    contentDiv.appendChild(card);
  });
}

// --------------------------------------------
// Function: Handle Patient Signup
// --------------------------------------------
window.signupPatient = async function () {
  try {
    const name = document.getElementById("signupName").value.trim();
    const email = document.getElementById("signupEmail").value.trim();
    const password = document.getElementById("signupPassword").value.trim();
    const phone = document.getElementById("signupPhone").value.trim();
    const address = document.getElementById("signupAddress").value.trim();

    if (!name || !email || !password) {
      alert("Please fill in all required fields.");
      return;
    }

    const response = await patientSignup({ name, email, password, phone, address });

    if (response.success) {
      alert(response.message || "Signup successful!");
      document.getElementById("patientSignupModal")?.classList.remove("show");
      loadDoctorCards(); // reload after signup
    } else {
      alert(response.message || "Signup failed. Please try again.");
    }
  } catch (error) {
    console.error("Signup error:", error);
    alert("Something went wrong during signup.");
  }
};

// --------------------------------------------
// Function: Handle Patient Login
// --------------------------------------------
window.loginPatient = async function () {
  try {
    const email = document.getElementById("loginEmail").value.trim();
    const password = document.getElementById("loginPassword").value.trim();

    if (!email || !password) {
      alert("Please enter both email and password.");
      return;
    }

    const response = await patientLogin({ email, password });

    if (response.ok) {
      const data = await response.json();
      localStorage.setItem("token", data.token);
      alert("Login successful!");
      window.location.href = "loggedPatientDashboard.html";
    } else {
      alert("Invalid credentials. Please try again.");
    }
  } catch (error) {
    console.error("Login error:", error);
    alert("Login failed. Please try again later.");
  }
};
