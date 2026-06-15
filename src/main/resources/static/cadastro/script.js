/**
 * Sistema de Cadastro de Usuários
 * Controla a lógica de seleção de perfil (Usuário, Aluno, Professor)
 * e gerencia os campos do formulário dinamicamente
 */

document.addEventListener('DOMContentLoaded', function() {
    const profileButtons = document.querySelectorAll('.object-button[data-profile]');
    const profileFields = document.querySelectorAll('.profile-field');
    const formTitle = document.getElementById('formTitle');
    const cadastroForm = document.getElementById('cadastroUsuarioForm');
    
    let currentProfile = 'usuario'; // Perfil padrão

    /**
     * Mapa de configurações por perfil
     */
    const profileConfig = {
        usuario: {
            title: 'Cadastrar Usuário Comum',
            requiredFields: ['nome', 'email', 'cpf', 'senha'],
            description: 'Cadastre um usuário comum com CPF'
        },
        aluno: {
            title: 'Cadastrar Aluno',
            requiredFields: ['nome', 'email', 'cpf', 'ra', 'senha'],
            description: 'Cadastre um aluno com CPF e RA (Registro Acadêmico)'
        },
        professor: {
            title: 'Cadastrar Professor',
            requiredFields: ['nome', 'email', 'cpf', 'siape', 'senha'],
            description: 'Cadastre um professor com CPF e SIAPE'
        }
    };

    /**
     * Alterna o perfil selecionado e atualiza os campos do formulário
     */
    profileButtons.forEach(button => {
        button.addEventListener('click', function() {
            const profile = this.getAttribute('data-profile');
            selectProfile(profile);
        });
    });

    /**
     * Função para selecionar um perfil
     */
    function selectProfile(profile) {
        // Atualiza o perfil atual
        currentProfile = profile;

        // Remove a classe 'active' de todos os botões
        profileButtons.forEach(btn => btn.classList.remove('active'));

        // Adiciona 'active' ao botão clicado
        document.querySelector(`.object-button[data-profile="${profile}"]`).classList.add('active');

        // Atualiza o título do formulário
        if (formTitle) {
            formTitle.textContent = profileConfig[profile].title;
        }

        // Mostra/oculta os campos de acordo com o perfil
        updateFormFields(profile);

        // Limpa as mensagens de erro
        clearFormErrors();
    }

    /**
     * Atualiza os campos do formulário baseado no perfil
     */
    function updateFormFields(profile) {
        profileFields.forEach(field => {
            const fieldProfile = field.getAttribute('data-profile');
            
            if (fieldProfile === profile) {
                field.style.display = 'grid';
                // Adiciona animação de fade-in
                field.style.animation = 'none';
                setTimeout(() => {
                    field.style.animation = 'fadeInField 0.2s ease-in-out';
                }, 10);
            } else {
                field.style.display = 'none';
            }
        });

        // Atualiza os atributos 'required' dos inputs
        updateRequiredFields(profile);
    }

    /**
     * Atualiza os campos obrigatórios baseado no perfil
     */
    function updateRequiredFields(profile) {
        const allInputs = cadastroForm.querySelectorAll('input, textarea, select');
        const requiredFields = profileConfig[profile].requiredFields;

        allInputs.forEach(input => {
            if (requiredFields.includes(input.id)) {
                input.setAttribute('required', 'required');
            } else {
                input.removeAttribute('required');
            }
        });
    }

    /**
     * Valida o formulário antes de enviar
     */
    function validateForm(data) {
        const errors = [];

        // Validação de nome
        if (!data.nome || data.nome.trim().length < 3) {
            errors.push('Nome deve ter pelo menos 3 caracteres');
        }

        // Validação de email
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!data.email || !emailRegex.test(data.email)) {
            errors.push('E-mail inválido');
        }

        // Validação de CPF
        if (!data.cpf || !validateCPF(data.cpf)) {
            errors.push('CPF inválido');
        }

        // Validação de RA (se for aluno)
        if (currentProfile === 'aluno' && (!data.ra || data.ra.trim().length === 0)) {
            errors.push('RA (Registro Acadêmico) é obrigatório para alunos');
        }

        // Validação de SIAPE (se for professor)
        if (currentProfile === 'professor' && (!data.siape || data.siape.trim().length === 0)) {
            errors.push('SIAPE é obrigatório para professores');
        }

        // Validação de senha
        if (!data.senha || data.senha.length < 6) {
            errors.push('Senha deve ter pelo menos 6 caracteres');
        }

        return errors;
    }

    /**
     * Valida CPF (formato básico)
     */
    function validateCPF(cpf) {
        // Remove caracteres especiais
        const cleanCPF = cpf.replace(/\D/g, '');

        // Verifica se tem 11 dígitos
        if (cleanCPF.length !== 11) {
            return false;
        }

        // Verifica se não é uma sequência repetida (ex: 111.111.111-11)
        if (/^(\d)\1{10}$/.test(cleanCPF)) {
            return false;
        }

        return true;
    }

    /**
     * Limpa mensagens de erro do formulário
     */
    function clearFormErrors() {
        const errorDiv = document.getElementById('formErrors');
        if (errorDiv) {
            errorDiv.remove();
        }
    }

    /**
     * Exibe mensagens de erro
     */
    function showErrors(errors) {
        clearFormErrors();

        const errorDiv = document.createElement('div');
        errorDiv.id = 'formErrors';
        errorDiv.style.cssText = `
            background-color: #fee2e2;
            border: 1px solid #fecaca;
            border-radius: 12px;
            padding: 14px 16px;
            margin-bottom: 16px;
            color: #991b1b;
        `;

        const errorList = document.createElement('ul');
        errorList.style.cssText = `
            margin: 0;
            padding-left: 20px;
        `;

        errors.forEach(error => {
            const li = document.createElement('li');
            li.textContent = error;
            errorList.appendChild(li);
        });

        errorDiv.appendChild(errorList);
        cadastroForm.insertBefore(errorDiv, cadastroForm.firstChild);
    }

    /**
     * Exibe mensagem de sucesso
     */
    function showSuccess() {
        clearFormErrors();

        const successDiv = document.createElement('div');
        successDiv.id = 'formSuccess';
        successDiv.style.cssText = `
            background-color: #dcfce7;
            border: 1px solid #86efac;
            border-radius: 12px;
            padding: 14px 16px;
            margin-bottom: 16px;
            color: #166534;
            font-weight: 600;
        `;
        successDiv.textContent = '✓ Cadastro realizado com sucesso!';

        cadastroForm.insertBefore(successDiv, cadastroForm.firstChild);

        // Remove a mensagem após 3 segundos
        setTimeout(() => {
            successDiv.remove();
        }, 3000);
    }

    /**
     * Manipula o envio do formulário
     */
    cadastroForm.addEventListener('submit', async function(e) {
        e.preventDefault();

        // Coleta os dados do formulário
        const formData = new FormData(cadastroForm);
        const data = {
            nome: formData.get('nome'),
            email: formData.get('email'),
            cpf: formData.get('cpf'),
            ra: formData.get('ra') || null,
            siape: formData.get('siape') || null,
            senha: formData.get('senha'),
            tipo: currentProfile
        };

        // Valida os dados
        const errors = validateForm(data);
        if (errors.length > 0) {
            showErrors(errors);
            return;
        }

        try {
            // Envia os dados para o servidor
            const response = await fetch('/api/usuarios/cadastro', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            });

            if (response.ok) {
                showSuccess();
                cadastroForm.reset();
                // Reseta para o perfil padrão
                selectProfile('usuario');
            } else {
                const errorData = await response.json();
                showErrors([errorData.message || 'Erro ao cadastrar. Tente novamente.']);
            }
        } catch (error) {
            console.error('Erro na requisição:', error);
            showErrors(['Erro de conexão. Verifique sua internet e tente novamente.']);
        }
    });

    // Inicializa com o perfil padrão
    selectProfile('usuario');
});
