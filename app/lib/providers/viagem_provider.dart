import 'package:controle_viagens/repositories/auth_repository.dart';
import 'package:flutter/material.dart';
import '../models/viagem.dart';
import '../repositories/viagem_repository.dart';

class ViagemProvider extends ChangeNotifier {
  final ViagemRepository _repository = ViagemRepository();
  final AuthRepository _authRepository = AuthRepository();

  List<Viagem> _viagens = [];
  List<Viagem> get viagens => _viagens;

  bool _isLoading = false;
  bool get isLoading => _isLoading;

  List<String> _finalidades = [];
  List<String> _transportes = [];
  List<String> get finalidades => _finalidades;
  List<String> get transportes => _transportes;

  String? _errorMessage;
  String? get errorMessage => _errorMessage;

  Future<void> carregarViagens() async {
    if (_isLoading) return;

    _isLoading = true;
    _errorMessage = null;
    Future.microtask(() => notifyListeners());

    try {
      debugPrint('Iniciando busca de viagens...');
      _viagens = await _repository.buscarViagens();
    } catch (e) {
      debugPrint('Erro na busca inicial, tentando refresh...');
      try {
        await _authRepository.refreshToken();
        _viagens = await _repository.buscarViagens();
      } catch (e2) {
        _errorMessage = "Servidor offline ou sem conexão.";
        debugPrint('Erro crítico: $e2');
      }
    } finally {
      _isLoading = false;
      notifyListeners();
      debugPrint('Fim do carregamento. Erro: $_errorMessage');
    }
  }

  Future<void> carregarDominios() async {
    _isLoading = true;
    notifyListeners();
    try {
      _finalidades = await _repository.buscarFinalidades();
      _transportes = await _repository.buscarTransportes();
    } catch (e) {
      debugPrint("Erro ao carregar domínios: $e");
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  Future<bool> criarViagem(Map<String, dynamic> dados) async {
    _isLoading = true;
    _errorMessage = null;
    notifyListeners();
    
    try {
      await _repository.criarViagem(dados);
      await carregarViagens(); 
      return true;
    } catch (e) {
      _errorMessage = e.toString().replaceAll("Exception: ", "");
      return false;
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  Future<bool> atualizarStatus(int id, String novoStatus) async {
    _isLoading = true;
    _errorMessage = null;
    notifyListeners();

    try {
      await _repository.atualizarStatus(id, novoStatus);
      await carregarViagens();
      return true;
    } catch (e) {
      debugPrint(e.toString());
      _errorMessage = e.toString().replaceAll("Exception: ", "");
      return false;
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }
}