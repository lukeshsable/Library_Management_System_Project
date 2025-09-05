// Ensure this file is saved with UTF-8 encoding (no BOM)
// Show/Hide password toggle
document.addEventListener("DOMContentLoaded", function () {
  const passwordField = document.querySelector("input[type='password']");
  if (passwordField) {
    const toggleButton = document.createElement("span");
    toggleButton.textContent = "Show"; // Replaced 👁️ with "Show"
    toggleButton.style.cursor = "pointer";
    toggleButton.style.position = "absolute";
    toggleButton.style.right = "10px";
    toggleButton.style.top = "50%";
    toggleButton.style.transform = "translateY(-50%)";
    toggleButton.style.color = "#ccc";

    const inputField = passwordField.parentElement;
    inputField.style.position = "relative";
    inputField.appendChild(toggleButton);

    toggleButton.addEventListener("click", () => {
      const type = passwordField.getAttribute("type") === "password" ? "text" : "password";
      passwordField.setAttribute("type", type);
     // toggleButton.textContent = type === "password" ? "Show" : "Hide"; // Replaced 👁️/🙈 with Show/Hide
	  toggleButton.innerHTML = '<i class="fas fa-eye"></i>';
	  toggleButton.innerHTML = type === "password" ? '<i class="fas fa-eye"></i>' : '<i class="fas fa-eye-slash"></i>';
    });
  }

  // Optional: Alert role selection (for UX feedback)
  const form = document.querySelector("form");
  form.addEventListener("submit", function (e) {
    const role = form.querySelector("select[name='role']");
    if (!role.value) {
      e.preventDefault();
      alert("Please select a role before submitting.");
    }
  });
});
	
  // Form submission validation (for role selection in register.jsp)
  const form = document.querySelector("form");
  if (form) {
    form.addEventListener("submit", function (e) {
      const role = form.querySelector("select[name='role']");
      if (role && !role.value) {
        e.preventDefault();
        alert("Please select a role before submitting.");
      }
    });
  }

  // Animation for Login link (from register.jsp to login.jsp)
  const loginLink = document.querySelector('a[href="login.jsp"]');
  if (loginLink) {
    loginLink.addEventListener("click", function (e) {
      e.preventDefault();
      const wrapper = document.querySelector(".wrapper");
      wrapper.style.transition = "opacity 0.5s ease-in-out";
      wrapper.style.opacity = "0";
      setTimeout(() => {
        window.location.href = loginLink.href;
      }, 500);
    });
  }
  // Animation for Register link (from login.jsp to register.jsp)
  const registerLink = document.querySelector('a[href="register.jsp"]');
    if (registerLink) {
      registerLink.addEventListener("click", function (e) {
        e.preventDefault();
        const wrapper = document.querySelector(".wrapper");
        wrapper.style.transition = "opacity 0.5s ease-in-out";
        wrapper.style.opacity = "0";
        setTimeout(() => {
          window.location.href = registerLink.href;
        }, 500);
      });
    }

  // Fade-in animation on page load (for both register.jsp and login.jsp)
  const wrapper = document.querySelector(".wrapper");
  if (wrapper) {
    wrapper.style.opacity = "0";
    wrapper.style.transition = "opacity 0.5s ease-in-out";
    setTimeout(() => {
      wrapper.style.opacity = "1";
    }, 50);
  }

