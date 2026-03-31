# Module Forum & Promotion — ShowMatchGoOn

**Développeur :** Elée Mejri  
**Branche :** `Elee-Module-ForumEtPromotion`

---

## 📋 Modules développés

### 🗨️ Forum (Posts & Commentaires)
- CRUD complet Posts et Commentaires
- Auteur depuis JWT, commentaires repliables
- Contrôle de saisie avec messages d'erreur

### 🏷️ Promotion
- CRUD complet avec interface Admin dédiée
- Vérification de code promo en temps réel
- Gestion actif/inactif

### 👤 Utilisateur
- Inscription/Connexion JWT
- Mot de passe oublié par email
- Page profil (modifier username, email, photo)
- Gestion des rôles et accès Admin

---

## 🛠️ Stack
- **Backend :** Spring Boot 3.3.5 + MongoDB + Spring Security JWT
- **Frontend :** Angular 18 + TailwindCSS

---

## 🚀 Lancement
```bash
# Backend
cd backend && ./mvnw spring-boot:run

# Frontend  
cd frontend && npm install && ng serve
```

---

## 🧪 Tests
- **Backend :** 18 tests JUnit/Mockito
- **Frontend :** 17 tests Jasmine
