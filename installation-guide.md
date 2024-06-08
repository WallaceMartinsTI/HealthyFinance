# Installation Guide (EN - US)

To install and use the application, follow the steps in this guide.

## Requirements

- Android Studio
- Firebase

## Initial Steps

Download the .zip file or clone the repository.

## Firebase Configuration

You need to create your own "app" in Firebase.

Click here and click "Get started." After logging in with your Google account, click "Add project," give your project a name in Firebase, and click "Continue." The use of Google Analytics is optional; you can uncheck it if you want, and then click "Create project." Once the project is created, click "Continue."

You've created the project in Firebase; now let's configure it for the Android app.

Below the "Get started by adding Firebase to your app" section, you'll see options for iOS, Android, and Web. Click the Android icon, enter the package name, for example, "com.company.appname," and click "Register app." This will generate the "google-services.json" file, which you should place inside the 'app' folder of the project. After downloading and placing the file in the app folder, click "Next." You can skip part 3 since this configuration is already set up in 'build.gradle.' Click "Next," and finally click "Continue to console."

Finally, just add Auth and Firestore to complete the setup.

In the side menu, click on Authentication and then "Get started." Under 'Sign-in providers,' click "Email/Password," and ensure that only "Email/Password" is checked, then click "Save." The authentication setup is now complete.

For Firestore, in the side menu, click on "Firestore Database" and then "Create database." In 'Location,' select the region closest to you and click "Next." Choose the best option for you, either "production mode" or "test mode." Let's go with "Start in production mode" and click "Create." To finish, after the Firestore Database is created, click on the "Rules" tab and change the line <code>allow read, write: if false;</code> to <code>allow read, write: if request.auth != null;</code> Done.

Now, if necessary, sync the build.gradle and start the app.

# Guia de Instalação (PT - BR)

Para conseguir instalar e utilizar o aplicativo, siga os passos desse guia.

## Requisitos

- Android Studio;
- Firebase;

## Passos Iniciais

Baixe o arquivo .zip ou clone o repositório.

## Configuração Firebase

Você deve criar seu próprio "aplicativo" no Firebase.

[Clique aqui](https://firebase.google.com/?authuser=1&hl=pt-br) e clique em "Começar", após fazer Login com sua conta do Google, clique em "Adicionar projeto", dê um nome para seu projeto no Firebase e clique em "Continuar", é opcional o uso do Google Analytics, você pode desmarcar se quiser, e então clique em "Criar projeto", Após o projeto ser criado, clique em "Continuar".

Você criou o projeto no Firebase, agora vamos configurar para o App Android.

Abaixo de onde está escrito "Comece adicionando o Firebase ao seu aplicativo" você verá opções para iOs, Android e Web. Clique no icone do Android, coloque o nome do pacote, exemplo: "com.company.appname" e clique em "Registrar app", assim será gerador o arquivo "google-services.json" você deve coloca-lo dentro da pasta 'app' do projeto. Após baixar e colocar o arquivo na pasta app, clique em "Próxima", você pode pular a parte 3, pois esta configuração já esta pronta no 'build.gradle', clique em "Próxima" e por fim clique em "Continua no console".

Por fim, basta adicionar o Auth e o Firestore para finalizar.

No menu lateral, clique em criação, depois em Authentication e em "Vamos começar". Em 'provedores nativos' clique em "E-mail/senha" e deixe marcado apenas a opção de "E-mail/senhas" e clique em "Salvar", pronto o auth está finalizado.

Agora para o Firestore, no menu lateral clique em "Firestore Database" e em "Criar banco de dados" em 'Local', selecione a região mais próxima a você e clique em "Próxima", depois selecione a melhor opção para você "modo produção" ou "modo teste", vamos por "Iniciar no modo produção" e clique em "Criar", para finalizar, após o Firestore Database ser criado clique na aba "Regras" e altere a linha <code>allow read, write: if false;</code> para <code>allow read, write: if request.auth != null</code>, pronto.

Agora se necessário sincronize o build gradle e inicie o app.
