const API_BASE_URL = 'http://localhost:8080';

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

    // Modificado para seguir estritamente a base e padrão do primeiro código fornecido
    loginForm.addEventListener('submit', function (e) {
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

        const url = `${API_BASE_URL}/auth/login`;
        const body = { email: email, senha: password };

        // Fluxo baseado no encadeamento de promessas .then().catch() do primeiro código
        fetch(url, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body)
        })
        .then(res => {
            // Se o servidor responder com erro de credenciais (401/403) ou qualquer outro erro
            if (res.status === 401 || res.status === 403) {
                throw new Error('E-mail ou senha inválidos');
            }
            if (!res.ok) {
                throw new Error(`Erro no servidor: ${res.status}`);
            }
            return res.json();
        })
        .then(data => {
            // Guarda o token JWT retornado pelo DTO
            localStorage.setItem('token', data.token);
            localStorage.setItem('id', data.idFuncionario);

            showSuccess();
            setTimeout(() => {
                window.location.href = '../emprestimos/index.html';
            }, 700);
        })
        .catch(error => {
            console.error('Login falhou:', error);
            
            // Trata erros de CORS ou servidor fora do ar sem estourar o erro genérico de conexão na tela de forma limpa
            if (error.message.includes('Failed to fetch') || error.message.includes('fetch')) {
                showErrors(['Erro de conexão. Verifique se o servidor backend está rodando e aceitando requisições (CORS).']);
            } else {
                showErrors([error.message]);
            }
        })
        .finally(() => {
            if (submitBtn) submitBtn.disabled = false;
        });
    });
});