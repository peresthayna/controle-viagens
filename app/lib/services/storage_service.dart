import 'package:flutter_secure_storage/flutter_secure_storage.dart';

class StorageService {
  final _storage = const FlutterSecureStorage();

  Future<void> saveToken(String token) async => 
      await _storage.write(key: 'accessToken', value: token);

  Future<void> saveRefreshToken(String token) async => 
      await _storage.write(key: 'refreshToken', value: token);

  Future<String?> getToken() async => await _storage.read(key: 'accessToken');

  Future<String?> getRefreshToken() async => await _storage.read(key: 'refreshToken');

  Future<void> deleteAll() async => await _storage.deleteAll();
}