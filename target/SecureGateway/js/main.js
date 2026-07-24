/**
 * SecureGateway – main.js
 * Vanilla JavaScript – no external frameworks.
 */

'use strict';

// ── Password toggle ──────────────────────────────────────────────────────────
function togglePassword() {
    const pwField = document.getElementById('password');
    const eyeIcon = document.getElementById('eyeIcon');
    if (!pwField) return;
    if (pwField.type === 'password') {
        pwField.type = 'text';
        if (eyeIcon) { eyeIcon.classList.remove('fa-eye'); eyeIcon.classList.add('fa-eye-slash'); }
    } else {
        pwField.type = 'password';
        if (eyeIcon) { eyeIcon.classList.remove('fa-eye-slash'); eyeIcon.classList.add('fa-eye'); }
    }
}

// ── Auto-dismiss alerts ──────────────────────────────────────────────────────
(function autoDismissAlerts() {
    const alerts = document.querySelectorAll('.alert.alert-success, .alert.alert-info');
    alerts.forEach(function (el) {
        setTimeout(function () {
            el.style.transition = 'opacity 0.6s ease';
            el.style.opacity = '0';
            setTimeout(function () { el.remove(); }, 650);
        }, 4000);
    });
})();

// ── Active nav link highlight ────────────────────────────────────────────────
(function highlightActiveNav() {
    const currentPath = window.location.pathname;
    document.querySelectorAll('.nav-link').forEach(function (link) {
        const href = link.getAttribute('href');
        if (href && currentPath.endsWith(href.split('?')[0].replace(/.*\//, '/'))) {
            link.classList.add('active');
        }
    });
})();

// ── Confirm dialogs already inline via onclick="return confirm(...)" ─────────

// ── File upload preview ──────────────────────────────────────────────────────
(function fileUploadFeedback() {
    const fileInputs = document.querySelectorAll('input[type="file"]');
    fileInputs.forEach(function (input) {
        input.addEventListener('change', function () {
            const file = this.files[0];
            if (!file) return;
            const maxBytes = 10 * 1024 * 1024;
            if (file.size > maxBytes) {
                alert('File exceeds the 10 MB limit. Please choose a smaller file.');
                this.value = '';
                return;
            }
            const label = this.nextElementSibling;
            if (label && label.classList.contains('file-label')) {
                label.textContent = file.name + ' (' + (file.size / 1024).toFixed(1) + ' KB)';
            }
        });
    });
})();

// ── Table row click to select message ────────────────────────────────────────
(function tableRowHighlight() {
    document.querySelectorAll('.table tbody tr').forEach(function (row) {
        row.addEventListener('click', function (e) {
            if (e.target.tagName === 'A' || e.target.tagName === 'BUTTON' ||
                e.target.closest('a') || e.target.closest('button')) return;
            const link = row.querySelector('a.msg-subject-link');
            if (link) link.click();
        });
    });
})();
