# Trilha Segura - MVP

O objetivo final do aplicativo Trilha Segura será se apresentar como forma de proporcionar mais conforto e segurança aos seus usuários, tendo como principal ponto positivo, os sistemas apoio e de pins. Seu sistema de apoio possui dois diferentes alarmes que podem ser acionados pelo ciclista, um de apoio médico, e outro de apoio mecânico. Já para o sistema de pins, imaginou-se proporcionar a mesma experiência que um motorista atrasado tem, ao acessar o Waze e decidir, a partir daí, qual será a melhor rota a se seguir zeu caminho.

## Funcionalidades

O objetivo deste MVP (Minimum Viable Product) é fornecer funcionalidades básicas para testar e validar o conceito do aplicativo. Possuindo assim as seguintes funcionalidades:

1. **Set Pin:** Ao clicar no botão "Set Pin", o usuário pode adicionar um marcador (PIN) em uma posição específica do mapa (a que ele está no momento, como pode ser vista na Imagem 1). Isso permite que os usuários marquem locais de interesse ou pontos de referência relevantes para futuras referências. Neste MVP o aplicativo possui um conjunto inicial de eventos que podem ser sinalizados ao longo da trilha (Imagem 2). Sendo as seguintes opções: Pista Escorregadia, Buraco, Mata-Burro e Animal na Trilha. Esses eventos servem para demonstrar a capacidade do aplicativo de sinalizar pontos de interesse e possíveis obstáculos ao longo do caminho, demonstrando. Ao finalizar o PIN estará disponível no mapa como é possível ver na Imagem 3.

<p align="center">
  <img src="https://github.com/JoaoMEspindola/MDS_Trilha_Segura/assets/49202702/a0400c27-bb52-4187-b3c3-95801b151174" alt="Tela Inicial" height="450px">
</p>

<p align="center">
  <img src="https://github.com/JoaoMEspindola/MDS_Trilha_Segura/assets/49202702/e2d5b231-5ace-4f2f-874b-08e91a7a37f7" alt="Tela de Setar PIN" height="450px">
</p>

<p align="center">
  <img src="https://github-production-user-asset-6210df.s3.amazonaws.com/49202702/244843313-ff0ba8a9-a74f-4066-8d63-27e05e5f2425.jpg" alt="Tela Inicial com PIN setado" height="450px">
</p>

2. **Start Tracking:** Ao clicar no botão "Start Tracking", o aplicativo registra o caminho percorrido pelo usuário em tempo real (Imagem 4). Isso permite que os usuários gravem suas trilhas, importante para que revisitem-nas posteriormente ou as compartilhem com amigos. Como é possível ver pela imagem, ao iniciar o tracking, um pequeno botão de stop aparecerá no canto direito inferior da tela permitindo que termine de se marcar a rota atual.

<p align="center">
  <img src="https://github.com/JoaoMEspindola/MDS_Trilha_Segura/assets/49202702/fbfe6d18-7b6b-406d-aa88-038a96da903c" alt="Tela com Rota sendo traçada" height="450px">
</p>

## Tecnologias Utilizadas

O Trilha Segura foi desenvolvido utilizando as seguintes tecnologias e ferramentas:

- Android Studio: Ambiente de desenvolvimento integrado (IDE) utilizado para criar o aplicativo Android.
- API do Google Maps: Utilizada para exibir e interagir com os mapas no aplicativo. Para utilizar a API, foi necessário o cadastro de uma chave de API no Console de Desenvolvedor do Google.

## Instalação e Execução

1. Clone este repositório em sua máquina local.

2. Abra o projeto no Android Studio.

3. Compile e execute o aplicativo em um dispositivo Android ou em um emulador.

## Próximos Passos

Este MVP do Trilha Segura fornece uma base para futuras iterações e melhorias. Alguns próximos passos para a evolução do aplicativo podem incluir:

- Integrar o aplicativo com um banco de dados para que todos possam ver as rotas e PINs dos outros.
- Expandir o conjunto de PINs.
- Adicionar recursos de pesquisa e navegação em trilhas existentes.
- Permitir a exclusão de PINs existentes que podem ser temporários.

## Funcionamento do Aplicativo

<p>No link abaixo, será possível ver um breve funcionamento das funções anteriormente listadas. Gravação feita diretamente do emulador do AndroidStudio.</p>
<a href="https://youtu.be/QQBdJ5aXxvM">Vídeo do funcionamento.</a>
