document.addEventListener('DOMContentLoaded', function () {
    const loginForm = document.getElementById('loginForm');
    if (!loginForm) return;

    const emailInput = document.getElementById('email');
    const passwordInput = document.getElementById('password');

    function clearMessages() {
        const err = document.getElementById('loginErrors');
        if (err) err.remove();
        const succ = document.getElementById('loginSuccess');
        if (succ) succ.remove();
    }

    function showErrors(messages) {
        clearMessages();
        const div = document.createElement('div');
        div.id = 'loginErrors';
        div.style.cssText = 'background:#fee2e2;border:1px solid #fecaca;border-radius:12px;padding:12px;margin-bottom:12px;color:#991b1b;';
        const ul = document.createElement('ul');
        ul.style.margin = '0';
        ul.style.paddingLeft = '20px';
        messages.forEach(m => {
            const li = document.createElement('li');
            li.textContent = m;
            ul.appendChild(li);
        });
        div.appendChild(ul);
        loginForm.insertBefore(div, loginForm.firstChild);
    }

    function showSuccess(message) {
        clearMessages();
        const div = document.createElement('div');
        div.id = 'loginSuccess';
        div.style.cssText = 'background:#dcfce7;border:1px solid #86efac;border-radius:12px;padding:12px;margin-bottom:12px;color:#166534;font-weight:600;';
        div.textContent = message || 'Login realizado com sucesso! Redirecionando...';
        loginForm.insertBefore(div, loginForm.firstChild);
    }

    function validate(email, password) {
        const errors = [];
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!email || !emailRegex.test(email)) errors.push('E-mail inválido');
        if (!password || password.length < 4) errors.push('Senha inválida (mínimo 4 caracteres)');
        return errors;
    }

    loginForm.addEventListener('submit', async function (e) {
        e.preventDefault();
        clearMessages();

        const email = emailInput && emailInput.value.trim();
        const password = passwordInput && passwordInput.value;

        const errors = validate(email, password);
        if (errors.length) {
            showErrors(errors);
            return;
        }

        const submitBtn = loginForm.querySelector('button[type="submit"]');
        if (submitBtn) submitBtn.disabled = true;

        try {
            const res = await fetch('/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ username: email, password: password })
            });

            if (res.ok) {
                showSuccess();
                // Pequeno delay para o usuário ver a mensagem
                setTimeout(() => {
                    // Redireciona para a página de componentes
                    window.location.href = '../componentes/index.html';
                }, 700);
            } else if (res.status === 401 || res.status === 403) {
                showErrors(['E-mail ou senha inválidos']);
            } else {
                // tenta ler mensagem JSON do backend
                let msg = 'Erro ao efetuar login. Tente novamente.';
                try {
                    const data = await res.json();
                    if (data && data.message) msg = data.message;
                } catch (_) {}
                showErrors([msg]);
            }
        } catch (err) {
            console.error('Login request failed', err);
            showErrors(['Erro de conexão. Verifique sua rede e tente novamente.']);
        } finally {
            if (submitBtn) submitBtn.disabled = false;
        }
    });
});
