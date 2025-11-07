// footer.js - Static Footer Component

/**
 * Renders the footer component
 * This footer is static and appears the same across all pages
 */
function renderFooter() {
    // Access the footer container element
    const footer = document.getElementById("footer");
    
    if (!footer) {
        console.warn("Footer element not found");
        return;
    }

    // Inject HTML content into the footer
    footer.innerHTML = `
        <footer class="footer">
            <!-- Branding Section -->
            <div class="footer-logo">
                <img src="../assets/images/clinic-logo.png" alt="Clinic Logo" width="150" />
                <p>© Copyright 2025 Clinic Management System. All rights reserved.</p>
            </div>

            <!-- Footer Links Columns -->
            <div class="footer-links">
                <!-- Company Column -->
                <div class="footer-column">
                    <h4>Company</h4>
                    <a href="#about">About Us</a>
                    <a href="#careers">Careers</a>
                    <a href="#press">Press</a>
                    <a href="#blog">Blog</a>
                </div>

                <!-- Support Column -->
                <div class="footer-column">
                    <h4>Support</h4>
                    <a href="#account">Account</a>
                    <a href="#help">Help Center</a>
                    <a href="#contact">Contact Us</a>
                    <a href="#faq">FAQ</a>
                </div>

                <!-- Legal Column -->
                <div class="footer-column">
                    <h4>Legal</h4>
                    <a href="#terms">Terms of Service</a>
                    <a href="#privacy">Privacy Policy</a>
                    <a href="#licensing">Licensing</a>
                    <a href="#cookies">Cookie Policy</a>
                </div>
            </div>

            <!-- Social Media Links (Optional) -->
            <div class="footer-social">
                <h4>Follow Us</h4>
                <div class="social-icons">
                    <a href="#facebook" aria-label="Facebook">
                        <i class="fab fa-facebook"></i>
                    </a>
                    <a href="#twitter" aria-label="Twitter">
                        <i class="fab fa-twitter"></i>
                    </a>
                    <a href="#linkedin" aria-label="LinkedIn">
                        <i class="fab fa-linkedin"></i>
                    </a>
                    <a href="#instagram" aria-label="Instagram">
                        <i class="fab fa-instagram"></i>
                    </a>
                </div>
            </div>
        </footer>
    `;
}

// Auto-render footer when DOM is loaded
if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", renderFooter);
} else {
    renderFooter();
}

// Call the function immediately for pages that load the script after DOM is ready
renderFooter();