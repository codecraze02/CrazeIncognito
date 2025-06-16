# Incognito - Minecraft Plugin

**Autor:** CodeCraze (ComplexHub.pl)  
**Wersja:** 1.0.0  
**Wymagana wersja MC:** 1.20.6+  

---

## Opis

Incognito to plugin do Minecraft, który pozwala graczom ukryć swoją prawdziwą tożsamość na serwerze — zmieniając wyświetlany nick oraz skórkę na fałszywe. Dzięki temu możesz zachować anonimowość w rozgrywce.  

---

## Funkcjonalności

- Włączanie i wyłączanie trybu incognito przez komendę `/incognito on|off`.
- Sprawdzanie statusu incognito `/incognito status`.
- Zmiana nicka i skóry gracza na fałszywe w trybie incognito.
- Wyświetlanie prawdziwego nicka tylko dla osób z uprawnieniem `codecraze.incognito.bypass`.
- Przeładowanie konfiguracji pluginu komendą `/reloadincognito`.
- Obsługa konfiguracji wiadomości przez plik JSON.
- Proste uprawnienia do komend.

---

## Komendy

| Komenda                | Opis                                 | Uprawnienie               |
|------------------------|-------------------------------------|---------------------------|
| `/incognito on`        | Włącza tryb incognito                | `incognito.use`           |
| `/incognito off`       | Wyłącza tryb incognito               | `incognito.use`           |
| `/incognito status`    | Pokazuje status trybu incognito      | `incognito.use`           |
| `/reloadincognito`     | Przeładowuje konfigurację pluginu   | `incognito.reload`        |

---

## Uprawnienia

| Uprawnienie                   | Opis                                      | Domyślny dostęp          |
|------------------------------|-------------------------------------------|-------------------------|
| `incognito.use`              | Pozwala korzystać z komendy `/incognito` | Wszystkie grupy          |
| `incognito.reload`           | Pozwala przeładować konfigurację          | Operatorzy (op)          |
| `codecraze.incognito.bypass` | Pozwala widzieć prawdziwe nicki graczy    | Operatorzy (op)          |

---

## Instalacja

1. Pobierz najnowszą wersję pluginu (plik `.jar`).
2. Włóż plik `.jar` do folderu `plugins` na serwerze Minecraft.
3. Uruchom lub przeładuj serwer.
4. Plugin utworzy domyślne pliki konfiguracyjne (`config.json`).
5. Dostosuj pliki konfiguracyjne według potrzeb.
6. Używaj komend opisanych powyżej.

---

## Konfiguracja

Plugin korzysta z plików JSON do konfiguracji i wiadomości. Przykładowe pliki:

- `config.json` — ustawienia pluginu, wszystkie komunikaty i teksty wysyłane do graczy.

---

## Wsparcie

Masz pytania lub potrzebujesz pomocy?  
Skontaktuj się: [ComplexHub.pl] https://discord.com/invite/mXpWpQBsQ5 

---

## Licencja

© 2025 CodeCraze. Wszystkie prawa zastrzeżone.
