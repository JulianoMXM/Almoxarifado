const objectButtons = document.querySelectorAll('.object-button');
const actionButtons = document.querySelectorAll('.action-button');
const actionCards = document.querySelectorAll('.action-card');
const API_BASE_URL = 'http://localhost:8080';

let selectedObject = 'componente';
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

function toggleActionButtons() {
    actionButtons.forEach(button => {
        const action = button.dataset.action;
        if (selectedObject === 'componente') {
            button.disabled = action === 'cadastrar' || action === 'atualizar';
            button.style.display = action === 'cadastrar' || action === 'atualizar' ? 'none' : '';
        } else {
            button.disabled = action === 'deletar';
            button.style.display = action === 'deletar' ? 'none' : '';
        }
    });
    if (selectedObject === 'componente' && selectedAction === 'cadastrar') {
        activateAction('consultar');
    }
    if (selectedObject === 'componente' && selectedAction === 'atualizar') {
        activateAction('consultar');
    }
}

async function consultarObjeto(event) {
    event.preventDefault();
    const form = event.target;
    const objeto = form.dataset.object;
    const resultList = document.getElementById('consultaResultado');
    const messageBox = document.getElementById('consultaMensagem');

    let url = `${API_BASE_URL}`;
    let params = new URLSearchParams();

    switch (objeto) {
        case 'componente':
            const componenteId = form.querySelector('input[name="id"]').value;
            url += componenteId ? `/componente/${componenteId}` : '/componente';
            break;
        case 'capacitor':
            url += '/capacitor';
            if (form.min.value) params.set('min', form.min.value);
            if (form.max.value) params.set('max', form.max.value);
            if (form.unidade.value) params.set('unidade', form.unidade.value);
            break;
        case 'resistor':
            url += '/resistor';
            if (form.min.value) params.set('min', form.min.value);
            if (form.max.value) params.set('max', form.max.value);
            if (form.unidade.value) params.set('unidade', form.unidade.value);
            break;
        case 'diodo':
            url += '/diodo';
            if (form.tipo.value) params.set('tipo', form.tipo.value);
            break;
        case 'indutor':
            url += '/indutor';
            if (form.min.value) params.set('min', form.min.value);
            if (form.max.value) params.set('max', form.max.value);
            if (form.unidade.value) params.set('unidade', form.unidade.value);
            break;
        case 'protoboard':
            url += '/protoboard';
            break;
        default:
            return;
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

function renderConsultaResultado(dados, objeto) {
    const resultList = document.getElementById('consultaResultado');
    resultList.innerHTML = '';

    if (!dados || (Array.isArray(dados) && dados.length === 0)) {
        resultList.innerHTML = '<p>Nenhum registro encontrado.</p>';
        return;
    }

    if (!Array.isArray(dados)) {
        dados = [dados];
    }

    // 1. Cria o elemento de tabela e adiciona uma classe para você estilizar no CSS se quiser
    const tabela = document.createElement('table');
    tabela.className = 'tabela-resultados';

    // 2. Monta o cabeçalho (títulos das colunas) baseado no tipo do objeto consultado
    let cabecalhoHTML = `
        <thead>
            <tr>
                <th>ID</th>
                <th>Modelo</th>
                <th>Descrição</th>
                <th>Qtd</th>
    `;

    // Adiciona colunas extras específicas para cada tipo de componente
    if (objeto === 'capacitor') {
        cabecalhoHTML += `
            <th>Capacitância</th>
            <th>Tensão Máx.</th>
            <th>Tolerância</th>
        `;
    } else if (objeto === 'resistor') {
        cabecalhoHTML += `
            <th>Resistência</th>
            <th>Potência Máx.</th>
            <th>Tolerância</th>
        `;
    } else if (objeto === 'diodo') {
        cabecalhoHTML += `
            <th>Tipo</th>
            <th>Queda de Tensão</th>
            <th>Corrente Máx.</th>
        `;
    } else if (objeto === 'indutor') {
        cabecalhoHTML += `
            <th>Indutância</th>
            <th>Corrente Máx.</th>
        `;
    }

    cabecalhoHTML += `</tr></thead>`;
    tabela.innerHTML = cabecalhoHTML;

    // 3. Monta o corpo da tabela (as linhas de dados)
    const tbody = document.createElement('tbody');

    dados.forEach(item => {
        const tr = document.createElement('tr');
        
        // Dados básicos comuns a todos os componentes (da tabela mãe)
        let linhaHTML = `
            <td>${item.id}</td>
            <td>${item.modelo || '-'}</td>
            <td>${item.descricao || '-'}</td>
            <td>${item.qntDisponivel ?? '-'}</td>
        `;

        // Preenche as células específicas mapeando as propriedades do JSON do Java
        if (objeto === 'capacitor') {
            linhaHTML += `
                <td>${item.capacitancia} ${item.unidadeDeMedida}F</td>
                <td>${item.tensaoMaxima}V</td>
                <td>${item.tolerancia}%</td>
            `;
        } else if (objeto === 'resistor') {
            linhaHTML += `
                <td>${item.resistencia} ${item.unidadeDeMedida}Ω</td>
                <td>${item.potenciaMaxima}W</td>
                <td>${item.tolerancia}%</td>
            `;
        } else if (objeto === 'diodo') {
            linhaHTML += `
                <td>${item.tipoDiodo}</td>
                <td>${item.quedaDeTensao}V</td>
                <td>${item.correnteDiretaMaxima}A</td>
            `;
        } else if (objeto === 'indutor') {
            linhaHTML += `
                <td>${item.indutancia} ${item.unidadeDeMedida}H</td>
                <td>${item.correnteMaxima}A</td>
            `;
        }

        tr.innerHTML = linhaHTML;
        tbody.appendChild(tr);
    });

    tabela.appendChild(tbody);
    resultList.appendChild(tabela);
}

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
    .then(() => alert(`${capitalize(objeto)} cadastrado com sucesso.`))
    .catch(error => alert(`Falha ao cadastrar: ${error.message}`));
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
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(body)
    })
    .then(res => {
        if (!res.ok) throw new Error(`Erro ${res.status}`);
        return res.json();
    })
    .then(() => alert(`${capitalize(objeto)} atualizado com sucesso.`))
    .catch(error => alert(`Falha ao atualizar: ${error.message}`));
}

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
    .then(() => alert(`${capitalize(selectedObject)} deletado com sucesso.`))
    .catch(error => alert(`Falha ao deletar: ${error.message}`));
}

function capitalize(value) {
    return value.charAt(0).toUpperCase() + value.slice(1);
}

objectButtons.forEach(button => {
    button.addEventListener('click', () => activateObject(button.dataset.object));
});

actionButtons.forEach(button => {
    button.addEventListener('click', () => activateAction(button.dataset.action));
});

window.addEventListener('DOMContentLoaded', () => {
    activateObject(selectedObject);
    activateAction(selectedAction);
    document.querySelectorAll('.consult-form').forEach(form => {
        form.addEventListener('submit', consultarObjeto);
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

