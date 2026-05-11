import 'package:flutter/material.dart';

enum StatusViagem {
  agendada('AGENDADA', 'Agendada', Colors.blue),
  emAndamento('EM_ANDAMENTO', 'Em Andamento', Colors.orange),
  concluida('CONCLUIDA', 'Concluída', Colors.green),
  cancelada('CANCELADA', 'Cancelada', Colors.red);

  final String apiValue;
  final String label;
  final Color color;

  const StatusViagem(this.apiValue, this.label, this.color);

  static StatusViagem fromString(String value) {
    return StatusViagem.values.firstWhere(
      (status) => status.apiValue == value,
      orElse: () => StatusViagem.agendada,
    );
  }
}