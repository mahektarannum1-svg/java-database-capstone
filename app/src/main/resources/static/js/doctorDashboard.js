// ----------------------------
// doctorDashboard.js
// Handles doctor's appointment management UI
// ----------------------------

// Import required modules
import { getAllAppointments } from "./services/appointmentRecordService.js";
import { createPatientRow } from "./components/patientRows.js";

// ----------------------------
// Global Variables
// ----------------------------
let patientTableBody = document.getElementById("patientTableBody");
let selectedDate = new Date().toISOString().split("T")[0]; // today's date (yyyy-mm-dd)
let token = localStorage.getItem("token");
let patientName = null;

// ----------------------------
// Search Bar Functionality
// ----------------------------
document.getElementById("searchBar").addEventListener("input", async (event) => {
  patientName = event.target.value.trim() || "null";
  await loadAppointments();
});

// ----------------------------
// Filter: Today's Appointments
// ----------------------------
document.getElementById("todayButton").addEventListener("click", async () => {
  selectedDate = new Date().toISOString().split("T")[0];
  document.getElementById("datePicker").value = selectedDate;
  await loadAppointments();
});

// ----------------------------
// Filter: Date Picker Change
// ----------------------------
document.getElementById("datePicker").addEventListener("change", async (event) => {
  selectedDate = event.target.value;
  await loadAppointments();
});

// ----------------------------
// Load Appointments Function
// ----------------------------
async function loadAppointments() {
  try {
    // Clear the table first
    patientTableBody.innerHTML = "";

    // Fetch appointment data
    const appointments = await getAllAppointments(selectedDate, patientName, token);

    // If no appointments found
    if (!appointments || appointments.length === 0) {
      const row = document.createElement("tr");
      row.innerHTML = `
        <td colspan="5" class="text-center text-muted py-3">
          No Appointments found for selected date
        </td>`;
      patientTableBody.appendChild(row);
      return;
    }

    // Render appointment rows
    appointments.forEach((appointment) => {
      const row = createPatientRow(appointment);
      patientTableBody.appendChild(row);
    });

  } catch (error) {
    console.error("Error loading appointments:", error);

    const errorRow = document.createElement("tr");
    errorRow.innerHTML = `
      <td colspan="5" class="text-center text-danger py-3">
        Error fetching appointments. Please try again later.
      </td>`;
    patientTableBody.appendChild(errorRow);
  }
}

// ----------------------------
// Initial Render
// ----------------------------
document.addEventListener("DOMContentLoaded", async () => {
  // Default to today's date
  document.getElementById("datePicker").value = selectedDate;
  await loadAppointments();
});
