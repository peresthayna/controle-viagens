import 'dart:async';
import 'dart:convert';
import 'package:http/http.dart' as http;
import '../models/viagem.dart';
import '../services/storage_service.dart';

class ViagemRepository {
  final String baseUrl = 'http://10.0.2.2:8080';
  final StorageService _storage = StorageService();

  Future<List<Viagem>> buscarViagens() async {
    final token = await _storage.getToken();

    final response = await http.get(
      Uri.parse('$baseUrl/viagens'),
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer $token',
      },
    ).timeout(const Duration(seconds: 5));

    if (response.statusCode == 200) {
      List<dynamic> body = jsonDecode(utf8.decode(response.bodyBytes));
      return body.map((item) => Viagem.fromJson(item)).toList();
    } else if (response.statusCode == 401) {
      throw Exception("Sessão expirada");
    } else {
      throw Exception("Erro ao buscar viagens");
    }
  }

  Future<List<String>> buscarFinalidades() async {
    final token = await _storage.getToken();

    final response = await http.get(
      Uri.parse('$baseUrl/dominios/finalidades'),
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer $token',
      },
    ).timeout(const Duration(seconds: 5));

    if (response.statusCode == 200) {
      return List<String>.from(jsonDecode(utf8.decode(response.bodyBytes)));
    }
    throw Exception("Erro ao buscar finalidades");
  }

  Future<List<String>> buscarTransportes() async {
    final token = await _storage.getToken();

    final response = await http.get(
      Uri.parse('$baseUrl/dominios/transportes'),
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer $token',
      },
    ).timeout(const Duration(seconds: 5));
    
    if (response.statusCode == 200) {
      return List<String>.from(jsonDecode(utf8.decode(response.bodyBytes)));
    }
    throw Exception("Erro ao buscar transportes");
  }

  Future<void> criarViagem(Map<String, dynamic> dados) async {
    final token = await _storage.getToken();

    final response = await http.post(
      Uri.parse('$baseUrl/viagens'),
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer $token',
      },
      body: jsonEncode(dados),
    ).timeout(const Duration(seconds: 5));

    if (response.statusCode == 201) {
      return;
    }

    if (response.statusCode == 422 || response.statusCode == 400) {
      final body = jsonDecode(utf8.decode(response.bodyBytes));
      throw Exception(body['mensagem'] ?? 'Erro de validação no servidor');
    }

    throw Exception('Erro inesperado ao salvar viagem');
  }

  Future<void> atualizarStatus(int id, String novoStatus) async {
    try {
      final token = await _storage.getToken();
      
      final response = await http.patch(
        Uri.parse('$baseUrl/viagens/$id/status'),
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer $token',
        },
        body: jsonEncode({'status': novoStatus}),
      ).timeout(const Duration(seconds: 5));

      if (response.statusCode == 200) return;

      if (response.statusCode == 422) {
        final errorData = jsonDecode(utf8.decode(response.bodyBytes));
        throw Exception(errorData['mensagem'] ?? "Transição inválida");
      }

      throw Exception("Erro inesperado ao atualizar status");
      
    } on TimeoutException catch (_) {
      throw Exception("O servidor demorou muito para responder. Tente novamente.");
    } catch (e) {
      throw Exception("Não foi possível conectar ao servidor.");
    }
  }
}