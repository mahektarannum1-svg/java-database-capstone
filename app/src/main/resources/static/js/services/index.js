// ===============================
// Import Required Modules
// ===============================
import { openModal } from "../components/modals.js";
import { API_BASE_URL } from "../config/config.js";

// Define API endpoints
const ADMIN_API = API_BASE_URL + '/admin';
const DOCTOR_API = API_BASE_URL + '/doctor/login';

// ===============================
// Setup Button Event Listeners
// ===============================
window.onload = function () {
  const adminBtn = document.getElementById('adminLogin');
  const doctorBtn = document.getElementById('doctorLogin');

  // Open Admin Login Modal
  if (adminBtn) {
    adminBtn.addEventListener('click', () => {
      openModal('adminLogin');
    });
  }

  // Open Doctor Login Modal
  if (doctorBtn) {
    doctorBtn.addEventListener('click', () => {
      openModal('doctorLogin');
    });
  }
};

// ===============================
// Admin Login Handler
// ===============================
window.adminLoginHandler = async function () {
  const username = document.getElementById('adminUsername').value;
  const password = document.getElementById('adminPassword').value;

  const admin = { username, password };

  try {
    const response = await fetch(ADMIN_API, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(admin)
    });

    if (response.ok) {
      const data = await response.json();
      localStorage.setItem('token', data.token);
      selectRole('admin'); // defined in render.js
    } else {
      alert('Invalid credentials!');
    }
  } catch (error) {
    alert('An error occurred: ' + error.message);
  }
};

// ===============================
// Doctor Login Handler
// ===============================
window.doctorLoginHandler = async function () {
  const email = document.getElementById('doctorEmail').value;
  const password = document.getElementById('doctorPassword').value;

  const doctor = { email, password };

  try {
    const response = await fetch(DOCTOR_API, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(doctor)
    });

    if (response.ok) {
      const data = await response.json();
      localStorage.setItem('token', data.token);
      selectRole('doctor'); // defined in render.js
    } else {
      alert('Invalid credentials!');
    }
  } catch (error) {
    alert('An error occurred: ' + error.message);
  }
};
