function q(id){return document.getElementById(id)}

// Tabs
q('btn-login').addEventListener('click', ()=>{q('btn-login').classList.add('active');q('btn-register').classList.remove('active');q('login-form').classList.remove('hidden');q('register-form').classList.add('hidden')})
q('btn-register').addEventListener('click', ()=>{q('btn-register').classList.add('active');q('btn-login').classList.remove('active');q('register-form').classList.remove('hidden');q('login-form').classList.add('hidden')})

// Helpers
function showMsg(id, text){q(id).textContent = text}

// Register
q('reg-submit').addEventListener('click', async ()=>{
  showMsg('reg-msg','')
  const nome = q('reg-nome').value.trim()
  const email = q('reg-email').value.trim()
  const cpf = q('reg-cpf').value.trim()
  const senha = q('reg-senha').value

  const cpfRegex = /\d{3}\.\d{3}\.\d{3}-\d{2}/
  if(!cpfRegex.test(cpf)) return showMsg('reg-msg','CPF inválido. Use XXX.XXX.XXX-XX')
  if(!nome||!email||!senha) return showMsg('reg-msg','Preencha todos os campos')

  const payload = { nome, email, cpf, senha, tipoUsuario: 'USUARIO' }

  try{
    const res = await fetch('/usuario', { method:'POST', headers:{'Content-Type':'application/json'}, body:JSON.stringify(payload) })
    if(res.ok){
      showMsg('reg-msg','Cadastro realizado. Faça login.')
      q('btn-login').click()
    } else {
      const text = await res.text()
      showMsg('reg-msg', 'Erro: '+ (text || res.status))
    }
  }catch(e){ showMsg('reg-msg','Erro de rede') }
})

// Login
q('login-submit').addEventListener('click', async ()=>{
  showMsg('login-msg','')
  const email = q('login-email').value.trim()
  const senha = q('login-senha').value
  if(!email||!senha) return showMsg('login-msg','Preencha email e senha')

  const payload = { email, senha }

  try{
    const res = await fetch('/login', { method:'POST', headers:{'Content-Type':'application/json'}, body:JSON.stringify(payload) })
    if(res.ok){
      // Se backend retornar JSON com token, salvar e redirecionar
      try{ const data = await res.json(); if(data.token) localStorage.setItem('token', data.token) }catch(e){}
      window.location.href = '/componentes/index.html'
    } else {
      const txt = await res.text()
      showMsg('login-msg', 'Falha: '+(txt || res.status))
    }
  }catch(e){ showMsg('login-msg','Erro de rede') }
})
