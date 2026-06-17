const objectButtons = document.querySelectorAll('.object-button');
const actionButtons = document.querySelectorAll('.action-button');
const actionCards = document.querySelectorAll('.action-card');
const API_BASE_URL = 'http://localhost:8080';
const token = localStorage.getItem('token');

// Começa com Pessoa e Consultar ativados
let selectedObject = 'pessoa';
let selectedAction = 'consultar';

function activateObject(objectId) {
    selectedObject = objectId;
    objectButtons.forEach(button => {
        button.classList.toggle('active', button.dataset.object === objectId);
    });
    updatePanels();
}

function activateAction(actionId) {
    selectedAction = actionId;
    actionButtons.forEach(button => {
        button.classList.toggle('active', button.dataset.action === actionId);
    });
    actionCards.forEach(card => {
        card.classList.toggle('active', card.dataset.action === actionId);
    });
    updatePanels();
}

function updatePanels() {
    const activeCard = document.querySelector(`.action-card.active`);
    if (!activeCard) return;
    const objectPanels = activeCard.querySelectorAll('.object-panel');
    objectPanels.forEach(panel => {
        panel.classList.toggle('active', panel.dataset.object === selectedObject);
    });
    toggleActionButtons();
}

// Bloqueia e libera os botões com base no contexto (Herdado do seu exemplo Componentes)
function toggleActionButtons() {
    actionButtons.forEach(button => {
        const action = button.dataset.action;
        if (selectedObject === 'pessoa') {
            button.disabled = action === 'cadastrar' || action === 'atualizar';
            button.style.display = action === 'cadastrar' || action === 'atualizar' ? 'none' : '';
        } else {
            button.disabled = action === 'deletar' || action === 'consultar';;
            button.style.display = action === 'deletar' || action === 'consultar' ? 'none' : '';
        }
    });

    if (selectedObject === 'pessoa' && (selectedAction === 'cadastrar' || selectedAction === 'atualizar')) {
        activateAction('consultar');
    }
    if (selectedObject !== 'pessoa' && selectedAction === 'deletar') {
        activateAction('consultar');
    }
}

// =================== REQUISIÇÕES (CRUD) ===================

