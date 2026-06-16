const actionButtons = document.querySelectorAll('.action-button');
const actionCards = document.querySelectorAll('.action-card');
const API_BASE_URL = 'http://localhost:8080';
const token = localStorage.getItem('token')

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

async function consultarEmprestimos(event) {
    event.preventDefault(); // Impede o refresh da página
    const form = event.target;
    const resultList = document.getElementById('consultaResultado');
    const messageBox = document.getElementById('consultaMensagem');

    // Aponta para o endpoint correto de empréstimos no seu Spring Boot
    let url = `${API_BASE_URL}/emprestimo`;
    const params = new URLSearchParams();

    const inputId = form.querySelector('input[name="id"]');
    const selectStatus = form.querySelector('select[name="status"]');

    if (selectStatus && selectStatus.value) {
        params.set('status', selectStatus.value);
    }

    if (inputId && inputId.value) {
        url = `${API_BASE_URL}/emprestimo/${inputId.value.trim()}`;
    } else if (params.toString()) {
        url += `?${params.toString()}`;
    }

    resultList.innerHTML = '';
    messageBox.textContent = 'Carregando empréstimos...';

    try {
        const resposta = await fetch(url, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}` // Autenticação JWT injetada
            }
        });

        if (resposta.status === 403) {
            throw new Error("Sessão expirada. Por favor, faça login novamente.");
        }

        if (!resposta.ok) {
            throw new Error(`Erro ${resposta.status}`);
        }

        const dados = await resposta.json();
        
        // Renderiza a tabela de empréstimos enviando os dados recebidos
        renderConsultaEmprestimos(dados);
        messageBox.textContent = 'Empréstimos carregados com sucesso.';
    } catch (error) {
        messageBox.textContent = `Falha ao consultar empréstimos: ${error.message}`;
    }
}

function renderConsultaEmprestimos(dados) {
    const resultList = document.getElementById('consultaResultado');
    resultList.innerHTML = '';

    if (!dados || (Array.isArray(dados) && dados.length === 0)) {
        resultList.innerHTML = '<p class="sem-registros">Nenhum empréstimo registrado encontrado.</p>';
        return;
    }

    // Se o backend devolver apenas um objeto isolado, transforma em Array
    if (!Array.isArray(dados)) {
        dados = [dados];
    }

    const tabela = document.createElement('table');
    tabela.className = 'tabela-resultados tabela-emprestimos';

    // Cabeçalho estruturado com os dados da Entidade Java
    tabela.innerHTML = `
        <thead>
            <tr>
                <th>ID</th>
                <th>Item / Descrição</th>
                <th>Solicitante (CPF)</th>
                <th>Qtd</th>
                <th>Data Retirada</th>
                <th>Data Limite</th>
                <th>Data Devolução</th>
                <th>Responsável (Funcionário)</th>
                <th>Status</th>
            </tr>
        </thead>
    `;

    const tbody = document.createElement('tbody');

    dados.forEach(item => {
        const tr = document.createElement('tr');
        
        // Tratamento elegante das datas para não exibir 'null' se o item não foi devolvido ainda
        const dataDevolucaoFormatada = item.dataDevolucao ? formatarData(item.dataDevolucao) : '<span class="pendente">-</span>';
        
        // Lógica de classe CSS baseada no status para estilização futura
        const statusClass = item.status ? `status-${item.status.toLowerCase()}` : '';

        tr.innerHTML = `
            <td><strong>#${item.id}</strong></td>
            <td>${item.itemEmprestado ? item.itemEmprestado.descricao : 'Item não identificado'}</td>
            <td>
                <div>${item.solicitante ? item.solicitante.nome : 'N/D'}</div>
                <small style="color: #64748b;">${item.solicitante ? item.solicitante.cpf : '-'}</small>
            </td>
            <td>${item.quantidade ?? 1}</td>
            <td>${formatarData(item.dataRetirada)}</td>
            <td>${formatarData(item.dataLimite)}</td>
            <td>${dataDevolucaoFormatada}</td>
            <td>${item.funcionario ? item.funcionario.nome : 'Sistema'}</td>
            <td><span class="badge-status ${statusClass}">${item.status || 'Ativo'}</span></td>
        `;
        
        tbody.appendChild(tr);
    });

    tabela.appendChild(tbody);
    resultList.appendChild(tabela);
}

/**
 * Função utilitária para converter datas do formato Java (yyyy-mm-dd) para o formato brasileiro (dd/mm/yyyy)
 */
function formatarData(dataString) {
    if (!dataString) return '-';
    const [ano, mes, dia] = dataString.split('-');
    return `${dia}/${mes}/${ano}`;
}

// 3. AÇÃO DE CADASTRAR CHAVE (POST)
function cadastrarObjeto(event) {
    event.preventDefault();
    const form = event.target;

    const funcionarioIdLogado = localStorage.getItem('id');

    // 1. Validação de segurança: se não achar o ID do funcionário, impede o envio
    if (!funcionarioIdLogado || funcionarioIdLogado === "undefined") {
        alert("Erro: Você precisa estar logado como funcionário para realizar esta ação.");
        return;
    }

    const data = new FormData(form);
    let body = {};
    
    // Captura os dados do formulário uma única vez de forma limpa
    data.forEach((value, key) => {
        if (!value) return;
        body[key] = value;
    });

    // 2. Injeta as chaves numéricas com os nomes EXATOS esperados pelo DTO Java
    body['idFuncionario'] = Number(funcionarioIdLogado);
    
    if (body['idItem']) {
        body['idItem'] = Number(body['idItem']);
    }
    if (body['quantidade']) {
        body['quantidade'] = Number(body['quantidade']);
    }

    console.log("JSON que será enviado ao Java:", JSON.stringify(body));
    const url = `${API_BASE_URL}/emprestimo`;

    fetch(url, {
        method: 'POST',
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
    .then(() => {
        alert(`Empréstimo cadastrado com sucesso.`);
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

    if (body['dataDevolucao'] && body['dataDevolucao'].includes('/')) {
        const [dia, mes, ano] = body['dataDevolucao'].split('/');
        if (ano.length === 2) {
            body['dataDevolucao'] = `${dia}/${mes}/20${ano}`;
        }
    }

    if (Object.keys(body).length === 0) {
        return alert('Informe ao menos um campo para atualizar.');
    }

    const url = `${API_BASE_URL}/emprestimo/${id}`;

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
    .then(() => {
        alert(`Empréstimo atualizado com sucesso.`);
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

    const url = `${API_BASE_URL}/emprestimo/${id}`;

    fetch(url, { 
        method: 'DELETE', 
        headers: {
            'Authorization': `Bearer ${token}`
        }
    })
    .then(res => {
        if (!res.ok) throw new Error(`Erro ${res.status}`);
        return res.text();
    })
    .then(() => {
        alert(`Empréstimo deletado com sucesso.`);
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
        form.addEventListener('submit', consultarEmprestimos); 
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