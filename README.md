# Trilha Segura - MVP

O objetivo final do aplicativo Trilha Segura será se apresentar como forma de proporcionar mais conforto e segurança aos seus usuários, tendo como principal ponto positivo, os sistemas apoio e de pins. Seu sistema de apoio possui dois diferentes alarmes que podem ser acionados pelo ciclista, um de apoio médico, e outro de apoio mecânico. Já para o sistema de pins, imaginou-se proporcionar a mesma experiência que um motorista atrasado tem, ao acessar o Waze e decidir, a partir daí, qual será a melhor rota a se seguir.

## Funcionalidades

O objetivo deste MVP (Minimum Viable Product) é fornecer funcionalidades básicas para testar e validar o conceito do aplicativo. Possuindo assim as seguintes funcionalidades:

1. **Set Pin:** Ao clicar no botão "Set Pin", o usuário pode adicionar um marcador (PIN) em uma posição específica do mapa (a que ele está no momento, como pode ser vista na Imagem 1). Isso permite que os usuários marquem locais de interesse ou pontos de referência relevantes para futuras referências. Neste MVP o aplicativo possui um conjunto inicial de eventos que podem ser sinalizados ao longo da trilha (Imagem 2). Sendo as seguintes opções: Pista Escorregadia, Buraco, Mata-Burro e Animal na Trilha. Esses eventos servem para demonstrar a capacidade do aplicativo de sinalizar pontos de interesse e possíveis obstáculos ao longo do caminho, demonstrando. Ao finalizar o PIN estará disponível no mapa como é possível ver na Imagem 3.

![efc07c9a-8419-4887-89a1-96105deb4b77](https://github.com/JoaoMEspindola/MDS_Trilha_Segura/assets/49202702/2beeeea6-2091-4de2-8062-a6f39fa67c74)

![93659fa2-608a-4ef0-b417-b905bec624e5](https://github.com/JoaoMEspindola/MDS_Trilha_Segura/assets/49202702/9af57cba-0a4b-4d2e-ac37-835d232fb662)

![3ed77f0c-cff2-47e1-b933-2f250eb8d19a](https://github.com/JoaoMEspindola/MDS_Trilha_Segura/assets/49202702/233899b8-6c09-4af4-9013-ef5a278afe37)

2. **Start Tracking:** Ao clicar no botão "Start Tracking", o aplicativo registra o caminho percorrido pelo usuário em tempo real (Imagem 4). Isso permite que os usuários gravem suas trilhas, importante para que revisitem-nas posteriormente ou as compartilhem com amigos. Como é possível ver pela imagem, ao iniciar o tracking, um pequeno botão de stop aparecerá no canto direito inferior da tela permitindo que termine de se marcar a rota atual.

![ecec13c9-b435-4e03-9be6-763a865fa2dd](https://github.com/JoaoMEspindola/MDS_Trilha_Segura/assets/49202702/791d53fb-6630-4dd7-92e4-7bca3cd6d42b)

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
