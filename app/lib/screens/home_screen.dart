import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:provider/provider.dart';
import '../providers/viagem_provider.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      context.read<ViagemProvider>().carregarViagens();
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Minhas Viagens")),
      body: Consumer<ViagemProvider>(
        builder: (context, provider, child) {
          if (provider.isLoading) return Center(child: CircularProgressIndicator());

          if (provider.errorMessage != null) {
            return Center(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  const Icon(Icons.cloud_off, size: 64, color: Colors.grey),
                  const SizedBox(height: 16),
                  Text(provider.errorMessage!, textAlign: TextAlign.center),
                  const SizedBox(height: 16),
                  ElevatedButton.icon(
                    onPressed: () => provider.carregarViagens(),
                    icon: const Icon(Icons.refresh),
                    label: const Text("Tentar novamente"),
                  ),
                ],
              ),
            );
          }
          
          if (provider.viagens.isEmpty) {
            return Center(child: Text("Nenhuma viagem encontrada."));
          }

          return RefreshIndicator(
            onRefresh: provider.carregarViagens,
            child: ListView.builder(
              itemCount: provider.viagens.length,
              itemBuilder: (context, index) {
                final viagem = provider.viagens[index];
                return Card(
                  margin: EdgeInsets.all(8),
                  child: ListTile(
                    title: Text(viagem.destino, style: TextStyle(fontWeight: FontWeight.bold)),
                    subtitle: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        const SizedBox(height: 4),
                        Row(
                          children: [
                            const Icon(Icons.flight_takeoff, size: 16, color: Colors.blue),
                            const SizedBox(width: 4),
                            Text("Ida: ${DateFormat('dd/MM/yyyy').format(viagem.dataIda)}"),
                          ],
                        ),
                        Row(
                          children: [
                            const Icon(Icons.flight_land, size: 16, color: Colors.green),
                            const SizedBox(width: 4),
                            Text("Volta: ${DateFormat('dd/MM/yyyy').format(viagem.dataVolta)}"),
                          ],
                        ),
                      ],
                    ),
                    trailing: Container(
                      padding: EdgeInsets.all(6),
                      decoration: BoxDecoration(
                        color: _getCorStatus(viagem.status.apiValue).withValues(alpha: 0.2),
                        borderRadius: BorderRadius.circular(4),
                      ),
                      child: Text(
                        viagem.status.label, 
                        style: TextStyle(color: _getCorStatus(viagem.status.apiValue), fontWeight: FontWeight.bold),
                      ),
                    ),
                    onTap: () => Navigator.pushNamed(context, '/detalhes', arguments: viagem),
                  ),
                );
              },
            ),
          );
        },
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () => Navigator.pushNamed(context, '/nova-viagem'),
        child: Icon(Icons.add),
      ),
    );
  }

  Color _getCorStatus(String status) {
    switch (status) {
      case 'AGENDADA': return Colors.blue;
      case 'EM_ANDAMENTO': return Colors.orange;
      case 'CONCLUIDA': return Colors.green;
      case 'CANCELADA': return Colors.red;
      default: return Colors.grey;
    }
  }
}