import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:intl/intl.dart';
import '../models/viagem.dart';
import '../providers/viagem_provider.dart';

class DetalhesViagemScreen extends StatelessWidget {
  final Viagem viagem;

  const DetalhesViagemScreen({super.key, required this.viagem});

  @override
  Widget build(BuildContext context) {
    final formatador = DateFormat('dd/MM/yyyy');

    return Scaffold(
      appBar: AppBar(title: const Text("Detalhes da Viagem")),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          children: [
            Card(
              elevation: 4,
              shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
              child: Padding(
                padding: const EdgeInsets.all(20.0),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    _itemInfo("Destino", viagem.destino, Icons.location_on),
                    const Divider(),
                    _itemInfo("Status", viagem.status.label, Icons.info_outline, cor: _getCorStatus(viagem.status.apiValue)),
                    const Divider(),
                    Row(
                      children: [
                        Expanded(child: _itemInfo("Ida", formatador.format(viagem.dataIda), Icons.calendar_today)),
                        Expanded(child: _itemInfo("Volta", formatador.format(viagem.dataVolta), Icons.event)),
                      ],
                    ),
                    const Divider(),
                    _itemInfo("Finalidade", viagem.finalidade, Icons.assignment),
                    const Divider(),
                    _itemInfo("Transporte", viagem.transporte, Icons.directions_car),
                    const Divider(),
                    _itemInfo("Observações", viagem.observacoes ?? "Sem observações", Icons.notes),
                  ],
                ),
              ),
            ),
            const SizedBox(height: 30),
            _buildBotoesAcao(context),
          ],
        ),
      ),
    );
  }

  Widget _itemInfo(String rotulo, String valor, IconData icone, {Color? cor}) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 8.0),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(rotulo, style: const TextStyle(fontSize: 12, color: Colors.grey)),
          const SizedBox(height: 4),
          Row(
            children: [
              Icon(icone, size: 18, color: cor ?? Colors.blueGrey),
              const SizedBox(width: 8),
              Text(valor, style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold, color: cor)),
            ],
          ),
        ],
      ),
    );
  }

  Widget _buildBotoesAcao(BuildContext context) {
    if (viagem.status.apiValue == 'AGENDADA') {
      return Column(
        children: [
          _botaoAcao(context, "Iniciar Viagem", "EM_ANDAMENTO", Colors.orange),
          const SizedBox(height: 12),
          _botaoAcao(context, "Cancelar", "CANCELADA", Colors.red, isOutlined: true),
        ],
      );
    }

    if (viagem.status.apiValue == 'EM_ANDAMENTO') {
      return Column(
        children: [
          _botaoAcao(context, "Concluir", "CONCLUIDA", Colors.green),
          const SizedBox(height: 12),
          _botaoAcao(context, "Cancelar", "CANCELADA", Colors.red, isOutlined: true),
        ],
      );
    }

    return const SizedBox.shrink();
  }

  Widget _botaoAcao(BuildContext context, String label, String novoStatus, Color cor, {bool isOutlined = false}) {
    return SizedBox(
      width: double.infinity,
      height: 50,
      child: isOutlined
        ? OutlinedButton(
          style: OutlinedButton.styleFrom(side: BorderSide(color: cor), foregroundColor: cor),
          onPressed: () => _confirmarMudanca(context, novoStatus, label),
          child: Text(label),
        )
        : ElevatedButton(
          style: ElevatedButton.styleFrom(backgroundColor: cor),
          onPressed: () => _confirmarMudanca(context, novoStatus, label),
          child: Text(label),
        ),
    );
  }

  void _confirmarMudanca(BuildContext context, String novoStatus, String acao) {
    final status = _getLabelStatus(novoStatus);
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: Text("Confirmar $acao"),
          content: Text("Tem certeza que deseja marcar esta viagem como $status?"),
          actions: [
            TextButton(
              onPressed: () => Navigator.pop(context),
              child: const Text("Voltar"),
            ),
            ElevatedButton(
              onPressed: () {
                Navigator.pop(context);
                _executarMudanca(context, novoStatus);
              },
              child: const Text("Confirmar"),
            ),
          ],
        );
      },
    );
  }

  void _executarMudanca(BuildContext context, String novoStatus) async {
    final messenger = ScaffoldMessenger.of(context);
    final navigator = Navigator.of(context);
    final provider = context.read<ViagemProvider>();
    
    final sucesso = await provider.atualizarStatus(viagem.id, novoStatus);
    
    if (sucesso) {
      messenger.showSnackBar(
        const SnackBar(content: Text("Status atualizado com sucesso!")),
      );
      navigator.pop();
    } else {
      messenger.showSnackBar(
        SnackBar(
          backgroundColor: Colors.redAccent,
          behavior: SnackBarBehavior.floating,
          content: Row(
            children: [
              const Icon(Icons.error_outline, color: Colors.white),
              const SizedBox(width: 10),
              Expanded(child: Text(provider.errorMessage ?? "Erro ao atualizar")),
            ],
          ),
        ),
      );
    }
  }

    Color _getCorStatus(String status) {
    switch (status) {
      case 'AGENDADA': return Colors.blue;
      case 'EM_ANDAMENTO': return Colors.orange;
      case 'CONCLUIDA': return Colors.green;
      case 'CANCELADA': return Colors.red;
      default: return Colors.grey;
    }
  }

  String _getLabelStatus(String status) {
    switch (status) {
      case 'AGENDADA': return 'Agendada';
      case 'EM_ANDAMENTO': return 'Em Andamento';
      case 'CONCLUIDA': return 'Concluída';
      case 'CANCELADA': return 'Cancelada';
      default: return 'Agendada';
    }
  }
}