// header.js - Dynamic Header Component

/**
 * Main function to render the header based on user role and login state
 */
function renderHeader() {
    const headerDiv = document.getElementById("header");
    
    if (!headerDiv) {
        console.warn("Header element not found");
        return;
    }

    // Check if on homepage - clear session data
    if (window.location.pathname.endsWith("/")) {
        localStorage.removeItem("userRole");
        localStorage.removeItem("token");
    }

    // Get user role and token from localStorage
    const role = localStorage.getItem("userRole");
    const token = localStorage.getItem("token");

    // Validate token for logged-in users
    if ((role === "loggedPatient" || role === "admin" || role === "doctor") && !token) {
        localStorage.removeItem("userRole");
        alert("Session expired or invalid login. Please log in again.");
        window.location.href = "/";
        return;
    }

    // Build header content based on role
    let headerContent = `
        <header class="header">
            <div class="logo">
                <h1>Clinic Management</h1>
            </div>
            <nav class="nav-links">
    `;

    // Role-specific navigation
    if (role === "admin") {
        headerContent += `
                <button id="addDocBtn" class="adminBtn" onclick="openModal('addDoctor')">Add Doctor</button>
                <a href="#" onclick="logout()">Logout</a>
        `;
    } else if (role === "doctor") {
        headerContent += `
                <a href="/doctor/doctorDashboard">Home</a>
                <a href="#" onclick="logout()">Logout</a>
        `;
    } else if (role === "patient") {
        headerContent += `
                <button id="loginBtn" class="header-btn">Login</button>
                <button id="signupBtn" class="header-btn">Sign Up</button>
        `;
    } else if (role === "loggedPatient") {
        headerContent += `
                <a href="/pages/patientDashboard.html">Home</a>
                <a href="/pages/appointments.html">Appointments</a>
                <a href="#" onclick="logoutPatient()">Logout</a>
        `;
    }

    headerContent += `
            </nav>
        </header>
    `;

    // Inject header into the page
    headerDiv.innerHTML = headerContent;

    // Attach event listeners to dynamically created elements
    attachHeaderButtonListeners();
}

/**
 * Attach event listeners to header buttons
 */
function attachHeaderButtonListeners() {
    // Login button for patients
    const loginBtn = document.getElementById("loginBtn");
    if (loginBtn) {
        loginBtn.addEventListener("click", () => {
            openModal("login");
        });
    }

    // Signup button for patients
    const signupBtn = document.getElementById("signupBtn");
    if (signupBtn) {
        signupBtn.addEventListener("click", () => {
            openModal("signup");
        });
    }

    // Add Doctor button for admin
    const addDocBtn = document.getElementById("addDocBtn");
    if (addDocBtn) {
        addDocBtn.addEventListener("click", () => {
            openModal("addDoctor");
        });
    }
}

/**
 * Logout function for admin and doctor
 * Clears session and redirects to homepage
 */
function logout() {
    // Clear session data
    localStorage.removeItem("token");
    localStorage.removeItem("userRole");

    // Show logout message
    alert("You have been logged out successfully.");

    // Redirect to homepage
    window.location.href = "/";
}

/**
 * Logout function specifically for patients
 * Retains patient role but removes token
 */
function logoutPatient() {
    // Remove token but keep role as "patient"
    localStorage.removeItem("token");
    localStorage.setItem("userRole", "patient");

    // Show logout message
    alert("You have been logged out successfully.");

    // Redirect to patient dashboard (shows login/signup)
    window.location.href = "/pages/patientDashboard.html";
}

/**
 * Placeholder for openModal function
 * This should be implemented in modals.js or util.js
 */
function openModal(modalType) {
    console.log(`Opening modal: ${modalType}`);
    // This function will be implemented in modals.js
    // It will handle opening different types of modals based on modalType
}

// Auto-render header when DOM is loaded
if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", renderHeader);
} else {
    renderHeader();
}