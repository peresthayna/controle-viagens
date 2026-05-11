import 'dart:convert';
import 'package:http/http.dart' as http;
import '../services/storage_service.dart';

class AuthRepository {
  final String baseUrl = 'http://10.0.2.2:8080';
  final StorageService _storage = StorageService();

  Future<bool> login(String username, String password) async {
    try {
      final response = await http.post(
        Uri.parse('$baseUrl/auth/login'),
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode({'username': username, 'password': password}),
      ).timeout(const Duration(seconds: 10));

      if (response.statusCode == 200) {
        final data = jsonDecode(response.body);
      
        await _storage.saveToken(data['accessToken']);
        await _storage.saveRefreshToken(data['refreshToken']);
        return true;
      }
      return false;
    } catch (e) {
      rethrow;
    }
  }

  Future<bool> refreshToken() async {
    final refreshToken = await _storage.getRefreshToken();
    
    if (refreshToken == null) return false;

    final response = await http.post(
      Uri.parse('$baseUrl/auth/refresh'),
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode({'refreshToken': refreshToken}),
    ).timeout(const Duration(seconds: 5));

    if (response.statusCode == 200) {
      final data = jsonDecode(response.body);
      await _storage.saveToken(data['accessToken']);
      await _storage.saveRefreshToken(data['refreshToken']);
      return true;
    }
    
    await _storage.deleteAll();
    return false;
  }
}