import 'package:controle_viagens/models/status_viagem.dart';

class Viagem {
  final int id;
  final DateTime dataIda;
  final DateTime dataVolta;
  final String destino;
  final StatusViagem status;
  final String finalidade;
  final String transporte;
  final String? observacoes;
  final DateTime criadoEm;

  Viagem({required this.id, required this.destino, required this.dataIda, required this.dataVolta, required this.status, required this.finalidade, required this.transporte, required this.observacoes, required this.criadoEm,});

  factory Viagem.fromJson(Map<String, dynamic> json) {
    return Viagem(
      id: json['id'],
      destino: json['destino'],
      dataIda: DateTime.parse(json['dataIda']),
      dataVolta: DateTime.parse(json['dataVolta']),
      status: StatusViagem.fromString(json['status']),
      finalidade: json['finalidade'],
      transporte: json['transporte'],
      observacoes: json['observacoes'],
      criadoEm:DateTime.parse(json['criadoEm']),
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'destino': destino,
      'status': status.apiValue,
      'dataIda': dataIda,
      'dataVolta': dataVolta,
      'finalidade': finalidade,
      'transporte': transporte,
      'observacoes': observacoes,
      'criadoEm': criadoEm,
    };
  }
}