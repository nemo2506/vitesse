# 🧠 Candidat Manager – Application RH Android 📱

## 🎯 Objectif du projet

Optimiser la gestion des candidats pour le service des Ressources Humaines grâce à une application Android moderne.  
Les RH peuvent :  
✅ Créer / modifier / supprimer des candidats  
⭐ Marquer certains candidats comme **favoris**  

---

## 🧰 Stack Technique

📌 Conformément au **Tech Radar** de l'entreprise :

- 🔷 **Langage :** Kotlin
- 📱 **Plateforme :** Android (SDK stable à jour)
- 🧩 **Architecture :** Clean Architecture (AppModule, UseCases, Repository, ViewModel)
- 💾 **Base de données :** Room (locale)
- 🌐 **Réseau (si besoin) :** Retrofit
- 🧪 **Tests :** JUnit + Mockito

---

## 🗃️ Modèle de Données (Room DB)

### Table principale : `Candidat`

| Champ         | Type        | Contraintes          |
|---------------|-------------|----------------------|
| `id`          | Int         | PK, AutoIncrement    |
| `firstName`   | String      | NOT NULL             |
| `lastName`    | String      | NOT NULL             |
| `phone`       | String      | NULLABLE             |
| `email`       | String      | UNIQUE               |
| `isFavorite`  | Boolean     | Default: `false`     |
| `photoUri`    | String?     | URI de la photo      |

---

## 📐 Architecture Clean

App (Android)
│
├── 📦 presentation (Fragments, ViewModels)
├── 🧠 domain (UseCases, Entities)
├── 🗂️ data (Room, Repositories)
└── 🔧 di (Dagger-Hilt or Koin modules)


- Communication via **UseCases**
- Respect des principes **SOLID**
- Injection de dépendances

---

## 📦 Fonctionnalités Techniques

- 🎨 Application multilingue : `fr` 🇫🇷 & `en` 🇬🇧
- 🧩 Sélecteur de photos Google (gestion de l’URI)
- 🌈 Palette de couleurs & UI basée sur wireframes
- 🧪 Couverture de code avec des **tests unitaires**
- 📂 Architecture modulaire dès le départ
- 🗂️ Menu et navigation par composant Android

---

## 🧪 Tests unitaires

✅ Les tests font **partie intégrante de la DoD** :  
Minimum recommandé : **6 tests unitaires** (UseCases, Repository, ViewModel)

**Outils compatibles :**
- 🧪 JUnit
- 🧪 Mockito
- ✅ Support Android Studio (coverage intégré)

---

## 🏗️ Jalons Techniques Initiaux

1. 🗃️ Création du dépôt Git (`git init` + lien GitHub)
2. 📦 Initialisation du projet Android avec SDK stable
3. 🔧 Implémentation de l'architecture de base (Clean)
4. 🏛️ Modélisation de la base de données (Room)
5. 🎨 Configuration de la palette, icône, langues

---

## 📌 Recommandations

- 🧱 Définissez bien les **prérequis techniques** avant d’implémenter les User Stories
- 🧪 Implémentez les **tests dès le début**
- 📸 Utilisez l’**URI** de la photo sélectionnée pour la base
- 🌍 Gérez bien la **localisation FR / EN**
- 💬 Consultez la **documentation Android officielle**

---

## 🚀 Suivi & Kanban

📋 Le **Kanban** contient :
- 🎯 Les User Stories
- 🎨 Les Wireframes
- 🌈 La palette de couleurs
- 🔍 Les dépendances fonctionnelles

---

## 👨‍💻 Collaboration

👩‍💼 **Product Owner** : Svetlana  
👨‍💻 **Lead Développeur** : Sam  
📬 Suivi technique et questions via échanges avec Sam  

---

> ✍️ *N'oubliez pas de faire des commits réguliers et de maintenir votre documentation à jour pour chaque tâche importante.*


