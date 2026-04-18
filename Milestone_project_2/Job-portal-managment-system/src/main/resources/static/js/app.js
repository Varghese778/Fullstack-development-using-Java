/**
 * Job Portal Management System - Core JavaScript
 */
document.addEventListener('DOMContentLoaded', function () {

    // Auto-dismiss alerts after 5s
    document.querySelectorAll('.alert-dismissible').forEach(function (alert) {
        setTimeout(function () {
            var bsAlert = bootstrap.Alert.getOrCreateInstance(alert);
            bsAlert.close();
        }, 5000);
    });

    // Notification badge poll (every 30 seconds)
    var notifBadge = document.getElementById('notif-badge');
    if (notifBadge) {
        function fetchUnreadCount() {
            fetch('/api/notifications/unread-count')
                .then(function (r) { return r.json(); })
                .then(function (data) {
                    if (data.success && data.data && data.data.count > 0) {
                        notifBadge.textContent = data.data.count;
                        notifBadge.classList.remove('d-none');
                    } else {
                        notifBadge.classList.add('d-none');
                    }
                })
                .catch(function () { /* silent */ });
        }
        fetchUnreadCount();
        setInterval(fetchUnreadCount, 30000);
    }

    // Confirmation dialogs for destructive actions
    document.querySelectorAll('[data-confirm]').forEach(function (el) {
        el.addEventListener('click', function (e) {
            if (!confirm(el.getAttribute('data-confirm'))) {
                e.preventDefault();
            }
        });
    });

    // File upload preview
    var fileInput = document.querySelector('input[type="file"]');
    if (fileInput) {
        fileInput.addEventListener('change', function () {
            var file = this.files[0];
            if (file && file.size > 5 * 1024 * 1024) {
                alert('File size exceeds 5MB limit!');
                this.value = '';
            }
        });
    }

    // Add smooth page transitions
    document.querySelectorAll('a:not([target="_blank"]):not([href^="#"])').forEach(function (link) {
        link.addEventListener('click', function (e) {
            if (link.hostname === window.location.hostname) {
                document.body.style.opacity = '0.7';
                document.body.style.transition = 'opacity 0.15s ease';
            }
        });
    });

    console.log('Job Portal JS loaded ✓');
});
