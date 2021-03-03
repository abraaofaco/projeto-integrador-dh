<h1 align="center">
 A5+
</h1>

<h3 align="center">
 Projeto Integrador TMDB: Santander Coders Mobile - By Digital House
</h3>

<p align="center">  
  <img alt="License" src="https://img.shields.io/badge/license-MIT-%2304D361">
</p>

## Regras

- Trabalhar cada parte da documentação como uma sprint
- Utilizar um fluxo de trabalho no Git

## Layout
<div align="center" style="">
  <img src="https://github.com/abraaofaco/projeto-integrador-dh/blob/master/screenshot/Screenshot_01.png?sanitize=true&raw=true" width="250px" />
  <img src="https://github.com/abraaofaco/projeto-integrador-dh/blob/master/screenshot/Screenshot_02.png?sanitize=true&raw=true" width="250px" />
  <img src="https://github.com/abraaofaco/projeto-integrador-dh/blob/master/screenshot/Screenshot_03.png?sanitize=true&raw=true" width="250px" />
  <img src="https://github.com/abraaofaco/projeto-integrador-dh/blob/master/screenshot/Screenshot_04.png?sanitize=true&raw=true" width="250px" />
  <img src="https://github.com/abraaofaco/projeto-integrador-dh/blob/master/screenshot/Screenshot_05.png?sanitize=true&raw=true" width="250px" />
  <img src="https://github.com/abraaofaco/projeto-integrador-dh/blob/master/screenshot/Screenshot_06.png?sanitize=true&raw=true" width="250px" />
  <img src="https://github.com/abraaofaco/projeto-integrador-dh/blob/master/screenshot/Screenshot_07.png?sanitize=true&raw=true" width="250px" />
  <img src="https://github.com/abraaofaco/projeto-integrador-dh/blob/master/screenshot/Screenshot_08.png?sanitize=true&raw=true" width="250px" />
  <img src="https://github.com/abraaofaco/projeto-integrador-dh/blob/master/screenshot/Screenshot_09.png?sanitize=true&raw=true" width="250px" />
  <img src="https://github.com/abraaofaco/projeto-integrador-dh/blob/master/screenshot/Screenshot_10.png?sanitize=true&raw=true" width="250px" />
  <img src="https://github.com/abraaofaco/projeto-integrador-dh/blob/master/screenshot/Screenshot_11.png?sanitize=true&raw=true" width="250px" />
  <img src="https://github.com/abraaofaco/projeto-integrador-dh/blob/master/screenshot/Screenshot_12.png?sanitize=true&raw=true" width="250px" />
</div>

## Bibliotecas e ferramentas usadas

- Kotlin
- AndroidX
- Material Components
- Android Architecture Components
  - Lifecycle and ViewModel
  - Dagger - Hilt
  - Navigation
- FirebaseAuth
- FirebaseMessaging
- FirebaseCoroutines
- Android Room
- Kotlin Coroutines
- Retrofit
- Glide
- Google Maps
- Google Place
- YouTube Player
- TMDB Api

## Configurações

- Adicione um novo projeto no [Firebase](https://console.firebase.google.com/u/0/?pli=1)
- Em *Firebase Authentication* habilitar as opções: Email/Senha, Google e Facebook
- Em *Configurações Firebase* registrar o certificado SHA
- Salvar aquivo *google-services.json* dentro da pasta *app*
- Criar projeto no [Facebook](https://developers.facebook.com/apps/)
- Gerar chave de acesso a api do [TMDB](https://developers.themoviedb.org/3/getting-started/introduction)
- Criar arquivo *gradle.properties* dentro da pasta *app*
- Em [Google Developers](https://console.developers.google.com/) habilitar: Maps SDK for Android, Place API e YouTube API
- Mudar plano do firebase para permitir integração com o Google cloud 

### Exemplo *gradle.properties*
```js
TMDB_API_KEY="SUA_CHAVE_TMDB"
FACEBOOK_APP_ID="CADASTRO_APP_FACEBBOK"
FB_LOGIN_PROTOCOL_SCHEME="CADASTRO_APP_FACEBBOK"
```

