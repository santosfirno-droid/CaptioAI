# CAPTIO - Gestão Inteligente para Profissionais de Prótese Capilar

Sistema de gestão completo e responsivo projetado para profissionais e estúdios de prótese capilar.

## Funcionalidades Principais

- **Painel Principal (Home)**: Indicadores-chave, notificações com badge, atalhos rápidos, próximos atendimentos confirmados e resumo semanal com mascote 3D.
- **Atendimentos**:
  - Timeline e agenda diária de atendimentos com duração estimada e status.
  - Seletor horizontal de dias da semana.
  - Validação inteligente de sobreposição de horários (impede duplo agendamento no mesmo horário).
  - Gestão de solicitações recebidas via Link Público com botões de Aceitar, Recusar e WhatsApp.
  - Banner para cópia e compartilhamento do link público do profissional (`captio.app/bruno-protese`).
- **Clientes**:
  - Lista completa de clientes com status (Ativo/Inativo), tipo de prótese, histórico de atendimentos e observações.
  - Filtros por status e ordenação (A-Z ou por maior número de atendimentos).
  - Modal de detalhes para agendar, editar dados, histórico ou conversar via WhatsApp.
- **Financeiro & Vendas**:
  - Total vendido por período (7 dias, 30 dias, 90 dias ou personalizado) com gráfico neon de desempenho.
  - Indicadores de Vendas Realizadas, Receita Líquida (margem operacional de 93%) e Ticket Médio.
  - Distribuição das formas de pagamento (PIX 61%, Cartão 30%, Outros 9%).
  - Histórico de transações com registro rápido de novas vendas.
- **Produtos & Insumos**:
  - Catálogo de próteses capilares, insumos (fitas, colas, solventes), cosméticos e vestuário.
  - Controle de estoque com ajuste rápido de unidades (+/-) e status de disponibilidade.
- **Relatórios & Insights**:
  - Gráficos de faturamento semanal, retenção de clientes (89%), clientes mais assíduos e produtos mais vendidos.
- **Página Pública do Cliente**:
  - Link público exclusivo para clientes agendarem avaliação ou manutenção online, escolhendo período (Manhã/Tarde) e enviando observações.
  - Notifica a clínica em tempo real ao ser preenchido.
- **Integração WhatsApp**:
  - Mensagens pré-formatadas para confirmação, remarcação e contato com o cliente prontas para envio ou cópia para a área de transferência.

## Tecnologias

- React 19 + TypeScript
- Vite 6
- Tailwind CSS v4
- Lucide React Icons
- Armazenamento com LocalStorage reativo para persistência de dados
