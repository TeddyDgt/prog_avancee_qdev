import pandas as pd
import matplotlib.pyplot as plt
import os

# Définir les dossiers pour les fichiers CSV et les graphiques
input_folder = "perfs"
output_folder = "courbe"
os.makedirs(output_folder, exist_ok=True)

# Fonction pour générer le graphique de speed-up pour la scalabilité forte (Assignment102)
def generate_strong_scaling_speedup():
    csv_file = os.path.join(input_folder, "performanceSS_assignment102.csv")
    df = pd.read_csv(csv_file)
    
    # Calcul du speed-up pour la scalabilité forte
    baseline_time = df['timeDurationNs'].iloc[0]
    df['speedUp'] = baseline_time / df['timeDurationNs']
    
    # Tracer le graphique
    plt.figure(figsize=(10, 6))
    plt.plot(df['numProcessors'], df['speedUp'], marker='o', linestyle='-', label='Speed-up Réel')
    plt.plot(df['numProcessors'], df['numProcessors'], 'r--', label='Speed-up Idéal')
    plt.xlabel('Nombre de processeurs')
    plt.ylabel('Speed-up')
    plt.title('Scalabilité Forte - Speed-up (Assignment102)')
    plt.legend()
    plt.grid(True)
    plt.xticks(df['numProcessors'])
    
    # Enregistrer le graphique
    output_path = os.path.join(output_folder, "performanceSS_speedup.png")
    plt.savefig(output_path)
    plt.show()

# Fonction pour générer le graphique de speed-up pour la scalabilité faible (Assignment102)
def generate_weak_scaling_speedup():
    csv_file = os.path.join(input_folder, "performanceWS_assignment102.csv")
    df = pd.read_csv(csv_file)
    
    # Calcul du speed-up pour la scalabilité faible
    baseline_time = df['timeDurationNs'].iloc[0]
    df['speedUp'] = baseline_time / df['timeDurationNs']
    
    # Tracer le graphique
    plt.figure(figsize=(10, 6))
    plt.plot(df['numProcessors'], df['speedUp'], marker='o', linestyle='-', label='Speed-up Réel')
    plt.axhline(y=1.0, color='r', linestyle='-', label='Speed-up Idéal')
    plt.xlabel('Nombre de processeurs')
    plt.ylabel('Speed-up')
    plt.title('Scalabilité Faible - Speed-up (Assignment102)')
    plt.legend()
    plt.grid(True)
    plt.xticks(df['numProcessors'])
    
    # Enregistrer le graphique
    output_path = os.path.join(output_folder, "performanceWS_speedup.png")
    plt.savefig(output_path)
    plt.show()

# Fonction pour générer le graphique de speed-up pour la scalabilité forte (Pi)
def generate_strong_scaling_speedup_pi():
    csv_file = os.path.join(input_folder, "performanceSS_pi.csv")
    df = pd.read_csv(csv_file)
    
    # Calcul du speed-up pour la scalabilité forte
    baseline_time = df['timeDurationNs'].iloc[0]
    df['speedUp'] = baseline_time / df['timeDurationNs']
    
    # Tracer le graphique
    plt.figure(figsize=(10, 6))
    plt.plot(df['numProcessors'], df['speedUp'], marker='o', linestyle='-', label='Speed-up Réel')
    plt.plot(df['numProcessors'], df['numProcessors'], 'r--', label='Speed-up Idéal')
    plt.xlabel('Nombre de processeurs')
    plt.ylabel('Speed-up')
    plt.title('Scalabilité Forte - Speed-up (Pi)')
    plt.legend()
    plt.grid(True)
    plt.xticks(df['numProcessors'])
    
    # Enregistrer le graphique
    output_path = os.path.join(output_folder, "performanceSS_pi_speedup.png")
    plt.savefig(output_path)
    plt.show()

# Fonction pour générer le graphique de speed-up pour la scalabilité faible (Pi)
def generate_weak_scaling_speedup_pi():
    csv_file = os.path.join(input_folder, "performanceWS_pi.csv")
    df = pd.read_csv(csv_file)
    
    # Calcul du speed-up pour la scalabilité faible
    baseline_time = df['timeDurationNs'].iloc[0]
    df['speedUp'] = baseline_time / df['timeDurationNs']
    
    # Tracer le graphique
    plt.figure(figsize=(10, 6))
    plt.plot(df['numProcessors'], df['speedUp'], marker='o', linestyle='-', label='Speed-up Réel')
    plt.axhline(y=1.0, color='r', linestyle='-', label='Speed-up Idéal')
    plt.xlabel('Nombre de processeurs')
    plt.ylabel('Speed-up')
    plt.title('Scalabilité Faible - Speed-up (Pi)')
    plt.legend()
    plt.grid(True)
    plt.xticks(df['numProcessors'])
    
    # Enregistrer le graphique
    output_path = os.path.join(output_folder, "performanceWS_pi_speedup.png")
    plt.savefig(output_path)
    plt.show()

# Menu pour le choix du graphe
def main():
    print("Choisissez le graphe à afficher :")
    print("1 - Courbe de Speed-up (Scalabilité Forte - Assignment102)")
    print("2 - Courbe de Speed-up (Scalabilité Faible - Assignment102)")
    print("3 - Courbe de Speed-up (Scalabilité Forte - Pi)")
    print("4 - Courbe de Speed-up (Scalabilité Faible - Pi)")
    print("Tapez 'pi' pour afficher les courbes 3 et 4")
    print("Tapez 'all' pour afficher tous les graphes")

    choice = input("Votre choix : ")

    if choice == "1":
        generate_strong_scaling_speedup()
    elif choice == "2":
        generate_weak_scaling_speedup()
    elif choice == "3":
        generate_strong_scaling_speedup_pi()
    elif choice == "4":
        generate_weak_scaling_speedup_pi()
    elif choice.lower() == "pi":
        generate_strong_scaling_speedup_pi()
        generate_weak_scaling_speedup_pi()
    elif choice.lower() == "all":
        generate_strong_scaling_speedup()
        generate_weak_scaling_speedup()
        generate_strong_scaling_speedup_pi()
        generate_weak_scaling_speedup_pi()
    else:
        print("Choix invalide.")

if __name__ == "__main__":
    main()
