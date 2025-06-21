# Collev Ride App

Esta versão adapta o projeto original para servir como um aplicativo de solicitações de corridas estilo taxi/uber. As classes originais de coletas permanecem, mas foram adicionadas novas estruturas para ilustrar o novo fluxo de corridas.

## Novas funcionalidades
- **Corrida**: nova classe de modelo que representa uma viagem solicitada.
- **CorridaStatus**: enum com os estados `SOLICITADA`, `ACEITA`, `CONCLUIDA` e `CANCELADA`.
- **SolicitarCorridaActivity**: tela simples onde um passageiro pode iniciar uma corrida.
- **AceitarCorridaActivity**: tela para o motorista aceitar a corrida.

Estas implementações são um esboço inicial; mais integrações e telas seriam necessárias para uma solução completa.
