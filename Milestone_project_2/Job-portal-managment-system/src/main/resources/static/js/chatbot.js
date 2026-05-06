/**
 * JobBot Chatbot Widget — Frontend JavaScript
 */
(function() {
    let sessionId = 'chat-' + Date.now() + '-' + Math.random().toString(36).substr(2, 9);
    const toggle = document.getElementById('chat-toggle');
    const window_ = document.getElementById('chat-window');
    const input = document.getElementById('chat-input');
    const sendBtn = document.getElementById('chat-send');
    const messages = document.getElementById('chat-messages');
    const suggestions = document.getElementById('chat-suggestions');
    const closeBtn = document.getElementById('chat-close');
    const clearBtn = document.getElementById('chat-clear');

    if (!toggle) return;

    toggle.addEventListener('click', () => {
        window_.classList.toggle('d-none');
        if (!window_.classList.contains('d-none')) {
            input.focus();
            messages.scrollTop = messages.scrollHeight;
        }
    });

    closeBtn.addEventListener('click', () => window_.classList.add('d-none'));

    clearBtn.addEventListener('click', () => {
        fetch('/api/chat/session?sessionId=' + sessionId, { method: 'DELETE' });
        sessionId = 'chat-' + Date.now() + '-' + Math.random().toString(36).substr(2, 9);
        messages.innerHTML = '<div class="chat-msg bot"><div class="chat-bubble">Chat cleared! How can I help you?</div></div>';
        suggestions.innerHTML = '';
        addDefaultSuggestions();
    });

    sendBtn.addEventListener('click', sendMessage);
    input.addEventListener('keypress', (e) => { if (e.key === 'Enter') sendMessage(); });

    function sendMessage() {
        const text = input.value.trim();
        if (!text) return;
        appendMessage('user', text);
        input.value = '';
        suggestions.innerHTML = '';
        showTyping();

        const csrfToken = document.querySelector('meta[name="_csrf"]');
        const csrfHeader = document.querySelector('meta[name="_csrf_header"]');
        const headers = { 'Content-Type': 'application/json' };
        if (csrfToken && csrfHeader) {
            headers[csrfHeader.content] = csrfToken.content;
        }

        fetch('/api/chat', {
            method: 'POST',
            headers: headers,
            body: JSON.stringify({ message: text, sessionId: sessionId })
        })
        .then(r => r.json())
        .then(data => {
            removeTyping();
            if (data.data && data.data.reply) {
                appendMessage('bot', data.data.reply);
                if (data.data.sessionId) sessionId = data.data.sessionId;
                if (data.data.suggestions && data.data.suggestions.length > 0) {
                    showSuggestions(data.data.suggestions);
                }
            } else {
                appendMessage('bot', 'Sorry, I could not process that. Please try again.');
            }
        })
        .catch(() => {
            removeTyping();
            appendMessage('bot', 'Connection error. Please try again later.');
        });
    }

    function appendMessage(role, text) {
        const div = document.createElement('div');
        div.className = 'chat-msg ' + role;
        div.innerHTML = '<div class="chat-bubble">' + escapeHtml(text) + '</div>';
        messages.appendChild(div);
        messages.scrollTop = messages.scrollHeight;
    }

    function showTyping() {
        const div = document.createElement('div');
        div.className = 'chat-msg bot typing-indicator';
        div.innerHTML = '<div class="chat-bubble"><span class="dot"></span><span class="dot"></span><span class="dot"></span></div>';
        messages.appendChild(div);
        messages.scrollTop = messages.scrollHeight;
    }

    function removeTyping() {
        const t = messages.querySelector('.typing-indicator');
        if (t) t.remove();
    }

    function showSuggestions(items) {
        suggestions.innerHTML = '';
        items.forEach(s => {
            const btn = document.createElement('button');
            btn.className = 'suggestion-chip';
            btn.textContent = s;
            btn.onclick = () => { input.value = s; sendMessage(); };
            suggestions.appendChild(btn);
        });
    }

    function addDefaultSuggestions() {
        showSuggestions(['Job Recommendations', 'Resume Help', 'Career Advice']);
    }

    function escapeHtml(t) {
        const d = document.createElement('div');
        d.textContent = t;
        return d.innerHTML;
    }

    window.sendSuggestion = function(text) {
        input.value = text;
        sendMessage();
    };
})();
