import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../providers/auth_provider.dart';

class LoginScreen extends StatefulWidget {
  const LoginScreen({super.key});

  @override
  State<LoginScreen> createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {
  final _formKey = GlobalKey<FormState>();
  final _userController = TextEditingController();
  final _passController = TextEditingController();

  void _handleLogin() async {
    if (_formKey.currentState!.validate()) {
      final messenger = ScaffoldMessenger.of(context);
      final navigator = Navigator.of(context);
      final authProvider = Provider.of<AuthProvider>(context, listen: false);

      final success = await authProvider.login(
        _userController.text, 
        _passController.text
      );

      if (!mounted) return;

      if (success) {
        navigator.pushReplacementNamed('/home');
      } else {
        messenger.showSnackBar(
          SnackBar(content: Text(authProvider.errorMessage ?? "Erro ao entrar"))
        );
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    final isLoading = context.watch<AuthProvider>().isLoading;

    return Scaffold(
      body: Padding(
        padding: EdgeInsets.all(20),
        child: Form(
          key: _formKey,
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Text(
                "Controle de Viagens", 
                style: TextStyle(
                  fontSize: 24, 
                  fontWeight: FontWeight.bold
                )
              ),
              SizedBox(height: 30),
              TextFormField(
                controller: _userController,
                decoration: InputDecoration(labelText: "Usuário"),
                validator: (v) => v!.isEmpty ? "Informe o usuário" : null,
              ),
              TextFormField(
                controller: _passController,
                decoration: InputDecoration(labelText: "Senha"),
                obscureText: true,
                validator: (v) => v!.isEmpty ? "Informe a senha" : null,
              ),
              SizedBox(height: 30),
              isLoading 
                ? CircularProgressIndicator() 
                : ElevatedButton(onPressed: _handleLogin, child: Text("Entrar")),
            ],
          ),
        ),
      ),
    );
  }
}