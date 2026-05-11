import 'package:controle_viagens/providers/viagem_provider.dart';
import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:provider/provider.dart';

class NovaViagemScreen extends StatefulWidget {
  const NovaViagemScreen({super.key});

  @override
  State<NovaViagemScreen> createState() => _NovaViagemScreenState();
}

class _NovaViagemScreenState extends State<NovaViagemScreen> {
  final _formKey = GlobalKey<FormState>();
  final _destinoController = TextEditingController();
  final _obsController = TextEditingController();
  DateTime? _dataIda;
  DateTime? _dataVolta;
  String? _finalidadeSelecionada;
  String? _transporteSelecionado;

  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      context.read<ViagemProvider>().carregarDominios();
    });
  }

  Future<void> _selecionarData(BuildContext context, bool isIda) async {
    DateTime initialDate = isIda ? DateTime.now() : (_dataIda != null ? _dataIda! : DateTime.now());

    final picked = await showDatePicker(
      context: context,
      initialDate: initialDate,
      firstDate: initialDate,
      lastDate: DateTime(2030),
    );
    if (picked != null) {
      setState(() => isIda ? _dataIda = picked : _dataVolta = picked);
    }
  }

  @override
  Widget build(BuildContext context) {
    final provider = context.watch<ViagemProvider>();

    return Scaffold(
      appBar: AppBar(title: Text("Nova Viagem")),
      body: provider.isLoading 
        ? Center(child: CircularProgressIndicator()) 
        : Form(
            key: _formKey,
            child: ListView(
              padding: EdgeInsets.all(16),
              children: [
                TextFormField(
                  controller: _destinoController,
                  decoration: InputDecoration(labelText: "Destino *"),
                  validator: (v) => v!.isEmpty ? "Campo obrigatório" : null,
                ),
                ListTile(
                  title: Text(_dataIda == null ? "Selecionar Data Ida *" : "Ida: ${DateFormat('dd/MM/yyyy').format(_dataIda!)}"),
                  trailing: Icon(Icons.calendar_today),
                  onTap: () => _selecionarData(context, true),
                ),
                ListTile(
                  title: Text(_dataVolta == null ? "Selecionar Data Volta *" : "Volta: ${DateFormat('dd/MM/yyyy').format(_dataVolta!)}"),
                  trailing: Icon(Icons.calendar_today),
                  onTap: () => _selecionarData(context, false),
                ),
                DropdownButtonFormField<String>(
                  initialValue: _finalidadeSelecionada,
                  hint: const Text("Finalidade *"),
                  items: provider.finalidades.map((f) => DropdownMenuItem(value: f, child: Text(f))).toList(),
                  onChanged: (v) => setState(() => _finalidadeSelecionada = v),
                  validator: (v) => v == null ? "Selecione uma finalidade" : null,
                ),
                DropdownButtonFormField<String>(
                  initialValue: _transporteSelecionado,
                  hint: const Text("Transporte *"),
                  items: provider.transportes.map((f) => DropdownMenuItem(value: f, child: Text(f))).toList(),
                  onChanged: (v) => setState(() => _transporteSelecionado = v),
                  validator: (v) => v == null ? "Selecione um transporte" : null,
                ),
                TextFormField(
                  controller: _obsController,
                  decoration: const InputDecoration(
                    labelText: "Observações",
                    hintText: "Ex: Levar documentos da reunião",
                    alignLabelWithHint: true,
                  ),
                  maxLines: 3,
                  keyboardType: TextInputType.multiline,
                ),
                SizedBox(height: 20),
                ElevatedButton(
                  onPressed: () async {
                    if (_formKey.currentState!.validate() && _dataIda != null && _dataVolta != null) {
                      final dados = {
                        "destino": _destinoController.text,
                        "dataIda": DateFormat('yyyy-MM-dd').format(_dataIda!),
                        "dataVolta": DateFormat('yyyy-MM-dd').format(_dataVolta!),
                        "finalidade": _finalidadeSelecionada,
                        "transporte": _transporteSelecionado,
                        "observacoes": _obsController.text,
                      };

                      final messenger = ScaffoldMessenger.of(context);
                      final navigator = Navigator.of(context);

                      final sucesso = await provider.criarViagem(dados);

                      if (!mounted) return;

                      if (sucesso) {
                        messenger.showSnackBar(
                          const SnackBar(content: Text("Viagem agendada com sucesso!")),
                        );
                        navigator.pop();
                      } else {
                        messenger.showSnackBar(
                          SnackBar(
                            content: Text(provider.errorMessage ?? "Erro ao salvar viagem. Verifique os dados."), 
                            backgroundColor: Colors.red,
                            action: SnackBarAction(
                              label: 'OK',
                              textColor: Colors.white,
                              onPressed: () {},
                            ),
                          ),
                        );
                      }
                    }
                  },
                  child: Text("Salvar Viagem"),
                )
              ],
            ),
          ),
    );
  }

  @override
  void dispose() {
    _obsController.dispose();
    _destinoController.dispose();
    super.dispose();
  }
}