async function consultarObjeto(event) {
    event.preventDefault();
    const form = event.target;
    if (form.dataset.object !== 'pessoa') return;

    const tipoBusca = form.querySelector('select[name="tipoBusca"]').value;
    const valor = form.querySelector('input[name="valor"]').value;
    const resultList = document.getElementById('consultaResultado');
    const messageBox = document.getElementById('consultaMensagem');

    // Roteamento baseado no PessoaController.java
    let url = `${API_BASE_URL}/pessoa`;
    if (valor) {
        if (tipoBusca === 'id') url += `/${valor}`;
        if (tipoBusca === 'cpf') url += `/buscaCpf/${valor}`;
        if (tipoBusca === 'email') url += `/buscaEmail/${valor}`;
    }

    resultList.innerHTML = '';
    messageBox.textContent = 'Carregando...';

    try {
        const resposta = await fetch(url, {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        
        if (!resposta.ok) throw new Error(`Erro ${resposta.status}`);
        
        const dados = await resposta.json();
        renderConsultaResultado(dados);
        messageBox.textContent = 'Resultados carregados.';
    } catch (error) {
        messageBox.textContent = `Falha ao consultar: ${error.message}. Verifique o filtro digitado.`;
    }
}

async function cadastrarObjeto(event) {
    event.preventDefault();
    const form = event.target;
    const objeto = form.dataset.object; // "usuario", "discente" ou "docente"
    
    const formData = new FormData(form);
    const payload = Object.fromEntries(formData.entries());

    try {
        clearFormErrors(form);

        const resposta = await fetch(`${API_BASE_URL}/${objeto}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(payload)
        });

        if (!resposta.ok) {
            await handleBackendErrorResponse(resposta, form);
            return;
        }
        alert(`${capitalize(objeto)} cadastrado com sucesso!`);
        form.reset();
    } catch (error) {
        alert(`Falha ao cadastrar: ${error.message}`);
    }
}

function atualizarObjeto(event) {
    event.preventDefault();
    const form = event.target;
    const objeto = form.dataset.object;
    const id = form.querySelector('input[name="id"]').value;
    if (!id) {
        return alert('Informe o ID para atualizar.');
    }

    const data = new FormData(form);
    let body = {};
    data.forEach((value, key) => {
        if (!value || key === 'id') return;
        body[key] = value;
    });

    if (Object.keys(body).length === 0) {
        return alert('Informe ao menos um campo para atualizar.');
    }

    const url = `${API_BASE_URL}/${objeto}/${id}`;

    fetch(url, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify(body)
    })
    .then(res => {
        if (!res.ok) throw new Error(`Erro ${res.status}`);
        return res.json();
    })
    .then(() => alert(`${capitalize(objeto)} atualizado com sucesso.`))
    .catch(error => alert(`Falha ao atualizar: ${error.message}`));
}

async function deletarObjeto(event) {
    event.preventDefault();
    const form = event.target;
    if (form.dataset.object !== 'pessoa') return;

    const id = form.querySelector('input[name="id"]').value;
    const url = `${API_BASE_URL}/pessoa/${id}`;

    if (!confirm("Atenção! Deseja mesmo deletar permanentemente esta pessoa?")) return;

    try {
        const resposta = await fetch(url, {
            method: 'DELETE',
            headers: { 'Authorization': `Bearer ${token}` }
        });

        if (!resposta.ok) throw new Error(`Erro ${resposta.status}`);
        const msgTexto = await resposta.text();
        
        alert(`Sucesso: ${msgTexto}`);
        form.reset();
    } catch (error) {
        alert(`Falha ao deletar: ${error.message}`);
    }
}

// Renderização dos Resultados (Como no exemplo de Componentes)
function renderConsultaResultado(dados) {
    const resultList = document.getElementById('consultaResultado');
    resultList.innerHTML = '';

    if (!dados || (Array.isArray(dados) && dados.length === 0)) {
        resultList.innerHTML = '<p>Nenhum registro encontrado.</p>';
        return;
    }

    if (!Array.isArray(dados)) dados = [dados];

    const tabela = document.createElement('table');
    tabela.className = 'tabela-resultados';
    tabela.innerHTML = `
        <thead>
            <tr>
                <th>ID</th>
                <th>Nome</th>
                <th>E-mail</th>
                <th>CPF</th>
                <th>Perfil Técnico</th>
            </tr>
        </thead>
    `;

    const tbody = document.createElement('tbody');
    dados.forEach(item => {
        // Se a busca retornar dados aninhados (Pessoa > Usuario) tentamos buscar em ambas as raízes
        const nome = item.nome || item.pessoa?.nome || '-';
        const email = item.email || item.pessoa?.email || '-';
        const cpf = item.cpf || item.pessoa?.cpf || '-';
        
        // Verifica o tipo baseado nos atributos preenchidos
        let tipo = "Funcionário";
        if (item.ra) tipo = `Discente (RA: ${item.ra})`;
        if (item.siape) tipo = `Docente (SIAPE: ${item.siape})`;

        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${item.id || '-'}</td>
            <td><strong>${nome}</strong></td>
            <td>${email}</td>
            <td>${cpf}</td>
            <td><span class="tag-perfil">${tipo}</span></td>
        `;
        tbody.appendChild(tr);
    });

    tabela.appendChild(tbody);
    resultList.appendChild(tabela);
}

function capitalize(value) {
    return value.charAt(0).toUpperCase() + value.slice(1);
}

function clearFormErrors(form) {
    form.querySelectorAll('.field-error').forEach(errorElement => errorElement.remove());
    form.querySelectorAll('.error-input').forEach(input => input.classList.remove('error-input'));
}

async function handleBackendErrorResponse(resposta, form) {
    const status = resposta.status;
    const texto = await resposta.text();
    let mensagem = texto;

    try {
        const json = JSON.parse(texto);
        if (json.message) mensagem = json.message;
    } catch (ignored) {
        // corpo não é JSON
    }

    const field = findFieldFromMessage(form, mensagem);
    if (field) {
        highlightFieldError(field, mensagem);
    }

    alert(`Falha: ${mensagem}`);
}

function findFieldFromMessage(form, mensagem) {
    const keywords = ['CPF', 'Email', 'RA', 'SIAPE', 'nome', 'senha'];
    const lowerMessage = mensagem.toLowerCase();

    for (const key of keywords) {
        if (lowerMessage.includes(key.toLowerCase())) {
            return form.querySelector(`[name="${key.toLowerCase()}"]`) || form.querySelector(`[name="${key.toLowerCase()}" i]`);
        }
    }
    return null;
}

function highlightFieldError(field, mensagem) {
    field.classList.add('error-input');
    const errorElement = document.createElement('div');
    errorElement.className = 'field-error';
    errorElement.textContent = mensagem;
    errorElement.style.cssText = 'color:#b91c1c;margin-top:4px;font-size:0.9rem;';
    field.insertAdjacentElement('afterend', errorElement);
}

// Inicializadores
objectButtons.forEach(button => {
    button.addEventListener('click', () => activateObject(button.dataset.object));
});

actionButtons.forEach(button => {
    button.addEventListener('click', () => activateAction(button.dataset.action));
});

window.addEventListener('DOMContentLoaded', () => {
    activateObject(selectedObject);
    activateAction(selectedAction);
    
    document.querySelectorAll('.consult-form').forEach(form => form.addEventListener('submit', consultarObjeto));
    document.querySelectorAll('.component-form').forEach(form => form.addEventListener('submit', cadastrarObjeto));
    document.querySelectorAll('.update-form').forEach(form => form.addEventListener('submit', atualizarObjeto));
    document.querySelectorAll('.delete-form').forEach(form => form.addEventListener('submit', deletarObjeto));
});

const inputsCpf = document.querySelectorAll('.Cpf'); 

inputsCpf.forEach(cpfInput => {
    cpfInput.addEventListener('input', (e) => {
        let value = e.target.value;
            
            // Remove tudo o que não for número
            value = value.replace(/\D/g, "");
            
        // Aplica a máscara dinamicamente
        value = value.replace(/(\d{3})(\d)/, "$1.$2");
        value = value.replace(/(\d{3})(\d)/, "$1.$2");
        value = value.replace(/(\d{3})(\d{1,2})$/, "$1-$2");
        
        e.target.value = value;
    });
});