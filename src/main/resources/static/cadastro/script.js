const API_BASE_URL = "http://localhost:8080"; // Ajuste para a porta do seu Spring Boot

document.addEventListener('DOMContentLoaded', () => {
    inicializarGerenciadorAbas();
    inicializarFormularios();
    inicializarMascaras();
});

/**
 * Controla a alternância visual das abas de Perfil (Funcionário, Aluno, Professor)
 */
function inicializarGerenciadorAbas() {
    const botoesAcao = document.querySelectorAll('.action-button');
    const cardsAcao = document.querySelectorAll('.action-card');

    botoesAcao.forEach(botao => {
        botao.addEventListener('click', () => {
            const acaoAlvo = botao.dataset.action;

            // Remove o estado ativo de todos os botões e atribui ao clicado
            botoesAcao.forEach(btn => btn.classList.remove('active'));
            botao.classList.add('active');

            // Exibe apenas o card correspondente ao perfil selecionado
            cardsAcao.forEach(card => {
                if (card.dataset.action === acaoAlvo) {
                    card.classList.add('active');
                } else {
                    card.classList.remove('active');
                }
            });
        });
    });
}

/**
 * Captura todos os formulários e intercepta os envios para a API
 */
function inicializarFormularios() {
    const formularios = document.querySelectorAll('.component-form');

    formularios.forEach(form => {
        form.addEventListener('submit', (event) => {
            event.preventDefault();
            
            const endpoint = form.dataset.endpoint;
            const data = new FormData(form);
            const payload = Object.fromEntries(data.entries());

            // Envia a requisição dinamicamente para /usuarios, /alunos ou /professores
            alert(`${API_BASE_URL}/${endpoint}`)
            fetch(`${API_BASE_URL}/${endpoint}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(payload)
            })
            .then(async response => {
                if (response.ok) {
                    alert('✓ Cadastro realizado com sucesso!');
                    form.reset();
                } else {
                    // Captura a mensagem de erro vinda das validações do Java (ex: Email/CPF duplicado)
                    const erroTxt = await response.text();
                    alert(`Erro ao cadastrar: ${erroTxt || 'Verifique as informações.'}`);
                }
            })
            .catch(error => {
                console.error(error);
                alert('Erro de conexão. Verifique se o servidor backend está rodando.');
            });
        });
    });
}

/**
 * Adiciona a máscara de formatação automática para inputs de CPF
 */
function inicializarMascaras() {
    const cpfs = document.querySelectorAll('.mask-cpf');
    cpfs.forEach(input => {
        input.addEventListener('input', (e) => {
            let value = e.target.value.replace(/\D/g, "");
            value = value.replace(/(\d{3})(\d)/, "$1.$2");
            value = value.replace(/(\d{3})(\d)/, "$1.$2");
            value = value.replace(/(\d{3})(\d{1,2})$/, "$1-$2");
            e.target.value = value;
        });
    });
}

const inputCpf = document.getElementById('cpf');
if (inputCpf) {
    inputCpf.addEventListener('input', (e) => {
        let value = e.target.value;
        
        // Remove tudo o que não for número
        value = value.replace(/\D/g, "");
        
        // Aplica a máscara dinamicamente
        value = value.replace(/(\d{3})(\d)/, "$1.$2");
        value = value.replace(/(\d{3})(\d)/, "$1.$2");
        value = value.replace(/(\d{3})(\d{1,2})$/, "$1-$2");
        
        e.target.value = value;
    });
}