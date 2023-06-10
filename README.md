# Trilha Segura - MVP

O objetivo final do aplicativo Trilha Segura será se apresentar como forma de proporcionar mais conforto e segurança aos seus usuários, tendo como principal ponto positivo, os sistemas apoio e de pins. Seu sistema de apoio possui dois diferentes alarmes que podem ser acionados pelo ciclista, um de apoio médico, e outro de apoio mecânico. Já para o sistema de pins, imaginou-se proporcionar a mesma experiência que um motorista atrasado tem, ao acessar o Waze e decidir, a partir daí, qual será a melhor rota a se seguir.

## Funcionalidades

O objetivo deste MVP (Minimum Viable Product) é fornecer funcionalidades básicas para testar e validar o conceito do aplicativo. Possuindo assim as seguintes funcionalidades:

1. **Set Pin:** Ao clicar no botão "Set Pin", o usuário pode adicionar um marcador (PIN) em uma posição específica do mapa (a que ele está no momento). Isso permite que os usuários marquem locais de interesse ou pontos de referência relevantes para futuras referências. Neste MVP o aplicativo possui um conjunto inicial de eventos que podem ser sinalizados ao longo da trilha (Imagem 1). Sendo as seguintes opções: Pista Escorregadia, Buraco, Mataburro e Animal na Trilha. Esses eventos servem para demonstrar a capacidade do aplicativo de sinalizar pontos de interesse e possíveis obstáculos ao longo do caminho, demonstrando.

Imagem 1 aqui

2. **Start Tracking:** Ao clicar no botão "Start Tracking", o aplicativo registra o caminho percorrido pelo usuário em tempo real (Imagem 2). Isso permite que os usuários gravem suas trilhas, importante para que revisitem-nas posteriormente ou as compartilhem com amigos.

Imagem 2 aqui

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
