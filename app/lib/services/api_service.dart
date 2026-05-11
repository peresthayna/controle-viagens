import 'package:http/http.dart' as http;
import 'storage_service.dart';

class ApiService {
  final String baseUrl = 'http://192.168.100.185:8080';
  final StorageService _storage = StorageService();

  Future<http.Response> get(String endpoint) async {
    final token = await _storage.getToken();
    
    return await http.get(
      Uri.parse('$baseUrl$endpoint'),
      headers: {
        'Content-Type': 'application/json',
        if (token != null) 'Authorization': 'Bearer $token',
      },
    );
  }

  Future<http.Response> post(String endpoint, dynamic body) async {
    final token = await _storage.getToken();

    return await http.post(
      Uri.parse('$baseUrl$endpoint'),
      headers: {
        'Content-Type': 'application/json',
        if (token != null) 'Authorization': 'Bearer $token',
      },
      body: body,
    );
  }
}