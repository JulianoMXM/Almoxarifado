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
            button.disabled = action === 'deletar';
            button.style.display = action === 'deletar' ? 'none' : '';
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
        const resposta = await fetch(`${API_BASE_URL}/${objeto}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(payload)
        });

        if (!resposta.ok) {
            const errTxt = await resposta.text();
            throw new Error(`Erro ${resposta.status}: ${errTxt}`);
        }
        alert(`${capitalize(objeto)} cadastrado com sucesso!`);
        form.reset();
    } catch (error) {
        alert(`Falha ao cadastrar: ${error.message}`);
    }
}

async function atualizarObjeto(event) {
    event.preventDefault();
    const form = event.target;
    const objeto = form.dataset.object; // "usuario", "discente" ou "docente"

    const id = form.querySelector('input[name="id"]').value;
    
    // Constrói dinamicamente o payload baseado nos inputs presentes no formulário
    let payload = {};
    if (objeto === 'usuario') {
        payload.senha = form.querySelector('input[name="senha"]').value;
    } else if (objeto === 'discente') {
        payload.ra = form.querySelector('input[name="ra"]').value;
    } else if (objeto === 'docente') {
        payload.siape = form.querySelector('input[name="siape"]').value;
    }

    const url = `${API_BASE_URL}/${objeto}/${id}`;
    
    try {
        const resposta = await fetch(url, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}` // Spring Security validará se quem chama é ADM
            },
            body: JSON.stringify(payload)
        });

        if (!resposta.ok) {
            if (resposta.status === 403) {
                throw new Error("Acesso negado. Seu usuário não possui permissão de ADMINISTRADOR.");
            }
            throw new Error(`Erro ${resposta.status}`);
        }
        
        // Mensagem de sucesso condicional e amigável
        let mensagemSucesso = "Dados atualizados com sucesso!";
        if (objeto === 'usuario') mensagemSucesso = "Senha do funcionário atualizada com sucesso!";
        if (objeto === 'discente') mensagemSucesso = "RA do discente atualizado com sucesso!";
        if (objeto === 'docente') mensagemSucesso = "SIAPE do docente atualizado com sucesso!";

        alert(mensagemSucesso);
        form.reset();
    } catch (error) {
        alert(`Falha ao atualizar: ${error.message}`);
    }
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