import 'package:controle_viagens/models/viagem.dart';
import 'package:controle_viagens/providers/auth_provider.dart';
import 'package:controle_viagens/providers/viagem_provider.dart';
import 'package:controle_viagens/screens/detalhes_viagem_screen.dart';
import 'package:controle_viagens/screens/home_screen.dart';
import 'package:controle_viagens/screens/login_screen.dart';
import 'package:controle_viagens/screens/nova_viagem_screen.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

void main() {
  runApp(
    MultiProvider(
      providers: [
        ChangeNotifierProvider(create: (_) => AuthProvider()),
        ChangeNotifierProvider(create: (_) => ViagemProvider()),
      ],
      child: MyApp(),
    ),
  );
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      theme: ThemeData(primarySwatch: Colors.blue),
      initialRoute: '/login',
      routes: {
        '/login': (context) => LoginScreen(),
        '/home': (context) => HomeScreen(),
        '/nova-viagem': (context) => NovaViagemScreen(),
        '/detalhes': (context) {
          final viagem = ModalRoute.of(context)!.settings.arguments as Viagem;
          return DetalhesViagemScreen(viagem: viagem);
        },
      },
    );
  }
}
