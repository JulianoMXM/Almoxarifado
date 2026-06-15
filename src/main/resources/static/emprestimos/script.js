const actionButtons = document.querySelectorAll('.action-button');
const actionCards = document.querySelectorAll('.action-card');
const API_BASE_URL = 'http://localhost:8080';

// Configurações iniciais travadas para o escopo de chaves
let selectedAction = 'consultar';
let selectedObject = 'chaves'; 

// Controla a troca visual das abas (Cadastrar, Consultar, Atualizar, Deletar)
function activateAction(actionId) {
    selectedAction = actionId;
    actionButtons.forEach(button => {
        button.classList.toggle('active', button.dataset.action === actionId);
    });
    actionCards.forEach(card => {
        card.classList.toggle('active', card.dataset.action === actionId);
    });
}

// 1. AÇÃO DE CONSULTAR CHAVE (GET)
async function consultarChave(event) {
    event.preventDefault(); // Impede o refresh da página
    const form = event.target;
    const objeto = form.dataset.object;
    const resultList = document.getElementById('consultaResultado');
    const messageBox = document.getElementById('consultaMensagem');

    let url = `${API_BASE_URL}/chaves`;
    let params = new URLSearchParams();

    // Se houver um input de busca por texto (sala), adiciona como parâmetro de Query
    const inputSala = form.querySelector('input[name="sala"]');
    if (inputSala && inputSala.value) {
        params.set('sala', inputSala.value);
    }

    if (params.toString()) {
        url += `?${params.toString()}`;
    }

    resultList.innerHTML = '';
    messageBox.textContent = 'Carregando...';

    try {
        const resposta = await fetch(url);
        if (!resposta.ok) {
            throw new Error(`Erro ${resposta.status}`);
        }
        const dados = await resposta.json();
        renderConsultaResultado(dados, objeto);
        messageBox.textContent = 'Resultados carregados.';
    } catch (error) {
        messageBox.textContent = `Falha ao consultar: ${error.message}`;
    }
}

// 2. RENDERIZAR OS RESULTADOS EM UMA TABELA LINDA
function renderConsultaResultado(dados, objeto) {
    const resultList = document.getElementById('consultaResultado');
    resultList.innerHTML = '';

    if (!dados || (Array.isArray(dados) && dados.length === 0)) {
        resultList.innerHTML = '<p>Nenhuma chave encontrada.</p>';
        return;
    }

    // Se o Java devolver um objeto único (busca por ID), envelopa em um Array
    if (!Array.isArray(dados)) {
        dados = [dados];
    }
    
    const tabela = document.createElement('table');
    tabela.className = 'tabela-resultados';

    // Monta o cabeçalho focado em Chaves
    tabela.innerHTML = `
        <thead>
            <tr>
                <th>ID</th>
                <th>Sala / Localização</th>
                <th>Status Atual</th>
            </tr>
        </thead>
    `;

    const tbody = document.createElement('tbody');

    // Preenche as linhas com os dados vindos do banco PostgreSQL
    dados.forEach(item => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${item.id}</td>
            <td><strong>${item.sala || '-'}</strong></td>
            <td><span class="status-tag">${item.status || '-'}</span></td>
        `;
        tbody.appendChild(tr);
    });

    tabela.appendChild(tbody);
    resultList.appendChild(tabela);
}

// 3. AÇÃO DE CADASTRAR CHAVE (POST)
function cadastrarObjeto(event) {
    event.preventDefault();
    const form = event.target;
    const objeto = form.dataset.object;
    const data = new FormData(form);
    let body = {};
    
    data.forEach((value, key) => {
        if (!value) return;
        body[key] = value;
    });

    const url = `${API_BASE_URL}/${objeto}`;

    fetch(url, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(body)
    })
    .then(res => {
        if (!res.ok) throw new Error(`Erro ${res.status}`);
        return res.json();
    })
    .then(() => {
        alert(`Chave cadastrada com sucesso.`);
        form.reset(); // Limpa os campos do formulário
    })
    .catch(error => alert(`Falha ao cadastrar: ${error.message}`));
}

// 4. AÇÃO DE ATUALIZAR STATUS DA CHAVE (PATCH / PUT)
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
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(body)
    })
    .then(res => {
        if (!res.ok) throw new Error(`Erro ${res.status}`);
        return res.json();
    })
    .then(() => {
        alert(`Chave atualizada com sucesso.`);
        form.reset();
    })
    .catch(error => alert(`Falha ao atualizar: ${error.message}`));
}

// 5. AÇÃO DE DELETAR CHAVE (DELETE)
function deletarObjeto(event) {
    event.preventDefault();
    const form = event.target;
    const id = form.querySelector('input[name="id"]').value;
    
    if (!id) {
        return alert('Informe o ID para deletar.');
    }

    const url = `${API_BASE_URL}/${selectedObject}/${id}`;

    fetch(url, { method: 'DELETE' })
    .then(res => {
        if (!res.ok) throw new Error(`Erro ${res.status}`);
        return res.text();
    })
    .then(() => {
        alert(`Chave deletada com sucesso.`);
        form.reset();
    })
    .catch(error => alert(`Falha ao deletar: ${error.message}`));
}

// Mapeia os cliques nos botões de ação do menu lateral
actionButtons.forEach(button => {
    button.addEventListener('click', () => activateAction(button.dataset.action));
});

// Inicializa os escutadores assim que a página HTML carregar por completo
window.addEventListener('DOMContentLoaded', () => {
    activateAction(selectedAction);
    
    document.querySelectorAll('.consult-form').forEach(form => {
        form.addEventListener('submit', consultarChave); // Vinculado corretamente aqui
    });
    document.querySelectorAll('.component-form').forEach(form => {
        form.addEventListener('submit', cadastrarObjeto);
    });
    document.querySelectorAll('.update-form').forEach(form => {
        form.addEventListener('submit', atualizarObjeto);
    });
    document.querySelectorAll('.delete-form').forEach(form => {
        form.addEventListener('submit', deletarObjeto);
    });
});

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

const inputsDate = document.querySelectorAll('.Date'); 

inputsDate.forEach(input => {
    input.addEventListener('input', (e) => {
        let value = e.target.value;

        value = value.replace(/\D/g, "");

        value = value.replace(/(\d{2})(\d)/, "$1/$2");
        value = value.replace(/(\d{2})(\d)/, "$1/$2");

        e.target.value = value;
    });
});