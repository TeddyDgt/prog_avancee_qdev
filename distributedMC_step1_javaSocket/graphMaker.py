import pandas as pd
import matplotlib.pyplot as plt
import os

# Définir les dossiers pour les fichiers CSV et les graphiques
input_folder = "perfs"
output_folder = "courbe"
os.makedirs(output_folder, exist_ok=True)

# Fonction pour générer le graphique de scalabilité forte
def generate_strong_scaling_montecarlo():
    csv_file = os.path.join(input_folder, "MonteCarlo_Distribue_Strong.csv")
    df = pd.read_csv(csv_file)

    # Calcul du speed-up pour la scalabilité forte
    baseline_time = df['Temps'].iloc[0]
    df['SpeedUp'] = baseline_time / df['Temps']

    # Tracer le graphique
    plt.figure(figsize=(10, 6))
    plt.plot(df['Nb Processeurs'], df['SpeedUp'], marker='o', linestyle='-', label='Speed-up Réel')
    plt.plot(df['Nb Processeurs'], df['Nb Processeurs'], 'r--', label='Speed-up Idéal')
    plt.xlabel('Nombre de processeurs')
    plt.ylabel('Speed-up')
    plt.title('Scalabilité Forte - MonteCarlo Distribué')
    plt.legend()
    plt.grid(True)
    plt.xticks(df['Nb Processeurs'])
    
    # Enregistrer le graphique
    output_path = os.path.join(output_folder, "MonteCarlo_Strong_Scaling.png")
    plt.savefig(output_path)
    plt.show()

# Fonction pour générer le graphique de scalabilité faible
def generate_weak_scaling_montecarlo():
    csv_file = os.path.join(input_folder, "MonteCarlo_Distribue_Weak.csv")
    df = pd.read_csv(csv_file)

    # Calcul du speed-up pour la scalabilité faible
    baseline_time = df['Temps'].iloc[0]
    df['SpeedUp'] = baseline_time / df['Temps']

    # Tracer le graphique
    plt.figure(figsize=(10, 6))
    plt.plot(df['Nb Processeurs'], df['SpeedUp'], marker='o', linestyle='-', label='Speed-up Réel')
    plt.axhline(y=1.0, color='r', linestyle='-', label='Speed-up Idéal')
    plt.xlabel('Nombre de processeurs')
    plt.ylabel('Speed-up')
    plt.title('Scalabilité Faible - MonteCarlo Distribué')
    plt.legend()
    plt.grid(True)
    plt.xticks(df['Nb Processeurs'])
    
    # Enregistrer le graphique
    output_path = os.path.join(output_folder, "MonteCarlo_Weak_Scaling.png")
    plt.savefig(output_path)
    plt.show()

# Menu pour le choix du graphe
def main():
    print("Choisissez le graphe à afficher :")
    print("1 - Scalabilité Forte - MonteCarlo Distribué")
    print("2 - Scalabilité Faible - MonteCarlo Distribué")
    print("Tapez 'montecarlo' pour afficher les deux graphes")
    print("Tapez 'all' pour afficher tous les graphes existants")

    choice = input("Votre choix : ")

    if choice == "1":
        generate_strong_scaling_montecarlo()
    elif choice == "2":
        generate_weak_scaling_montecarlo()
    elif choice.lower() == "montecarlo":
        generate_strong_scaling_montecarlo()
        generate_weak_scaling_montecarlo()
    elif choice.lower() == "all":
        generate_strong_scaling_montecarlo()
        generate_weak_scaling_montecarlo()
    else:
        print("Choix invalide.")

if __name__ == "__main__":
    main()
